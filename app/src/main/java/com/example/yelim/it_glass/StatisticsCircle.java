package com.example.yelim.it_glass;

/**
 * Created by sehyeon on 2017-06-02.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by sehyeon on 2015-11-16.
 */
public class StatisticsCircle extends View {

    private Paint circlePaint;
    private Paint progressPaint;

    private RectF backgroundCircle;
    private RectF progressArc;

    private int percent;

    private float centerX;
    private float centerY;
    private float radius;

    private int iCurStep;

    private boolean isPercent=false;


    public StatisticsCircle(Context context) {
        super(context);
        init();
    }

    public StatisticsCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatisticsCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isPercent == true) {
            drawBackgroundCircle(canvas);
        }
        else {
            drawBackgroundMIniCircle(canvas);
        }
        invalidate();
    }

    private void init() {
        circlePaint = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setColor(Color.parseColor("#FFE5E2CF"));
                setStrokeWidth(30);
                setAntiAlias(true);
            }
        };
        progressPaint = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setColor(Color.parseColor("#FF165BCA"));
                setStrokeWidth(30);
                setAntiAlias(true);
            }
        };
        backgroundCircle = new RectF();
        progressArc = new RectF();
        iCurStep = 0;

    }

    public void drawBackgroundCircle(Canvas canvas) {

        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;

        radius = (canvas.getWidth() - 200) / 2;
        backgroundCircle.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        progressArc.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        canvas.drawArc(backgroundCircle, 270, 360, false, circlePaint);

        progressArc.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        if (iCurStep <= percent) {
            canvas.drawArc(progressArc, 270, -(360 * (iCurStep / 100f)), false, progressPaint);
            iCurStep++;
        } else {
            canvas.drawArc(progressArc, 270, -(360 * (percent / 100f)), false, progressPaint);
        }
    }
    public void drawBackgroundMIniCircle(Canvas canvas) {

        progressPaint.setStrokeWidth(10);
        circlePaint.setStrokeWidth(10);

        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;

        radius = (canvas.getWidth() - 80) / 2;

        backgroundCircle.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        progressArc.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        canvas.drawArc(backgroundCircle, 270, 360, false, circlePaint);

        progressArc.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        if (iCurStep <= 100) {
            canvas.drawArc(progressArc, 270, -(360 * (iCurStep / 100f)), false, progressPaint);
            iCurStep++;
        } else {
            canvas.drawArc(progressArc, 270, -(360 * (100 / 100f)), false, progressPaint);
        }
    }
    public void drawMiniCircle(Canvas canvas) {

        progressPaint.setStrokeWidth(10);
        circlePaint.setStrokeWidth(10);

        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;

        radius = (canvas.getWidth() - 80) / 2;

        progressArc.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        if (iCurStep <= 100) {
            canvas.drawArc(progressArc, 270, -(360 * (iCurStep*2 / 100f)), false, progressPaint);
            iCurStep++;
        } else {
            canvas.drawArc(progressArc, 270, -(360 * (100 / 100f)), false, progressPaint);
        }
    }
    public void setColor(String color){
        progressPaint.setColor(Color.parseColor(color));
    }
    public void setPercentFlag(boolean isPercent){
        this.isPercent = isPercent;
    }

    public void setPercent(int percent){
        this.percent=percent;
    }
    private void drawFixedArc(Canvas canvas) {

        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;

        radius = (canvas.getWidth() - 200) / 2;
        backgroundCircle.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        progressArc.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);


        canvas.drawArc(backgroundCircle, 270, 360, false, circlePaint);

        if (iCurStep <= percent) {
            canvas.drawArc(progressArc, 270, -(360 * (iCurStep / 100f)), false, progressPaint);
            iCurStep++;
            //   Log.d("ProgressCircle", "Percent : " + iCurStep);
        } else {
            canvas.drawArc(progressArc, 270, -(360 * (percent / 100f)), false, progressPaint);
        }
    }

}
