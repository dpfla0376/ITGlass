package com.example.yelim.it_glass;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

/**
 * Created by Yelim on 2017-03-28.
 */

/**
 * MainActivity의 friend lsit view 에서 object 클릭 시 나타나는 detail view
 */
public class FriendListDetailActivity extends Activity {

    ColorPicker cp;
    Button btLight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cp = new ColorPicker(FriendListDetailActivity.this, 255, 255, 255);
        btLight = (Button) findViewById(R.id.btFriendDetailLight);
        btLight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cp.show();
            }
        });
    }
}
