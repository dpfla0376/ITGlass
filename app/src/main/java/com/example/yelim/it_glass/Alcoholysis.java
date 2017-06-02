package com.example.yelim.it_glass;

import android.content.Context;

/**
 * Created by sehyeon on 2017-05-02.
 */

public class Alcoholysis {
    int vol;
    int alcoholPercent;
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
        this.alcoholPercent = alcoholPercent;
        alcoholConcentration = alcoholPercent * 0.1;
        alcoholRatio = 0.7894;
        absorptionRatio = 0.7;
        sexFactor = 0.55;
        weight = getWeight();
        sex = getSex();
        dbManager = new DatabaseManager(context, DatabaseManager.DB_NAME + ".db", null, 1);
        /*
        if (sex.equals("XX")) {
            sexFactor = 0.55;
            weight = 55;
        } else if (sex.equals("XY")) {
            sexFactor = 0.69;
            weight = 75;
        } else {
            sexFactor = 0.69;
            weight = 75;
        }
        */
        decrement = 0.019;
    }

    /**
     * @return time minutes required to alcoholysis
     */
    int getTime(int vol, int weight, String sex) {
        // 술의 양 * 알코올농도 * 알코올비중 * 체내흡수율 / 몸무게 * 성별계수 = 혈중알콜
        // 혈중알콜 / 감소량 *60 = 시간

        if (sex.equals("woman")) {
            sexFactor = 0.55;
            weight = 55;
        } else if (sex.equals("man")) {
            sexFactor = 0.69;
            weight = 75;
        }

        int minutes = (int) ((vol * alcoholPercent * alcoholConcentration * alcoholRatio * absorptionRatio * 60) / (sexFactor * weight * decrement));
        return minutes;
    }

    int getWeight() {
        int weight =0 ;

        return weight;
    }

    String getSex() {
        String sex = "";
        return sex;
    }

}
