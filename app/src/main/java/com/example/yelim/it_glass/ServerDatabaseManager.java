package com.example.yelim.it_glass;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Yelim on 2017-03-25.
 */

public class ServerDatabaseManager {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private Long value;
    //private ArrayAdapter<String> mAdapter;

    ServerDatabaseManager() {

    }

    public boolean hasID(String ID) {
        //access [ user_list ] line in firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("user_list");

        //access to value of which key = ID in user_list of firebase
        mDatabaseReference.child(ID).addValueEventListener(new ValueEventListener() {
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

    public void saveUserID(String userID) {
        //access [ user_list ] line in firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("user_list");

        mDatabaseReference.child(userID).setValue(0);
    }

    public boolean addFriend(String userID, String friendID) {
        if(hasID(friendID)) {
            //access [ friend_list ] line in firebase
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference("friend_list");

            //친구를 중복으로 넣을 때 예외처리를 해줘야하나..? 근데 JSON 형식에서 중복이면 그냥 저장 안되는거같던데..
            mDatabaseReference.child(userID).child(friendID).setValue("255/255/255");
            return true;
        }
        //there is no friendID
        else {
            Log.e("ADD_FRIEND_FIREBASE", "-------failed-------");
            return false;
        }
    }

    public boolean addFriend(String userID, String friendID, int R, int G, int B) {
        if(hasID(friendID)) {
            //access [ friend_list ] line in firebase
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference("friend_list");

            //친구를 중복으로 넣을 때 예외처리를 해줘야하나..? 근데 JSON 형식에서 중복이면 그냥 저장 안되는거같던데..
            mDatabaseReference.child(userID).child(friendID).setValue(R + "/" + G + "/" + B + "");
            return true;
        }
        //there is no friendID
        else {
            Log.e("ADD_FRIEND_FIREBASE", "-------failed-------");
            return false;
        }
    }

    public void deleteFriend(String userID, String friendID) {
        //access [ friend_list ] line in firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("friend_list");

        mDatabaseReference.child(userID).child(friendID).removeValue();

    }

    public void changeLightColor(String userID, String friendID, int R, int G, int B) {
        deleteFriend(userID, friendID);
        addFriend(userID, friendID, R, G ,B);

    }

    public void initServerDatabase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
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

}
