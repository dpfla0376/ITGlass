package com.example.yelim.it_glass;

import android.provider.BaseColumns;

/**
 * Created by Yelim on 2017-03-22.
 */

/**
 * Table, View 마다 중첩 클래스를 만든다.
 * table명, column명 정보는 상수로 저장한다.
 */
public final class Database {

    /**
     * create [ user table ] in database
     */
    public static final class UserTable  {

        public static final String ID = "id";
        public static final String DRINK_ON_OFF = "drink_on_off";
        public static final String SEX = "sex";
        public static final String AGE = "age";
        public static final String WEIGHT = "weight";
        public static final String AVG_DRINK = "avg_drink";

        public static final String _TABLENAME = "USER";

        public static final String _CREATE = "CREATE TABLE "
                + _TABLENAME + "("
                + ID + " TEXT PRIMARY KEY, "
                + DRINK_ON_OFF + " TEXT, "
                + SEX + " TEXT, "
                + AGE + " TEXT, "
                + WEIGHT + " TEXT, "
                + AVG_DRINK + " TEXT);";
       }

    /**
     * create [ drink_record table ] in database
     */
    public static final class DrinkRecordTable implements BaseColumns {

        public static final String DATE = "date";
        public static final String DRINK = "drink";

        public static final String _TABLENAME = "drink_record";

        public static final String _CREATE = "CREATE TABLE "
                + _TABLENAME + "("
                + DATE + " TEXT PRIMARY KEY, "
                + DRINK + " TEXT);";
    }

}
