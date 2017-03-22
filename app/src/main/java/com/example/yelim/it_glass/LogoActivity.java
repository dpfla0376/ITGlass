package com.example.yelim.it_glass;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LogoActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MAIN = 1001;
    public static SQLiteDatabase db;
    boolean databaseCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        //Database checked (created or not)
        try {
            db = openOrCreateDatabase("itGlass", MODE_WORLD_READABLE, null);
            databaseCreated = true;
            Log.d("database", "-----created-----");

        }
        catch (Exception e) {
            databaseCreated = false;
            e.printStackTrace();
            Log.e("database", "-----not created-----");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(databaseCreated) {
                    createDatabase();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                else {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
