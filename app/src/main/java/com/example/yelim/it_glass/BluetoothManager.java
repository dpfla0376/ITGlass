package com.example.yelim.it_glass;

/**
 * Created by sehyeon on 2017-03-23.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.logging.LogRecord;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class BluetoothManager {

    // 블루투스 활성 상태 변경 결과
    static final int REQUEST_ENABLE_BT = 13;
    static final int CONNECTED = 100;
    static final int MESSAGE = 200;

    private static BluetoothAdapter myBluetoothAdapter;
    private static Set<BluetoothDevice> deviceList;
    private static BluetoothDevice myDevice;
    private static BluetoothSocket mySocket = null;

    private static OutputStream toDeviceStream = null;
    private static InputStream fromDeviceStream = null;

    private static Thread thread = null;
    private static Handler handler;

    public static Context context;
    public static CallBack callBack;

    private static boolean isReady = false;
    private static ArrayList<String>  waitingTask;

    /**
     * int howMany : 사용자가 마신 술의 양
     */
    BluetoothManager(Context context) {
        this.context = context;
        checkBluetooth();
        waitingTask = new ArrayList<>();
    }

    /**
     * 블루투스의 활성 상태 체크 , 활성화
     * return : 블루투스 미지원일 때 -1, 활성 상태가 아닐때 0, 활성 상태일때 1.
     * 블루투스가 활성 상태일때 '장치 선택(selectDevice())'이 실행됨
     */
    static int checkBluetooth() {

        // 이거 어디에 해야되는지 몰라서 일단 여기에 적엇어여..
        waitingTask = new ArrayList<>();

        Log.d("Bluetooth", "Let's checking bluetooth state");
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (myBluetoothAdapter == null) {
            // 블루투스 미지원
            Toast.makeText(context,"블루투스 미지원으로 기능을 이용할 수 없습니다.", Toast.LENGTH_LONG).show();
            return -1;
        } else {
            if (myBluetoothAdapter.isEnabled()) {
                // 활성 상태. selectDevice(); 실행하면 됨.
                selectDevice();
                return 1;
            } else // 구현해야됨
                Toast.makeText(context,"단말 연결 후 사용해주세요.", Toast.LENGTH_LONG).show();
                return 0;

        }
    }

    /**
     * 장치 선택 및 연결
     * return : 장치가 없는 경우 -1, 아닌 경우 0
     * 장치를 선택한 경우 connectToMyDevice(String deviceName) 실행하여 연결시도.
     */
    private static void selectDevice() {
        deviceList = myBluetoothAdapter.getBondedDevices(); // 페어링된 장치 목록

        if (deviceList.size() == 0) { // 없는 경우
            Log.d("BLUETOOTH", "NO PAIRED DEVICE");
        } else {
            // 장치 선택하기
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("블루투스 장치 선택");

            // 페어링 된 블루투스 장치의 이름 목록 작성
            List<String> listItems = new ArrayList<String>();
            for (BluetoothDevice device : deviceList) {
                listItems.add(device.getName());
            }
            listItems.add("취소");    // 취소 항목 추가

            final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (item == deviceList.size()) {
                        // 연결할 장치를 선택하지 않고 '취소'를 누른 경우
                        Toast.makeText(context,"단말 연결 후 사용해주세요.", Toast.LENGTH_LONG).show();

                    } else {
                        // 연결할 장치를 선택한 경우
                        // 선택한 장치와 연결을 시도함
                        connectToMyDevice(items[item].toString());
                    }
                }
            });

            builder.setCancelable(false);    // 뒤로 가기 버튼 사용 금지
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    /**
     * myDeviceName 장치에 연결한다.
     * 소켓 i/o, 쓰레드를 이용해 데이터를 송수신한다.
     */
    private static int connectToMyDevice(String myDeviceName) {

        Log.d("Bluetooth", "OK, Let's connect to your device");
        myDevice = getMyDeviceFromList(myDeviceName);

        // 블루투스 프로토콜
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // secure insecure 알아보기
            mySocket = myDevice.createRfcommSocketToServiceRecord(uuid);

            // connected!!
            mySocket.connect();
            Log.d("Bluetooth", "connected.");

            // 스트림 준비
            toDeviceStream = mySocket.getOutputStream();
            fromDeviceStream = mySocket.getInputStream();

            Log.d("Bluetooth", "OK, I'm READY");
            isReady = true;
            callBack.callBackMethod(CONNECTED, "CONNECTED");

            // 다음 단계 : 데이터 수신
            listenData();

            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"단말 연결에 실패했습니다.", Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    private static void listenData() {
        Log.d("-----BT-----", "I'm listening");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] readSaveBuffer = new byte[1024];
                int savePosition = 0;
                while (true) {
                    try {
                        int remainedBufferSize = fromDeviceStream.available();

                        if (remainedBufferSize > 0) {
                            byte[] buffer = new byte[remainedBufferSize]; // 남은 버퍼 공간만큼만 쓰자
                            fromDeviceStream.read(buffer);

                            for (int i = 0; i < remainedBufferSize; i++) {
                                if (buffer[i] == '\n') {
                                    // 기기로부터 한 문장 입력을 다 받았다.
                                    byte[] encodedBytes = new byte[savePosition];
                                    // 여태 하나하나 잘 모아왔던 거 encodedBytes에 복사하자
                                    System.arraycopy(readSaveBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String fromDeviceMessage = new String(encodedBytes, "UTF-8");
                                    savePosition = 0;

                                    // 최종 수신된 문자열은 fromDeviceMessage이다.
                                    // data를 넘겨주면 되겠음.
                                    callBack.callBackMethod(MESSAGE, fromDeviceMessage);

                                    Log.d("Bluetooth", "From Device : " + fromDeviceMessage);

                                } else {
                                    readSaveBuffer[savePosition++] = buffer[i];
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    // 블루투스 장치 이름을 가지고, 블루투스 장치 객체를 리턴
    private static BluetoothDevice getMyDeviceFromList(String myDeviceName) {
        BluetoothDevice selectedDevice = null;
        // 찾은 디바이스 처음부터 끝까지
        for (BluetoothDevice device : deviceList) {
            if (myDeviceName.equals(device.getName())) {
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }

    public static void writeData(String toDeviceString) {
        Log.d("BLUETOOTH", "IN write data to my device");
        if (isReady) {
            while(waitingTask.size()>0)
            {
                try {
                    toDeviceStream.write(waitingTask.get(0).toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                waitingTask.remove(0);
            }
            try {
                String temp = new String(toDeviceString.getBytes());
                Log.d("BLUETOOTH", "trying write data to my device" + temp);
                toDeviceStream.write(temp.getBytes());
            } catch (IOException e) {
                Log.e("BLUETOOTH", "fail write data to my device");
                e.printStackTrace();
            }
        }
        else
        {
            waitingTask.add(toDeviceString);
        }
    }

    public interface CallBack {
        void callBackMethod(int flag, String fromDeviceMessage);
    }
    public static void setContext(Context context){
        BluetoothManager.context = context;
    }

    public static void setCallBack(CallBack callBack) {
        BluetoothManager.callBack= callBack;
    }

}