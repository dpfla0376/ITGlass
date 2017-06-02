package com.example.yelim.it_glass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private int year, month;
    private int day;
    private Calendar calendar;


    final DatabaseManager dbManager = new DatabaseManager(AlcoholChart.this, DatabaseManager.DB_NAME + ".db", null, 1);
    private ArrayList<Record> recordList;

    @BindView(R.id.statisticsPreviousMonth)
    TextView previousMonth;
    @BindView(R.id.statisticsNextMonth)
    TextView nextMonth;
    @BindView(R.id.staticYearAndMonth)
    TextView yearAndMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alcohol_chart);

        init();

        ButterKnife.bind(this);

        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        calendar = Calendar.getInstance();
        year = Integer.parseInt(curYearFormat.format(date));
        month = Integer.parseInt(curMonthFormat.format(date));
        day = calendar.get(Calendar.DAY_OF_WEEK);

        drawCircles();

        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousYear;
                int previousMonth;

                if (month == 1) {
                    previousMonth = 12;
                    previousYear = year - 1;
                } else {
                    previousMonth = month - 1;
                    previousYear = year;
                }

                year = previousYear;
                month = previousMonth;
                calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
                day = calendar.get(Calendar.DAY_OF_WEEK);

                Log.d("STATIC", "PREVIOUS"+month);

                drawCircles();
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextYear;
                int nextMonth;

                if (month == 12) {
                    nextMonth = 1;
                    nextYear = year + 1;
                } else {
                    nextMonth = month + 1;
                    nextYear = year;
                }
                year = nextYear;
                month = nextMonth;

                calendar = Calendar.getInstance();

                calendar.set(nextYear, nextMonth - 1, 1);

                Log.d("STATIC", "NEXT"+month);

                drawCircles();
            }
        });
    }

    public void init() {

        totalText = (TextView) findViewById(R.id.totalText);
        completedText = (TextView) findViewById(R.id.completedText);
        uncompletedText = (TextView) findViewById(R.id.uncompletedText);
        percentText = (TextView) findViewById(R.id.percentText);

        percentView = (com.example.yelim.it_glass.StatisticsCircle) findViewById(R.id.percentView);
        percentView.setPercentFlag(true);

        totalView = (com.example.yelim.it_glass.StatisticsCircle) findViewById(R.id.totalView);
        completedView = (com.example.yelim.it_glass.StatisticsCircle) findViewById(R.id.completedView);
        uncompletedView = (com.example.yelim.it_glass.StatisticsCircle) findViewById(R.id.uncompletedView);

        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);

        recordList = new ArrayList<Record>();

        // yyyy년 mm월
        year = Integer.parseInt(curYearFormat.format(date));
        month = Integer.parseInt(curMonthFormat.format(date));

    }

    private int countCompleted(int month) {

        return 1;
    }

    private int countTotal(int year, int month) {
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

    private void drawCircles(){
        completedCount = countCompleted(month);
        totalCount = countTotal(year, month);
        uncompletedCount = totalCount - completedCount;

        completePercent = (totalCount * 100 / day);

        percentView.setPercent(completePercent);
        percentView.setColor("#FF323024");
        completedView.setColor("#FFFF5900");
        totalView.setPercent(100);
        totalView.setColor("#FF1C639D");
        uncompletedView.setColor("#FF178A50");

        yearAndMonth.setText(year+" / "+month);
        totalText.setText(totalCount + "");     // 총 음주일수
        completedText.setText(completedCount + "");     // 과음한 날
        uncompletedText.setText(uncompletedCount + "");     // 과음 안한 날

        percentText.setText(completePercent + "ml");        // 총 음주량
    }
}