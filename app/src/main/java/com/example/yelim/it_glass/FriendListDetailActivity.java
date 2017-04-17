package com.example.yelim.it_glass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

/**
 * Created by Yelim on 2017-03-28.
 */

/**
 * MainActivity의 friend lsit view 에서 object 클릭 시 나타나는 detail view
 */
public class FriendListDetailActivity extends Activity {

    ColorPicker cp;
    TextView tvFriendDetailName;
    TextView friendDetailName;
    EditText friendNameAdd;
    Button btFriendAddOk;
    TextView tvFriendDetailDrink;
    TextView friendDetailDrink;
    TextView tvFriendDetailLight;
    Button btLight;
    Button btFriendSave;
    Button btFriendAddCancel;

    String type;
    String inputFriendID = new String();
    Context context;
    int selectedColorR;
    int selectedColorG;
    int selectedColorB;
    boolean check;

    private static Callback callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list_detail);

        tvFriendDetailName = (TextView) findViewById(R.id.tvFriendDetailName);
        friendDetailName = (TextView) findViewById(R.id.friendDetailName);
        friendNameAdd = (EditText) findViewById(R.id.friendNameAdd);
        btFriendAddOk = (Button) findViewById(R.id.btFriendAddOk);
        tvFriendDetailDrink = (TextView) findViewById(R.id.tvFriendDetailDrink);
        friendDetailDrink = (TextView) findViewById(R.id.friendDetailDrink);
        cp = new ColorPicker(FriendListDetailActivity.this, 255, 255, 255);
        tvFriendDetailLight = (TextView) findViewById(R.id.tvFriendDetailLight);
        btLight = (Button) findViewById(R.id.btFriendDetailLight);
        btFriendSave = (Button) findViewById(R.id.btFriendSave);
        btFriendAddCancel = (Button) findViewById(R.id.btFriendAddCancel);

        context = this;
        type = getIntent().getStringExtra("LAYOUT_TYPE");
        selectedColorR = 255;
        selectedColorG = 255;
        selectedColorB = 255;

        if(type.equals("DETAIL")) {
            friendDetailName.setText("");
            friendNameAdd.setVisibility(View.INVISIBLE);
            btFriendAddOk.setVisibility(View.INVISIBLE);
            tvFriendDetailDrink.setVisibility(View.VISIBLE);
            friendDetailDrink.setVisibility(View.VISIBLE);

            btLight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    cp.show();
                }
            });
        }
        else if(type.equals("ADD")) {
            friendDetailName.setVisibility(View.INVISIBLE);
            friendNameAdd.setVisibility(View.VISIBLE);
            btFriendAddOk.setVisibility(View.VISIBLE);
            tvFriendDetailDrink.setVisibility(View.INVISIBLE);
            friendDetailDrink.setVisibility(View.INVISIBLE);
            tvFriendDetailLight.setVisibility(View.VISIBLE);
            check = false;

            btFriendAddOk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputFriendID = friendNameAdd.getText().toString();
                    ServerDatabaseManager.hasID(friendNameAdd.getText().toString());
                    /*Callback */callback = new Callback() {
                        @Override
                        public void callBackMethod(boolean hasID) {
                            if(hasID) {
                                check = true;
                                Log.d("FRIEND_DETAIL", "---------FRIEND IS IN SERVER" + "/ ID = " + friendNameAdd.getText().toString());
                                new AlertDialog.Builder(context)
                                        .setTitle("Good!")
                                        .setMessage("You enter " + friendNameAdd.getText().toString() + " as your friend!")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                            }
                            else {
                                check = false;
                                Log.e("FRIEND_DETAIL_ERROR", "---------FRIEND NOT IN SERVER" + "/ ID = " + friendNameAdd.getText().toString());
                                new AlertDialog.Builder(context)
                                        .setTitle("Warning!")
                                        .setMessage("There is no friend in our service! Please try again.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                friendNameAdd.setText("");
                                            }
                                        })
                                        .show();
                            }
                        }

                        @Override
                        public void callBackMethod() {

                        }
                    };
                    ServerDatabaseManager.sethasIDCallback(callback);
                }
            });

            btLight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    cp.show();
                }
            });
            cp.setCallback(new ColorPickerCallback() {
                @Override
                public void onColorChosen(@ColorInt int color) {
                    // Do whatever you want
                    selectedColorR = cp.getRed();
                    selectedColorG = cp.getGreen();
                    selectedColorB = cp.getBlue();
                    Log.d("COLOR_PICKER", "color : " + selectedColorR + "/" + selectedColorG + "/" + selectedColorB);

                    cp.dismiss();
                }
            });

            btFriendSave.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inputFriendID.equals(friendNameAdd.getText().toString())) {
                        if(check) {
                            ServerDatabaseManager.addFriend(ServerDatabaseManager.getLocalUserID(),
                                    inputFriendID, selectedColorR, selectedColorG, selectedColorB);
                        }
                    }
                    else {
                        new AlertDialog.Builder(context)
                                .setTitle("Warning!")
                                .setMessage("Please check friend name existed!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                    FriendListDetailActivity.this.finish();
                }


            });
            btFriendAddCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FriendListDetailActivity.this.finish();
                }
            });
        }
        else {
            Log.e("FRIEND_DETAIL_ERROR", "----------NO TYPE");
        }

    }

    public static void setCallBack(Callback callBack) {
        callback = callBack;
    }
}
