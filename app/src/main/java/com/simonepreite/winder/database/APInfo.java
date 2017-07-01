package com.simonepreite.winder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class APInfo extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "apInfo.db";

    private static APInfo sInstance;

    private static final String SQL_CREATE_EXPENSE_TABLE =
            "CREATE TABLE " + APAuxdb.APBaseColums.TABLE_NAME + " (" +
                    APAuxdb.APBaseColums.COLUMN_MAC_ADDRESS + " TEXT NOT NULL," +
                    APAuxdb.APBaseColums.COLUMN_SSID + " TEXT NOT NULL," +
                    APAuxdb.APBaseColums.DB + " TEXT NOT NULL," +
                    APAuxdb.APBaseColums.CAPABILITIES + " TEXT NOT NULL," +
                    APAuxdb.APBaseColums.POSITION + " TEXT NOT NULL)" ;

    private static final String SQL_DELETE_EXPENSE_TABLE =
            "DROP TABLE IF EXISTS " + "APLIST";

    public APInfo(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized APInfo getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new APInfo(context.getApplicationContext());
        }
        return sInstance;
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
