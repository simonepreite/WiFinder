package com.simonepreite.winder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.simonepreite.winder.database.APAuxdb.APBaseColums.TABLE_NAME;


public class APInfo extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "apInfo.db";

    private static APInfo sInstance;

    private static final String SQL_CREATE_EXPENSE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    APAuxdb.APBaseColums.COLUMN_MAC_ADDRESS + " STRING PRIMARY KEY," +
                    APAuxdb.APBaseColums.COLUMN_SSID + " STRING NOT NULL," +
                    APAuxdb.APBaseColums.DB + " INT NOT NULL," +
                    APAuxdb.APBaseColums.CAPABILITIES + " STRING NOT NULL," +
                    APAuxdb.APBaseColums.LATITUDE+" DOUBLE,"+
                    APAuxdb.APBaseColums.LONGITUDE+" DOUBLE"+
                    ")" ;

    private static final String SQL_DELETE_EXPENSE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public APInfo(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized APInfo getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new APInfo(context);
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

    public long  insertScanRes(String BSSID, int level, String capabilities, String SSID, double lat, double lon){
        // in questo punto va controllato se il record esiste già (attraverso il macaddress)
        // se esiste controllare se il segnale è più forte dall'ultimo aggiornamento e in caso positivo aggiornare le cordinate
        // altrimenti lasciare il record così com'è
        // ha senso un thread che aggiorna il database?
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(APAuxdb.APBaseColums.COLUMN_MAC_ADDRESS, BSSID);
        values.put(APAuxdb.APBaseColums.DB, level);
        values.put(APAuxdb.APBaseColums.CAPABILITIES, capabilities);
        values.put(APAuxdb.APBaseColums.COLUMN_SSID, SSID);
        values.put(APAuxdb.APBaseColums.LATITUDE, lat);
        values.put(APAuxdb.APBaseColums.LONGITUDE, lon);
        Long newRowId = db.insert(APAuxdb.APBaseColums.TABLE_NAME, null, values);
        return newRowId;
    }

    public Cursor getAP() {
        return this.getWritableDatabase().query(APAuxdb.APBaseColums.TABLE_NAME, null, null, null, null, null, null);
    }

}
