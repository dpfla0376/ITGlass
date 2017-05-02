package com.example.yelim.it_glass;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.BindView;

public class LogoActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MAIN = 1001;
    boolean firstCome;
    final DatabaseManager dbManager = new DatabaseManager(LogoActivity.this, DatabaseManager.DB_NAME + ".db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        //Database checked (created or not)
        if(dbManager.isEmpty(Database.UserTable._TABLENAME)) {
            firstCome = true;
            Log.d("FIRST_COME", "-----true-----");
        }
        else {
            firstCome = false;
            Log.d("FIRST_COME", "-----false-----");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(firstCome) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);

                    startActivity(intent);
                    finish();
                }

                else {
                    setting();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
    
    private void setting() {
        ServerDatabaseManager.setLocalUserID(dbManager.getLocalUserName());
        ServerDatabaseManager.resetServerDB();

    }
}
