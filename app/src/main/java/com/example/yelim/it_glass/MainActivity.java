package com.example.yelim.it_glass;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_MAIN = 2000;
    private final int MAX_PERCENT = 60;
    final DatabaseManager dbManager = new DatabaseManager(MainActivity.this, DatabaseManager.DB_NAME + ".db", null, 1);
    ViewPager vp;
    LinearLayout ll;
    TextView setting;
    Context mContext;
    MainFragment mainFragment;
    TextViewCallBack tvCallBack;

    public static int alcoholPercent = 4;
    private Alcoholysis alcoholysis;
    private String[] info;
    static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Log.d("DATABASE", "---------" + dbManager.getDatabasePath() + "---------");

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.what == 1) {
                    mainFragment.updateTextView("avg_drink", (String) msg.obj);
                }
            }
        };
        vp = (ViewPager) findViewById(R.id.vp);
        ll = (LinearLayout) findViewById(R.id.ll);
        setting = (TextView) findViewById(R.id.setting);
        TextView tab_first = (TextView) findViewById(R.id.tab_first);
        TextView tab_second = (TextView) findViewById(R.id.tab_second);
        info = getIntent().getStringArrayExtra("avg_drink");
        if(info != null && info[0].equals("refresh")) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    super.run();
                    avgDrinkAlgorithm();
                }
            };
            t.start();
        }

        selectDrink();

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        tab_first.setOnClickListener(movePageListener);
        tab_first.setTag(0);
        tab_second.setOnClickListener(movePageListener);
        tab_second.setTag(1);

        tab_first.setSelected(true);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int i = 0;
                while (i < 2) {
                    if (position == i) {
                        ll.findViewWithTag(i).setSelected(true);
                    } else {
                        ll.findViewWithTag(i).setSelected(false);
                    }
                    i++;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MAIN);
            }
        });

        setBtManager();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String[] warning = {"과음하셨어요! 오늘은 이제 그만!"};
/*
        //0 = request code
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notiBuilder = new NotificationCompat
                .Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("IT잔")
                .setContentText(warning[0])
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notiBuilder.build());
*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "---------- MainActivityResult");
        Log.d("REQUEST_CODE", "---------- " + requestCode);

        if (requestCode == REQUEST_CODE_MAIN) {
            if (data.getExtras().getString("app_restart").equals("true")) {
                Intent mStartActivity = new Intent(mContext, LogoActivity.class);
                int mPendingIntentId = 0000;
                PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager amr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                amr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= 21) {
                    finishAndRemoveTask();
                } else {
                    finishAffinity();
                }
            }
        }
    }

    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            Log.d("TAG", "---------- " + tag);

            int i = 0;
            while (i < 2) {
                if (tag == i) {
                    ll.findViewWithTag(i).setSelected(true);
                } else {
                    ll.findViewWithTag(i).setSelected(false);
                }
                i++;
            }
            vp.setCurrentItem(tag);
        }
    };

    private void setBtManager() {

        BluetoothManager.setContext(this);
        BluetoothManager.checkBluetooth();

        BluetoothManager.CallBack callBack = new BluetoothManager.CallBack() {
            @Override
            public void callBackMethod(int flag, String fromDeviceMessage) {
                Log.d("Bluetooth", "I'm MainActivity. I got your message : " + fromDeviceMessage.toString());
                switch (flag) {
                    case 100:
                        Log.d("Bluetooth", "DEVICE IS CONNECTED");
                        break;
                    case 200:
                        // 술을 마신다
                        if (fromDeviceMessage.toString().equals("drink")) {
                            ServerDatabaseManager.turnOnDrinkTiming();
                            Log.d("Bluetooth", "NEW MESSAGE FROM YOUR DEVICE : " + fromDeviceMessage.toString());
                        }

                        // 다 마셨다
                        else if (fromDeviceMessage.toString().equals("drank")) {
                            ServerDatabaseManager.turnOffDrinkTiming();
                            Log.d("Bluetooth", "NEW MESSAGE FROM YOUR DEVICE : " + fromDeviceMessage.toString());
                        } else {
                            int vol = (int)Float.parseFloat(fromDeviceMessage.toString());
                            String[] data = new String[2];
                            data[0] = ServerDatabaseManager.getTime();
                            data[1] = vol + "";
                            dbManager.insertToDatabase(Database.DrinkRecordTable._TABLENAME, data);
                            checkAlchol();
                            final String temp = data[1];
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mainFragment.updateTextView("realtime_drink", temp);
                                }
                            });
                        }
                        break;
                }
            }
        };
        BluetoothManager.setCallBack(callBack);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    mainFragment = new MainFragment();
                    return mainFragment;
                case 1:
                    return new FriendlistFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void selectDrink() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("술의 도수를 선택하세요.");

        // 페어링 된 블루투스 장치의 이름 목록 작성
        List<String> listItems = new ArrayList<String>();
        for (int i = 0; i < MAX_PERCENT; i++) {
            listItems.add((i + 1) + "%");
        }
        listItems.add("취소");    // 취소 항목 추가

        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == MAX_PERCENT) {
                    alcoholPercent = 4;
                    Toast.makeText(mContext, "맥주 기준 4%로 설정됩니다.", Toast.LENGTH_SHORT).show();
                } else {
                    String tempPercent = items[item].toString().replace("%", "");
                    alcoholPercent = Integer.parseInt(tempPercent);
                    Log.d("SELECT PERCENT",alcoholPercent+"");
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainFragment.updateTextView("none", "");
                    }
                });
            }
        });

        builder.setCancelable(false);    // 뒤로 가기 버튼 사용 금지
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void avgDrinkAlgorithm() {
        String drink = dbManager.getLastDateDrink();
        if(drink == null) {

        }
        else if(drink.equals("0")){

        }
        else {
            int iDrink = Integer.parseInt(drink);
            int num = dbManager.getRecordNum();
            if(iDrink > DatabaseManager.avgDrink + 350) {
                iDrink = (int) (0.5 * iDrink);
            }
            else if(iDrink < DatabaseManager.avgDrink - 350) {
                iDrink = (int) (1.5 * iDrink);
            }
            else {

            }
            DatabaseManager.avgDrink = (DatabaseManager.avgDrink * num + iDrink) / (num + 1);
            dbManager.updateDatabase(Database.UserTable._TABLENAME, Database.UserTable.AVG_DRINK, DatabaseManager.avgDrink+"", Database.UserTable.ID, ServerDatabaseManager.getLocalUserID());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mainFragment.updateTextView("avg_drink", DatabaseManager.avgDrink+"");
                }
            });
        }
    }

    private void checkAlchol() {
        Log.d("checkAlchol", ServerDatabaseManager.getLocalUserDrink() + " ml");
        if(ServerDatabaseManager.getLocalUserDrink() > DatabaseManager.avgDrink + 350) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            String[] warning = {"과음하셨어요! 오늘은 이제 그만!"};

            //0 = request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notiBuilder = new NotificationCompat
                    .Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("IT잔")
                    .setContentText(warning[0])
                    .setAutoCancel(true)
                    .setSound(notificationSound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notiBuilder.build());
        }

    }

}
