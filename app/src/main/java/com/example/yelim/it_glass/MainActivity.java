package com.example.yelim.it_glass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final DatabaseManager dbManager = new DatabaseManager(MainActivity.this, DatabaseManager.DB_NAME + ".db", null, 1);
    ListView friendListView;
    Button insButton;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("DATABASE", "---------" + dbManager.getDatabasePath() + "---------");
/*
        insButton = (Button) findViewById(R.id.insButton);
        insButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("MainActivity", "Token : " + token);
                Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });
*/
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //콜백 써야될듯!!
                ServerDatabaseManager.getFriend("한");
                Log.d("MainActivity", "---------------out of getFriend");
                Callback callBack = new Callback(){
                    @Override
                    public void callBackMethod() {
                        Log.d("MainActivity", "---------------in callBackMethod");
                        //ServerDatabaseManager.getFriend("한");
                        Log.d("MainActivity", "---------------FriendListSize : " + ServerDatabaseManager.getFriendList().size());
                        if (ServerDatabaseManager.getFriendList().size() > 0) {
                            Log.d("MainActivity", "---------------in callBackMethod [ if ]");
                            String token = "";
                            for (int i = 0; i < ServerDatabaseManager.getFriendList().size(); i++) {
                                token = token + ServerDatabaseManager.getFriendList().get(i).getfID() + " : "
                                        + ServerDatabaseManager.getFriendList().get(i).getfLight() + " / ";
                            }
                            Log.d("MainActivity", "Token : " + token);
                            Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.e("MainActivity", "---------------list is empty");
                        }
                    }

                };
                ServerDatabaseManager.setCallBack(callBack);

            }
        });
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ServerDatabaseManager.hasID("시험") == false) {
                    ServerDatabaseManager.saveUserID("시험");
                }
                //Log.d("MainActivity", "Token : " + token);
                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerDatabaseManager.deleteFriend("시험", "한");
                //Log.d("MainActivity", "Token : " + token);
                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerDatabaseManager.addFriend("한", "시험");
                //Log.d("MainActivity", "Token : " + token);
                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerDatabaseManager.changeLightColor("시험", "한", 0, 0, 0);
                //Log.d("MainActivity", "Token : " + token);
                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT);
            }
        });

        friendListView = (ListView) findViewById(R.id.friend_list);
        //friendListView.setAdapter(new ItemFriendListAdapter(MainActivity.this, ));
    }



}
