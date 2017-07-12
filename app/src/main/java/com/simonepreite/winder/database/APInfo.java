package com.simonepreite.winder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import static com.simonepreite.winder.database.APAuxdb.APBaseColums.*;
import static com.simonepreite.winder.database.APQuery.*;

public class APInfo extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "apInfo.db";

    private static APInfo sInstance;

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

        db.execSQL(SQL_CREATE_AP_TABLE);
        db.execSQL(SQL_CREATE_MISURATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_AP_TABLE);
        db.execSQL(SQL_DELETE_MEASURATION_TABLE);
        onCreate(db);
    }

    public ArrayList<HashMap<String,String>> getAllEntries(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c= db.rawQuery(GET_ALL_ENTRIES, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            HashMap<String,String> temp = new HashMap<>();
            temp.put("BSSID", c.getString(c.getColumnIndex(COLUMN_MAC_ADDRESS)));
            temp.put("SSID", c.getString(c.getColumnIndex(COLUMN_SSID)));
            temp.put("CAPABILITIES", c.getString(c.getColumnIndex(CAPABILITIES)));
            Cursor c2 = getAPposition(c.getString(c.getColumnIndex(COLUMN_MAC_ADDRESS)));
            c2.moveToFirst();
            if (c2.moveToFirst()) {
                do {
                    temp.put("LATITUDE", c2.getString(c2.getColumnIndex(LATITUDE)));
                    temp.put("LONGITUDE", c2.getString(c2.getColumnIndex(LONGITUDE)));
                } while (c2.moveToNext());
            }
            c2.close();
            list.add(temp);
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public long  insertScanRes(String BSSID, int level, String capabilities, String SSID, double lat, double lon){

        Long newRowId = Long.valueOf(-1);
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values_ap = new ContentValues();
            ContentValues values_misuration = new ContentValues();
            boolean exist = checkPrimaryKey(BSSID);
            boolean mis = checkMisExist(BSSID, level);

            values_ap.put(COLUMN_MAC_ADDRESS, BSSID);
            values_ap.put(CAPABILITIES, capabilities);
            values_ap.put(COLUMN_SSID, SSID);

            values_misuration.put(COLUMN_MAC_ADDRESS, BSSID);
            values_misuration.put(DB, level);
            values_misuration.put(LATITUDE, lat);
            values_misuration.put(LONGITUDE, lon);
            if(!exist) {
                newRowId = db.insert(TABLE_AP, null, values_ap);
            }
            if(!mis) {
                db.insert(TABLE_MEASURATION, null, values_misuration);
            }
        return newRowId;
    }

    public Cursor getRowByMac(String BSSID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] args = {BSSID};
        c = db.rawQuery(GET_ROW_BY_MAC , args);
        return c;
    }

    public double estimateCoverage(String BSSID){
        Cursor APposition = getAPposition(BSSID);
        Cursor APLastDistance = getAPcoverage(BSSID);
        double earthRadius = 6371000;
        double coverage;
        double APLat = APposition.getDouble(APposition.getColumnIndex(LATITUDE));
        double APLon = APposition.getDouble(APposition.getColumnIndex(LONGITUDE));
        double APLatDistance = APLastDistance.getDouble(APLastDistance.getColumnIndex(LATITUDE));
        double APLonDistance = APLastDistance.getDouble(APLastDistance.getColumnIndex(LONGITUDE));
        double phy1 = Math.toRadians(APLat);
        double phy2 = Math.toRadians(APLatDistance);
        double deltaLat = Math.toRadians(APLatDistance - APLat);
        double deltaLon = Math.toRadians(APLonDistance - APLon);

        double tmp = Math.sin(deltaLat/2)*Math.sin(deltaLat/2)+
                     Math.cos(phy1/2)*Math.cos(phy2/2)*
                     Math.sin(deltaLon/2)*Math.sin(deltaLon/2);
        coverage = 2 * Math.atan2(Math.sqrt(tmp), Math.sqrt(1-tmp)) * earthRadius;
        return coverage;
    }

    private Cursor getAPposition(String BSSID){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] arg = {BSSID};
        Cursor c = db.rawQuery(GET_AP_POSITION, arg);
        c.moveToFirst();
        db.close();
        return c;
    }

    private Cursor getAPcoverage(String BSSID){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] arg = {BSSID};
        Cursor c = db.rawQuery(GET_AP_COVERAGE, arg);
        c.moveToFirst();
        db.close();
        return c;
    }

    private boolean checkPrimaryKey(String BSSID){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean res = false;
        String[] arg = {BSSID};
        Cursor c = db.rawQuery(GET_ROW_BY_MAC, arg);
        if(c.getCount() > 0) res = true;
        return res;
    }

    private boolean checkMisExist(String BSSID, int level){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean res = false;
        String[] arg = {BSSID, String.valueOf(level)};
        Cursor c = db.rawQuery(GET_MIS, arg);
        if(c.getCount() > 0) res = true;
        return res;

    }


    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
