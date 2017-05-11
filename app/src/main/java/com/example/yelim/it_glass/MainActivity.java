package com.example.yelim.it_glass;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_MAIN = 2000;

    final DatabaseManager dbManager = new DatabaseManager(MainActivity.this, DatabaseManager.DB_NAME + ".db", null, 1);
    ViewPager vp;
    LinearLayout ll;
    TextView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("DATABASE", "---------" + dbManager.getDatabasePath() + "---------");

        vp = (ViewPager) findViewById(R.id.vp);
        ll = (LinearLayout) findViewById(R.id.ll);
        setting = (TextView) findViewById(R.id.setting);
        TextView tab_first = (TextView) findViewById(R.id.tab_first);
        TextView tab_second = (TextView) findViewById(R.id.tab_second);

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        tab_first.setOnClickListener(movePageListener);
        tab_first.setTag(0);
        tab_second.setOnClickListener(movePageListener);
        tab_second.setTag(1);

        tab_first.setSelected(true);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int i = 0;
                while (i < 2) {
                    if (position == i) {
                        ll.findViewWithTag(i).setSelected(true);
                    } else {
                        ll.findViewWithTag(i).setSelected(false);
                    }
                    i++;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        setBtManager();

    }

    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            Log.d("TAG", "---------- " + tag);

            int i = 0;
            while (i < 2) {
                if (tag == i) {
                    ll.findViewWithTag(i).setSelected(true);
                } else {
                    ll.findViewWithTag(i).setSelected(false);
                }
                i++;
            }
            vp.setCurrentItem(tag);
        }
    };

    private void setBtManager() {
        //btManager=new BluetoothManager(this);

        BluetoothManager.setContext(this);
        BluetoothManager.checkBluetooth();

        BluetoothManager.CallBack callBack = new BluetoothManager.CallBack() {
            @Override
            public void callBackMethod(int flag, String fromDeviceMessage) {
                Log.d("Bluetooth", "I'm MainActivity. I got your message : " + fromDeviceMessage.toString());

                switch (flag) {
                    case 100:
                        Log.d("Bluetooth", "DEVICE IS CONNECTED");
                        break;
                    case 200:
                        // 술을 마신다
                        if (fromDeviceMessage.toString().equals("drink")) {
                            ServerDatabaseManager.turnOnDrinkTiming();
                            Log.d("Bluetooth", "NEW MESSAGE FROM YOUR DEVICE : " + fromDeviceMessage.toString());
                        }

                        // 다 마셨다
                        if (fromDeviceMessage.toString().equals("drank")) {
                            ServerDatabaseManager.turnOffDrinkTiming();
                            Log.d("Bluetooth", "NEW MESSAGE FROM YOUR DEVICE : " + fromDeviceMessage.toString());
                        }

                        break;
                }
            }
        };
        BluetoothManager.setCallBack(callBack);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new FriendlistFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
