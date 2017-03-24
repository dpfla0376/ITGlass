package com.example.yelim.it_glass;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Yelim on 2017-03-22.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public static final String DB_NAME = "itGlass";
    private String DB_ADDRESS;
    private Context mContext;

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
        this.db.execSQL(Database.FriendTable._CREATE);
        Log.d("DATABASE", "-------created-------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getDatabasePath() {
        ContextWrapper wrapper = new ContextWrapper(mContext);
        return wrapper.getDatabasePath(DB_NAME + ".db").getAbsolutePath( );
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
            return true;
        }
        else
            return false;
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
        else if(TABLE_NAME.equals(Database.FriendTable._TABLENAME)) {
            insertToFriendTable(record);
        }
        else {
            Log.e("DB_INSERT", "--------wrong_table_name--------");
        }
    }

    /**
     * update [ TABLE_NAME table ] with record
     * @param TABLE_NAME
     * @param record
     */
    public void updateDatabase(String TABLE_NAME, String[] record) {
        if(TABLE_NAME.equals(Database.UserTable._TABLENAME)) {
            updateUserTable(record);
        }
        else if(TABLE_NAME.equals(Database.FriendTable._TABLENAME)) {
            updateFriendTable(record);
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
                + " (" + Database.UserTable.ID + ") VALUES ("
                + "'" + record[0] + "');");
        db.close();
    }

    /**
     * insert record to [ friend table ]
     * @param record
     */
    private void insertToFriendTable(String[] record) {
        db = getWritableDatabase();
        db.execSQL("INSERT INTO "
                + Database.FriendTable._TABLENAME
                + " (" + Database.FriendTable.FRIEND_ID + ") VALUES ("
                + "'" + record[0] + "');");
        db.close();
    }

    /**
     * update [ user table ]
     * @param record
     */
    private void updateUserTable(String[] record) {
        db = getWritableDatabase();
        db.execSQL("UPDATE "
                + Database.UserTable._TABLENAME
                + " SET "
                + Database.UserTable.ID
                + "='" + record[0] + "'");
        db.close();
    }

    /**
     * update [ friend table ]
     * @param record
     */
    private void updateFriendTable(String[] record) {
        db = getWritableDatabase();
        db.execSQL("UPDATE "
                + Database.FriendTable._TABLENAME
                + " SET "
                + Database.FriendTable.FRIEND_ID
                + "='" + record[0] + "'");
        db.close();
    }

}