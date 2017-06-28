package com.simonepreite.winder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class APInfo extends SQLiteOpenHelper {

    public static final String COLUMN_SSID = "SSID";
    public static final String COLUMN_MAC_ADDRESS = "BSSID";
    public static final String DB = "level";
    public static final String CAPABILITIES = "security";
    public static final String POSITION = "security";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "apInfo.db";

    private static final String SQL_CREATE_EXPENSE_TABLE =
            "CREATE TABLE " + "APLIST" + " (" +
                    COLUMN_MAC_ADDRESS + " TEXT NOT NULL," +
                    COLUMN_SSID + " TEXT NOT NULL," +
                    DB + " TEXT NOT NULL," +
                    CAPABILITIES + " TEXT NOT NULL," +
                    POSITION + " TEXT NOT NULL)" ;

    private static final String SQL_DELETE_EXPENSE_TABLE =
            "DROP TABLE IF EXISTS " + "APLIST";

    public APInfo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_EXPENSE_TABLE);
        onCreate(db);
    }
}
