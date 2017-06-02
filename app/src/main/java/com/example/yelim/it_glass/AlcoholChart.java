package com.example.yelim.it_glass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlcoholChart extends AppCompatActivity {

    private int totalCount;
    private int completedCount;
    private int uncompletedCount;
    private int completePercent;

    private TextView totalText;
    private TextView completedText;
    private TextView uncompletedText;
    private TextView percentText;

    private com.example.yelim.it_glass.StatisticsCircle percentView;
    private com.example.yelim.it_glass.StatisticsCircle totalView;
    private com.example.yelim.it_glass.StatisticsCircle completedView;
    private com.example.yelim.it_glass.StatisticsCircle uncompletedView;

    private int year=2017, month=6;
    final DatabaseManager dbManager = new DatabaseManager(AlcoholChart.this, DatabaseManager.DB_NAME + ".db", null, 1);
    private ArrayList<Record> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alcohol_chart);

        init();

        completedCount = countCompleted();
        totalCount = countTotal();
        uncompletedCount = totalCount - completedCount;

        completePercent = (totalCount * 100 / 30);
        percentView.setPercent(completePercent);
        percentView.setColor("#FF323024");
        completedView.setColor("#FFFF5900");
        totalView.setPercent(100);
        totalView.setColor("#FF1C639D");
        uncompletedView.setColor("#FF178A50");

        totalText.setText(totalCount + "");
        completedText.setText(completedCount + "");
        uncompletedText.setText(uncompletedCount + "");
        percentText.setText(completePercent + "ml");

    }

    public void init() {

        totalText = (TextView) findViewById(R.id.totalText);
        completedText = (TextView) findViewById(R.id.completedText);
        uncompletedText = (TextView) findViewById(R.id.uncompletedText);
        percentText = (TextView) findViewById(R.id.percentText);

        percentView = (com.example.yelim.it_glass.StatisticsCircle) findViewById(R.id.percentView);
        percentView.setPercentFlag(true);

        totalView = (com.example.yelim.it_glass.StatisticsCircle) findViewById(R.id.totalView);
        completedView  = (com.example.yelim.it_glass.StatisticsCircle) findViewById(R.id.completedView);
        uncompletedView  = (com.example.yelim.it_glass.StatisticsCircle) findViewById(R.id.uncompletedView);

        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);

        recordList = new ArrayList<Record>();

        // yyyy년 mm월
        year = Integer.parseInt(curYearFormat.format(date));
        month = Integer.parseInt(curMonthFormat.format(date));

    }

    private int countCompleted() {

        return 1;
    }

    private int countTotal() {
        recordList = (ArrayList<Record>) dbManager.getDrinkList(year, month);
        return recordList.size();
    }

    private int countLight() {
        return 0;
    }
    private int countSoMuch() {
        return 0;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setYear(int year) {
        this.year = year;
    }
}