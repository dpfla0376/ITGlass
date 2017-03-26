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
                Log.d("Bluetooth","I'm MainActivity. I got your message");
                switch(flag){
                    case 100 :
                        Toast.makeText(getApplicationContext(),"Device is connected",Toast.LENGTH_LONG);
                        break;
                    case 200 :
                        Toast.makeText(getApplicationContext(),fromDeviceMessage,Toast.LENGTH_LONG);
                        break;
                }
            }
        };
        btManager.setCallBack(callBack);

    }

}
