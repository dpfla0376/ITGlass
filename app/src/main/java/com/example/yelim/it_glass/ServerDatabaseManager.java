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
    private static Long value;
    private static String userID;
    private static GenericTypeIndicator<List<Friend>> t = new GenericTypeIndicator<List<Friend>>() {};
    private static List<Friend> friendList = new ArrayList<Friend>();
    private static Callback callback;
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
                value = dataSnapshot.getValue(Long.class);
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

        mDatabaseReference.child(userID).setValue(0);
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
            mDatabaseReference = mFirebaseDatabase.getReference("friend_list");
            mDatabaseReference.child(userID).child(friendID).setValue("255/255/255");
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
            mDatabaseReference = mFirebaseDatabase.getReference("friend_list");
            mDatabaseReference.child(userID).child(friendID).setValue(R + "/" + G + "/" + B + "");
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
        mDatabaseReference = mFirebaseDatabase.getReference("friend_list");
        mDatabaseReference.child(userID).child(friendID).removeValue();

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
     * @return
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
        mDatabaseReference = mFirebaseDatabase.getReference("friend_list");
        Log.d("SERVER_DBM", "GetReferenceSuccessful");
        mDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("SERVER_DBM", "-------SingleValueEvent");
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

    /**
     * Firebase의 database에서 얻어온 data를 형식에 맞도록 조정
     * @param friends
     */
    private static void collectFriends(Map<String,String> friends) {
        for (Map.Entry<String, String> entry : friends.entrySet()){
            friendList.add(new Friend(entry.getKey(), entry.getValue()));
        }
    }

    public static void initServerDatabase() {
        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("message");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String message = dataSnapshot.getValue(String.class);
                //mAdapter.add(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String message = dataSnapshot.getValue(String.class);
                //mAdapter.remove(message);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    /**
     * Firebase의 Realtime Database에서 값을 얻어올 때 asynchronous 하므로 callback을 이용
     * @param callBack
     */
    public static void setCallBack(Callback callBack) {
        callback = callBack;
    }
}
