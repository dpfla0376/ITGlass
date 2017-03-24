package com.example.yelim.it_glass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    final DatabaseManager dbManager = new DatabaseManager(MainActivity.this, DatabaseManager.DB_NAME + ".db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("DATABASE", "---------" + dbManager.getDatabasePath() + "---------");
    }
}
