package com.example.yelim.it_glass;

import android.content.Context;
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
    TextView tvAlcholChart;
    Context mContext;
    private Alcoholysis alcoholysis;
    public MainFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        tvUserAvgDrink.setText("평균 " + DatabaseManager.avgDrink + " ml");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);
        mContext = getActivity();
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
        tvAlcholChart = (TextView) layout.findViewById(R.id.alcoholChart);
        tvAlcholChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlcoholChart.class);
                startActivity(intent);
            }
        });
        tvUserAvgDrink = (TextView) layout.findViewById(R.id.tvUserAvgDrink);
        tvUserAvgDrink.setText("평균 " + DatabaseManager.avgDrink + " ml");
        Log.d("Fragment", "Start Main");

        tvAlcholDetox = (TextView)layout.findViewById(R.id.tvAlcholDetox);
        alcoholysis = new Alcoholysis(MainActivity.alcoholPercent, getContext());
        tvAlcholDetox.setText("해독까지 " + alcoholysis.getTime(ServerDatabaseManager.getLocalUserDrink())+"분");
        return layout;
    }

    @Override
    public void updateTextView(String tag, String data) {
        if(tag.equals("avg_drink")) tvUserAvgDrink.setText("평균 " + DatabaseManager.avgDrink + " ml");
        else if(tag.equals("realtime_drink")) tvUserDrink.setText(ServerDatabaseManager.getLocalUserDrink() + " ml");
        else Log.e("updateTextView", "invalid tag");
        tvAlcholDetox.setText("해독까지 " + alcoholysis.getTime(ServerDatabaseManager.getLocalUserDrink())+"분");
    }
}
