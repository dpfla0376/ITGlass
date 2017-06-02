package com.example.yelim.it_glass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Yelim on 2017-05-02.
 */

public class MainFragment extends Fragment implements TextViewCallBack {
    TextView tvUserName;
    ImageView imgGlass;
    TextView tvUserDrink;
    TextView tvUserAvgDrink;
    TextView tvAlcholDetox;
    public MainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);
        tvUserName = (TextView) layout.findViewById(R.id.tvUserName);
        tvUserName.setText(ServerDatabaseManager.getLocalUserID() + " 님");
        imgGlass = (ImageView) layout.findViewById(R.id.imgGlass);
        imgGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        tvUserDrink = (TextView) layout.findViewById(R.id.tvUserDrink);
        tvUserDrink.setText(ServerDatabaseManager.getLocalUserDrink() + " ml");
        tvAlcholDetox = (TextView) layout.findViewById(R.id.tvAlcholDetox);
        tvAlcholDetox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvUserAvgDrink = (TextView) layout.findViewById(R.id.tvUserAvgDrink);
        tvUserAvgDrink.setText(DatabaseManager.avgDrink+"");
        Log.d("Fragment", "Start Main");
        return layout;
    }

    @Override
    public void updateTextView(String tag, String data) {
        if(tag.equals("agv_drink")) tvUserAvgDrink.setText(DatabaseManager.avgDrink);
        else if(tag.equals("realtime_drink")) tvUserDrink.setText(ServerDatabaseManager.getLocalUserDrink() + " ml");
        else Log.e("updateTextView", "invalid tag");
    }
}
