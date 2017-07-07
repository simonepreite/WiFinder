package com.simonepreite.winder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import static com.simonepreite.winder.database.APAuxdb.APBaseColums.TABLE_NAME;


public class APInfo extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
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

    public ArrayList<HashMap<String,String>> getAllEntries(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c= db.rawQuery("select * from "+TABLE_NAME, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            HashMap<String,String> temp = new HashMap<>();
            temp.put("BSSID", c.getString(0));
            temp.put("SSID", c.getString(1));
            temp.put("level", c.getString(2));
            temp.put("CAPABILITIES", c.getString(3));
            temp.put("LATITUDE", c.getString(4));
            temp.put("LONGITUDE", c.getString(5));
            list.add(temp);
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public long  insertScanRes(String BSSID, int level, String capabilities, String SSID, double lat, double lon){
        // in questo punto va controllato se il record esiste già (attraverso il macaddress)
        // se esiste controllare se il segnale è più forte dall'ultimo aggiornamento e in caso positivo aggiornare le cordinate
        // altrimenti lasciare il record così com'è
        // ha senso un thread che aggiorna il database?
        Long newRowId = Long.valueOf(-1);
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args={BSSID};

        Cursor c = db.rawQuery("select "+ APAuxdb.APBaseColums.DB +" from "+TABLE_NAME+" where "+APAuxdb.APBaseColums.COLUMN_MAC_ADDRESS+"=?", args);
        String signal = "";
        if(c!=null){
            c.moveToFirst();
            signal = c.getString(0);
        }
        if(signal!="" && Integer.parseInt(signal) > level){
            ContentValues values = new ContentValues();
            values.put(APAuxdb.APBaseColums.LATITUDE, lat); //These Fields should be your String values of actual column names
            values.put(APAuxdb.APBaseColums.LONGITUDE, lon);
            values.put(APAuxdb.APBaseColums.DB, level);
            newRowId = Long.valueOf(db.update(TABLE_NAME, values, APAuxdb.APBaseColums.COLUMN_MAC_ADDRESS+"=?", args));
        }else{
            ContentValues values = new ContentValues();
            values.put(APAuxdb.APBaseColums.COLUMN_MAC_ADDRESS, BSSID);
            values.put(APAuxdb.APBaseColums.DB, level);
            values.put(APAuxdb.APBaseColums.CAPABILITIES, capabilities);
            values.put(APAuxdb.APBaseColums.COLUMN_SSID, SSID);
            values.put(APAuxdb.APBaseColums.LATITUDE, lat);
            values.put(APAuxdb.APBaseColums.LONGITUDE, lon);
            newRowId = db.insert(APAuxdb.APBaseColums.TABLE_NAME, null, values);
        }
        return newRowId;
    }
}
