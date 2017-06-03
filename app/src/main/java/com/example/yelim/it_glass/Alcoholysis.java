package com.example.yelim.it_glass;

import android.content.Context;
import android.util.Log;

/**
 * Created by sehyeon on 2017-05-02.
 */

public class Alcoholysis {
    int vol;
    double alcoholPercent;
    double alcoholConcentration;
    double alcoholRatio;
    double absorptionRatio;
    double sexFactor;
    int weight;
    double decrement;
    String sex;
    private DatabaseManager dbManager = null;

    /**
     * (ml, %, man || woman || none)
     *
     * @param alcoholPercent
     */
    Alcoholysis(int alcoholPercent, Context context) {
        this.alcoholPercent = (float) alcoholPercent * 0.01;
        alcoholConcentration = alcoholPercent * 0.1;
        alcoholRatio = 0.7894;
        dbManager = new DatabaseManager(context, DatabaseManager.DB_NAME + ".db", null, 1);
        weight = getWeight();
        sex = getSex();

        if (sex.equals("woman")) {
            sexFactor = 0.55;
            absorptionRatio = 0.6;
        } else if (sex.equals("man")) {
            sexFactor = 0.69;
            absorptionRatio = 0.7;
        }
        else {
            sexFactor = 0.62;
            absorptionRatio = 0.65;
        }
        decrement = 0.019;
    }

    /**
     * @return time minutes required to alcoholysis
     */
    int getTime(int vol) {
        // 술의 양 * 알코올농도 * 알코올비중 * 체내흡수율 / 몸무게 * 성별계수 = 혈중알콜
        // 혈중알콜 / 감소량 *60 = 시간

        int minutes = (int) ((vol * alcoholConcentration * alcoholRatio * absorptionRatio * 60) / (sexFactor * weight * decrement));
        Log.d("VOL", vol+"");
        Log.d("Percent", alcoholPercent+"");

        return minutes;
    }

    int getWeight() {
        return dbManager.getLocalUserWeight();
    }

    String getSex() {
        return dbManager.getLocalUserSex();
    }

    public void setPercent(int percent){
        this.alcoholPercent = percent;
    }
}
