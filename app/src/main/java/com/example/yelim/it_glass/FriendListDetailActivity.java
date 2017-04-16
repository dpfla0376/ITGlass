package com.example.yelim.it_glass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
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
    String type;
    Context context;
    int tempColor;
    int selectedColorR;
    int selectedColorG;
    int selectedColorB;

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
        context = this;
        type = getIntent().getStringExtra("LAYOUT_TYPE");

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

            btFriendAddOk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ServerDatabaseManager.hasID(friendNameAdd.getText().toString())) {
                        Log.d("FRIEND_DETAIL_ERROR", "---------FRIEND IS IN SERVER" + "/ ID = " + friendNameAdd.getText().toString());
                    }
                    else {
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
                    tempColor = color;
                    selectedColorR = cp.getRed();
                    selectedColorG = cp.getGreen();
                    selectedColorB = cp.getBlue();
                    Log.d("COLOR_PICKER", "color : " + selectedColorR + "/" + selectedColorG + "/" + selectedColorB);

                    cp.dismiss();
                }
            });
        }
        else {
            Log.e("FRIEND_DETAIL_ERROR", "----------NO TYPE");
        }

    }
}
