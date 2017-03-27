package com.example.yelim.it_glass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final DatabaseManager dbManager = new DatabaseManager(MainActivity.this, DatabaseManager.DB_NAME + ".db", null, 1);
    private BluetoothManager btManager;
    static int howMany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("DATABASE", "---------" + dbManager.getDatabasePath() + "---------");
        howMany=0;
        setBtManager();
    }

    private void setBtManager(){
        btManager=new BluetoothManager(this);

        BluetoothManager.CallBack callBack = new BluetoothManager.CallBack() {
            @Override
            public void callBackMethod(int flag, String fromDeviceMessage) {
                Log.d("Bluetooth","I'm MainActivity. I got your message : "+fromDeviceMessage);

                switch(flag){
                    case 100 :
                        Log.d("Bluetooth","DEVICE IS CONNECTED");
                        break;
                    case 200 :
                        Log.d("Bluetooth","NEW MESSAGE FROM YOUR DEVICE : "+fromDeviceMessage);
                        break;
                }
            }
        };
        btManager.setCallBack(callBack);


    }

}
