package com.example.yelim.it_glass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Yelim on 2017-05-02.
 */

public class MainFragment extends Fragment {
    TextView tvUserName;
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
        Log.d("Fragment", "Start Main");
        return layout;
    }
}
