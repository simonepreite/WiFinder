package com.simonepreite.winder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class APInfo extends SQLiteOpenHelper {

    public static final String TABLE_GRADES = "grades";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SSID = "SSID";
    public static final String COLUMN_MAC_ADDRESS = "macaddress";
    public static final String COLUMN_CLASS = "class";
    public static final String COLUMN_GRADE = "grade";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "apinfo.db";

    public APInfo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
