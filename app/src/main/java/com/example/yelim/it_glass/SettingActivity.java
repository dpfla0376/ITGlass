package com.example.yelim.it_glass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class SettingActivity extends AppCompatActivity {
    TextView tvWithdrawl;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;

        tvWithdrawl = (TextView) findViewById(R.id.tvWithdrawl);
        tvWithdrawl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Warning!")
                        .setMessage("Are you sure to delete all your data? It is irrevocable!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAllData();
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Good bye!")
                                        .setMessage("Your data are deleted! Good bye!")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //restart the app
                                                /*Intent mStartActivity = new Intent(mContext, LogoActivity.class);
                                                int mPendingIntentId = 0000;
                                                PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                                AlarmManager amr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                                amr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);*/

                                                Intent resultIntent = new Intent();
                                                resultIntent.putExtra("app_restart", "true");
                                                setResult(RESULT_OK, resultIntent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    private void deleteAllData() {
        deleteServerData();
        Log.d("SETTING_DELETE", "------server data deleted");
        deleteLocalData();
        Log.d("SETTING_DELETE", "------local data deleted");
    }

    private void deleteServerData() {
        ServerDatabaseManager.deleteServerData();
    }

    private void deleteLocalData() {
        String strDBFilePath = DatabaseManager.getDatabasePath();
        File file = new File(strDBFilePath);
        file.delete();
    }
}
