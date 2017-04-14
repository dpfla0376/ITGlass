package com.example.yelim.it_glass;

import android.os.Handler;
import android.os.Message;
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
    ItemFriendListAdapter friendListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("DATABASE", "---------" + dbManager.getDatabasePath() + "---------");


        friendListView = (ListView) findViewById(R.id.friend_list);
        ServerDatabaseManager.getFriend("한");
        //ServerDatabaseManager.getFriend(ServerDatabaseManager.getLocalUserID());
        Log.d("MainActivity", "---------------out of getFriend");
        Callback callBack = new Callback() {
            @Override
            public void callBackMethod() {
                Log.d("MainActivity", "---------------in callBackMethod");
                //ServerDatabaseManager.getFriend(LocalUserID);
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
                } else {
                    Log.e("MainActivity", "---------------list is empty");
                }

                /**
                 * < 친구목록 생성 과정 >
                 * 1. 서버에서 로컬 사용자의 이름으로 친구 목록을 받아와 ServerDBM.friendList에 저장 (친구ID, 전구값)
                 * 2. friendList에 저장된 친구ID를 서버에서 검색하여 drink값을 받아와 ServerDBM.tempFriendDrink에 저장 (버퍼. 구분은 flag)
                 * 3. tempFriendDrink의 값을 실제 데이터인 friendList에 옮김
                 * 4. flag값과 tempFriendDrink를 초기화
                 * 5. friendList를 가지고 friendListView 생성
                 */
                friendListAdapter = new ItemFriendListAdapter(MainActivity.this);
                Log.d("MainActivity", "---------------list view making start");
                // 서버에서 drink값 받아옴
                ServerDatabaseManager.getFriendListDrinkAmount();
                // drink값을 다 받아왔는지 확인
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        while (ServerDatabaseManager.getFlag() != ServerDatabaseManager.getFriendList().size()) {
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //다 받아왔으면 ServerDatabaseManager의 friendList에 값을 재설정.
                        //다음번 작업을 위해 flag와 buffer를 비움.
                        ServerDatabaseManager.setFriendListDrinkAmount();
                        ServerDatabaseManager.setFlag(0);
                        ServerDatabaseManager.clearTempFriendDrink();
                        makeFriendListView();

                        //main thread 외의 thread에서는 UI작업 불가능. handler로 처리.
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                };
                t.start();
            }

        };
        ServerDatabaseManager.setCallBack(callBack);
    }

    void makeFriendListView() {
        for (int i = 0; i < ServerDatabaseManager.getFriendList().size(); i++) {
            friendListAdapter.addItem(
                    new ItemFriend(
                            ServerDatabaseManager.getFriendList().get(i).getfID(),
                            ServerDatabaseManager.getFriendList().get(i).getfDrink() + "잔",
                            ServerDatabaseManager.getFriendList().get(i).getfLight()));
        }

    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            friendListView.setAdapter(friendListAdapter);
            Log.d("MainActivity", "---------------list adpader set");
        }
    };

}
