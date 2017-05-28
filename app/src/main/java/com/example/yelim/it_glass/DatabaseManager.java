package com.example.yelim.it_glass;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yelim on 2017-03-22.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public static final String DB_NAME = "itGlass";
    public static boolean isDrinkOn;
    private String DB_ADDRESS;
    private static Context mContext;

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        this.db = db;
        this.db.execSQL(Database.UserTable._CREATE);
        this.db.execSQL(Database.DrinkRecordTable._CREATE);
        Log.d("DATABASE", "-------created-------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String getDatabasePath() {
        ContextWrapper wrapper = new ContextWrapper(mContext);
        return wrapper.getDatabasePath(DB_NAME + ".db").getAbsolutePath();
    }

    /**
     * check if the table is empty
     * @param table
     * @return
     */
    public boolean isEmpty(String table) {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table, null);
        if(c.getCount() == 0) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }

    /**
     * check if the table is empty where attribute = attrValue
     * @param table
     * @param attribute
     * @param attrValue
     * @return
     */
    public boolean isEmpty(String table, String attribute, String attrValue) {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table + " WHERE " + attribute + "='" + attrValue + "'", null);
        if(c.getCount() == 0) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }

    /**
     * get local user name from local DataBase
     * @return local user name
     */
    public String getLocalUserName() {
        String name = new String();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + Database.UserTable.ID + " FROM USER", null);
        if(c.getCount() != 0) {
            c.moveToNext();
            name = c.getString(0);
            Log.d("LogoActivity", "-------- Local User Name : " + name);
        }
        else {
            Log.e("DATABASE", "------- getLocalUserName() ERROR");
        }

        db.close();
        return name;
    }

    public void getSetting() {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + Database.UserTable.DRINK_ON_OFF + " FROM USER", null);
        if(c.getCount() != 0) {
            c.moveToNext();
            if(c.getString(0).equals("on")) {
                isDrinkOn = true;
            }
            else if(c.getString(0).equals("off")) {
                isDrinkOn = false;
            }
            else {
                Log.e("DATABASE", "------- getSetting() ERROR");
            }
        }
        else {
            Log.e("DATABASE", "------- getSetting() ERROR");
        }
        db.close();
    }

    /**
     * insert record into [ TABLE_NAME table ]
     * @param TABLE_NAME
     * @param record
     */
    public void insertToDatabase(String TABLE_NAME, String[] record) {
        if(TABLE_NAME.equals(Database.UserTable._TABLENAME)) {
            insertToUserTable(record);
        }
        else if(TABLE_NAME.equals(Database.DrinkRecordTable._TABLENAME)) {
            insertToDrinkRecordTable(record);
        }
        else {
            Log.e("DB_INSERT", "--------wrong_table_name--------");
        }
    }

    /**
     * update [ TABLE_NAME table ] with record
     * @param TABLE_NAME
     * @param attribute
     * @param value
     * @param conAttr
     * @param conAttrValue
     */
    public void updateDatabase(String TABLE_NAME, String attribute, String value, String conAttr, String conAttrValue) {
        if(TABLE_NAME.equals(Database.UserTable._TABLENAME)) {
            updateUserTable(attribute, value, conAttr, conAttrValue);
        }
        else if(TABLE_NAME.equals(Database.DrinkRecordTable._TABLENAME)) {
            updateDrinkRecordTable(attribute, value, conAttr, conAttrValue);
        }
        else {
            Log.e("DB_UPDATE", "--------wrong_table_name--------");
        }
    }

    /**
     * insert record to [ user table ]
     * @param record
     */
    private void insertToUserTable(String[] record) {
        db = getWritableDatabase();
        db.execSQL("INSERT INTO "
                + Database.UserTable._TABLENAME
                + " (" + Database.UserTable.ID
                + ", " + Database.UserTable.DRINK_ON_OFF + ") VALUES ("
                + "'" + record[0]
                + "', '" + record[1] + "');");
        db.close();
    }

    /**
     * insert record to [ friend table ]
     * @param record
     */
    private void insertToDrinkRecordTable(String[] record) {
        db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + Database.DrinkRecordTable.DRINK + " FROM " + Database.DrinkRecordTable._TABLENAME + " WHERE " + Database.DrinkRecordTable.DATE + "='" + record[0] + "'", null);
        if(c.getCount() == 0) {
            db.execSQL("INSERT INTO "
                    + Database.DrinkRecordTable._TABLENAME
                    + " (" + Database.DrinkRecordTable.DATE
                    + ", " + Database.DrinkRecordTable.DRINK + ") VALUES ("
                    + "'" + record[0]
                    + "', '" + record[1] + "');");
            ServerDatabaseManager.setLocalUserDrink(Integer.parseInt(record[1]));
            ServerDatabaseManager.setServerDrinkAmount(record[1]);
        }
        else {
            c.moveToNext();
            int drink = Integer.parseInt(c.getString(0)) + Integer.parseInt(record[1]);
            updateDrinkRecordTable(Database.DrinkRecordTable.DRINK, drink+"", Database.DrinkRecordTable.DATE, record[0]);
            ServerDatabaseManager.setLocalUserDrink(drink);
            ServerDatabaseManager.setServerDrinkAmount(drink+"");
        }
        db.close();
    }

    /**
     * update [ user table ]
     * @param attribute
     * @param value
     * @param conAttr
     * @param conAttrValue
     */
    private void updateUserTable(String attribute, String value, String conAttr, String conAttrValue) {
        db = getWritableDatabase();
        db.execSQL("UPDATE "
                + Database.UserTable._TABLENAME
                + " SET "
                + attribute
                + "='" + value + "' WHERE " + conAttr + "='" + conAttrValue + "'");
        db.close();
    }

    /**
     * update [ friend table ]
     * @param attribute
     * @param value
     * @param conAttr
     * @param conAttrValue
     */
    private void updateDrinkRecordTable(String attribute, String value, String conAttr, String conAttrValue) {
        db = getWritableDatabase();
        db.execSQL("UPDATE "
                + Database.DrinkRecordTable._TABLENAME
                + " SET "
                + attribute
                + "='" + value + "' WHERE " + conAttr + "='" + conAttrValue + "'");
        db.close();
    }

    public List getDrinkList(int conYear, int conMonth) {
        List list = new ArrayList<Record>();
        String data;
        if(conMonth<10) data = conYear + "/0" + conMonth;
        else data = conYear + "/" + conMonth;

        db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Database.DrinkRecordTable._TABLENAME + " WHERE " + Database.DrinkRecordTable.DATE + " LIKE '" + data + "%'", null);
        if(c.getCount() != 0) {
            for(int i=0; i<c.getCount(); i++) {
                c.moveToNext();
                String[] temp = Record.parsingDate(c.getString(0));
                Record r = new Record(temp[0], temp[1], temp[2], c.getString(1));
                list.add(r);
            }
        }
        db.close();

        return list;
    }

}