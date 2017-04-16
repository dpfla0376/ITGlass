package com.example.yelim.it_glass;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yelim on 2017-03-25.
 */

/**
 * Firebase 상의 Realtime Database와 연동하여 Data를 관리
 */
public class ServerDatabaseManager {
    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mDatabaseReference;
    private static ChildEventListener mChildEventListener;
    private static Object value;
    private static String userID;
    private static int flag = 0;
    private static List<Friend> friendList = new ArrayList<Friend>();
    private static ArrayList<String> tempFriendDrink = new ArrayList<String>();
    private static Callback callback;
    private static Callback innerCallback;
    //private ArrayAdapter<String> mAdapter;

    ServerDatabaseManager() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    /**
     * 입력받은 ID가 user로 등록되었는지 확인
     * 유저 등록 시 중복 검사 & 친구 등록 시 존재 여부 확인
     * 등록 : true
     * 비등록 : false
     * @param ID
     * @return
     */
    public static boolean hasID(String ID) {

        //access [ user_list ] line in firebase
        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("user_list");

        //access to value of which key = ID in user_list of firebase
        mDatabaseReference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(Object.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //there is no data has key = ID
        if(value == null) {
            return false;
        }
        //key = ID data is already in firebase
        else {
            return true;
        }

    }

    /**
     * 입력받은 userID를 user로 서버에 등록
     * @param userID
     */
    public static void saveUserID(String userID) {
        //access [ user_list ] line in firebase
        mDatabaseReference = mFirebaseDatabase.getReference("user_list");

        mDatabaseReference.child(userID).child("drink").child("amount").setValue("0");
        mDatabaseReference.child(userID).child("drink").child("timing").setValue(0);
        mDatabaseReference.child(userID).child("friend_list").setValue(null);
    }

    /**
     * 현재 기기의 userID를 저장
     * @param ID
     */
    public static void setLocalUserID(String ID) {
        userID = ID;
    }

    /**
     * 현재 기기의 userID를 반환
     * @return
     */
    public static String getLocalUserID() {
        return userID;
    }

    /**
     * userID의 친구로 friendID를 추가
     * light값이 없으므로 default값 255/255/255 로 저장됨
     * @param userID
     * @param friendID
     * @return
     */
    public static boolean addFriend(String userID, String friendID) {
        if(hasID(friendID)) {
            //access [ friend_list ] line in firebase
            mDatabaseReference = mFirebaseDatabase.getReference("user_list");
            mDatabaseReference.child(userID).child("friend_list").child(friendID).setValue("255.255.255");
            return true;
        }
        //there is no friendID
        else {
            Log.e("ADD_FRIEND_FIREBASE", "-------failed-------");
            return false;
        }
    }

    /**
     * userID의 친구로 friendID를 추가
     * light값은 R/G/B 로 저장됨
     * @param userID
     * @param friendID
     * @param R
     * @param G
     * @param B
     * @return
     */
    public static boolean addFriend(String userID, String friendID, int R, int G, int B) {
        if(hasID(friendID)) {
            //access [ friend_list ] line in firebase
            mDatabaseReference = mFirebaseDatabase.getReference("user_list");
            mDatabaseReference.child(userID).child("friend_list").child(friendID).setValue(R + "." + G + "." + B + "");
            return true;
        }
        //there is no friendID
        else {
            Log.e("ADD_FRIEND_FIREBASE", "-------friendID not user-------");
            return false;
        }
    }

    /**
     * userID의 친구 중 friendID를 삭제
     * @param userID
     * @param friendID
     */
    public static void deleteFriend(String userID, String friendID) {
        //access [ friend_list ] line in firebase
        mDatabaseReference = mFirebaseDatabase.getReference("user_list");
        mDatabaseReference.child(userID).child("friend_list").child(friendID).removeValue();

    }

    /**
     * userID의 친구 중 friendID의 light값을 R/G/B 로 변경
     * @param userID
     * @param friendID
     * @param R
     * @param G
     * @param B
     */
    public static void changeLightColor(String userID, String friendID, int R, int G, int B) {
        //deleteFriend(userID, friendID);
        addFriend(userID, friendID, R, G ,B);

    }

    /**
     * 현재 기기의 userID의 friendList를 받아옴
     * @return List<Friend> friendList
     */
    public static List<Friend> getFriendList() {
        return friendList;
    }

    /**
     * 현재 기기의 userID의 friend 전부를 서버에서 받아와 friendList에 저장
     * @param userID
     */
    public static void getFriend(String userID) {
        Log.d("SERVER_DBM", "In GetFriend Method");
        mDatabaseReference = mFirebaseDatabase.getReference("user_list");
        Log.d("SERVER_DBM", "GetReferenceSuccessful");
        mDatabaseReference.child(userID).child("friend_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("SERVER_DBM", "-------ValueEvent");
                Log.d("SERVER_DBM", "FriendListSize : " + dataSnapshot.getChildrenCount());
                collectFriends((Map<String,String>) dataSnapshot.getValue());
                for(int i=0; i<friendList.size(); i++) Log.d("FRIEND_LIST", friendList.get(i).getfID() + ":" + friendList.get(i).getfLight());
                callback.callBackMethod();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static ChildEventListener myEventListener(final String friendID, final String light) {
        ChildEventListener eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // 술을 마시는 모션
                if(dataSnapshot.getKey().equals("timing") && dataSnapshot.getValue(Long.class) == 1) {
                    Log.d("SERVER_DBM", "------- event from " + friendID + " / light " + light + " / drinking");

                    BluetoothManager.writeData(light);
                }
                // 모션 끝. 다시 원상태로 복귀.
                else if(dataSnapshot.getKey().equals("timing") && dataSnapshot.getValue(Long.class) == 0) {
                    Log.d("SERVER_DBM", "------- event from " + friendID + " / light " + light + " / stop drinking");
                }
                else {
                    Log.e("SERVER_DBM", "------ " + friendID + " [ timing ] error");
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //String message = dataSnapshot.getValue(String.class);
                //mAdapter.remove(message);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        return eventListener;
    }

    /**
     * Firebase의 database에서 얻어온 data를 형식에 맞도록 조정
     * @param friends
     */
    private static void collectFriends(Map<String,String> friends) {
        mDatabaseReference = mFirebaseDatabase.getReference("user_list");
        friendList.clear();
        if(friends != null) {
            for (Map.Entry<String, String> entry : friends.entrySet()) {
                mDatabaseReference.child(entry.getKey()).child("drink").addChildEventListener(myEventListener(entry.getKey(), entry.getValue()));
                friendList.add(new Friend(entry.getKey(), entry.getValue()));
            }
        }
    }

    public static void getFriendDrinkAmount(final String friendID) {
        mDatabaseReference.child(friendID).child("drink").child("amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("SERVER_DBM", "-------FriendDrinkValueEvent");
                tempFriendDrink.add(dataSnapshot.getValue(String.class));
                Log.d("SERVER_DBM", "-------" + friendID + " : " + tempFriendDrink + "잔");
                flag++;
                innerCallback.callBackMethod();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * 서버에서 사용자의 친구들의 음주량을 받아와 tempFriendDrink 리스트(=buffer)에 저장.
     */
    public static void getFriendListDrinkAmount() {
        for(int i=0; i<ServerDatabaseManager.getFriendList().size(); i++) {
            final int j = i;
            ServerDatabaseManager.getFriendDrinkAmount(friendList.get(i).getfID());
            Callback inCallBack = new Callback() {
                @Override
                public void callBackMethod() {
                    Log.d("ServerDatabaseManager", "---------------in callBackMethod");
                }

            };

            ServerDatabaseManager.setInnerCallBack(inCallBack);
        }
    }

    /**
     * tempFriendDrink 버퍼의 값을 실제 friendList에 복사.
     */
    public static void setFriendListDrinkAmount() {
        for(int i=0; i<ServerDatabaseManager.getFriendList().size(); i++) {
            friendList.get(i).setfDrink(tempFriendDrink.get(i));
        }
    }

    public static int getFlag() {
        return flag;
    }

    public static void setFlag(int f) {
        flag = f;
    }

    public static ArrayList<String> getTempFriendDrink() {
        return tempFriendDrink;
    }

    public static String getTempFriendDrink(int position) {
        return tempFriendDrink.get(position);
    }

    public static void clearTempFriendDrink() {
        tempFriendDrink.clear();
    }

    public static void turnOnDrinkTiming() {
        // 술을 마신다 로 변경
        mDatabaseReference = mFirebaseDatabase.getReference("user_list");
        mDatabaseReference.child(userID).child("drink").child("timing").setValue(1);
    }

    public static void turnOffDrinkTiming() {
        // 술을 안마신다 로 변경
        mDatabaseReference = mFirebaseDatabase.getReference("user_list");
        mDatabaseReference.child(userID).child("drink").child("timing").setValue(0);
    }

    /**
     * Firebase의 Realtime Database에서 값을 얻어올 때 asynchronous 하므로 callback을 이용
     * @param callBack
     */
    public static void setCallBack(Callback callBack) {
        callback = callBack;
    }

    public static void setInnerCallBack(Callback callback) { innerCallback = callback; }
}
