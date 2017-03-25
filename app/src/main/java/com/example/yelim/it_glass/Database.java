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
        //settings 기능 추가

        public static final String _TABLENAME = "USER";

        public static final String _CREATE = "CREATE TABLE "
                + _TABLENAME + "("
                + ID + " TEXT PRIMARY KEY);";
       }

    /**
     * create [ friend table ] in database
     */
    public static final class FriendTable implements BaseColumns {

        public static final String FRIEND_ID = "id";
        public static final String R = "r";
        public static final String G = "g";
        public static final String B = "b";

        public static final String _TABLENAME = "FRIEND";

        public static final String _CREATE = "CREATE TABLE "
                + _TABLENAME + "("
                + FRIEND_ID + " TEXT PRIMARY KEY, "
                + R + " INT, "
                + G + " INT, "
                + B + " INT);";
    }

}
