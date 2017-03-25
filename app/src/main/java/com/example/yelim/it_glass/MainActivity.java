package com.example.yelim.it_glass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    final DatabaseManager dbManager = new DatabaseManager(MainActivity.this, DatabaseManager.DB_NAME + ".db", null, 1);
    ServerDatabaseManager serverDBM = new ServerDatabaseManager();
    Button insButton;
    Button button3;
    Button button4;
    Button button5;
    Button button6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("DATABASE", "---------" + dbManager.getDatabasePath() + "---------");

        insButton = (Button) findViewById(R.id.insButton);
        insButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("MainActivity", "Token : " + token);
                Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });

        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serverDBM.hasID("시험") == false) {
                    serverDBM.saveUserID("시험");
                }
                //Log.d("MainActivity", "Token : " + token);
                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverDBM.deleteFriend("시험", "한");
                //Log.d("MainActivity", "Token : " + token);
                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverDBM.addFriend("시험", "한");
                //Log.d("MainActivity", "Token : " + token);
                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverDBM.changeLightColor("시험", "한", 0, 0, 0);
                //Log.d("MainActivity", "Token : " + token);
                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });

        serverTest();



    }

    void serverTest() {

    }
}
