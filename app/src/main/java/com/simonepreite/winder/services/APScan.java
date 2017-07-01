package com.simonepreite.winder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.simonepreite.winder.threads.wifiScanThread;
import com.simonepreite.winder.database.APInfo;
import com.simonepreite.winder.database.APAuxdb;

public class APScan extends Service {

    private WifiManager Wmanager;
    private WifiReceiver Wreceiver;
    private wifiScanThread firstScanThread;

    public APScan() {
    }



    @Override
    public void onCreate(){
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Wreceiver = new WifiReceiver();
        registerReceiver(Wreceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        Wmanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        firstScanThread = new wifiScanThread(Wmanager);
        firstScanThread.start();
        Wmanager.startScan();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            APInfo db = APInfo.getInstance(getApplicationContext());
            SQLiteDatabase db_write = db.getWritableDatabase();
            SQLiteDatabase db_read = db.getReadableDatabase();
            ContentValues values = new ContentValues();
            final List<ScanResult> apList = Wmanager.getScanResults();
            for (int i = 0; i < apList.size(); i++) {
                // in questo punto va controllato se il record esiste già (attraverso il macaddress)
                // se esiste controllare se il segnale è più forte dall'ultimo aggiornamento e in caso positivo aggiornare le corrdinate
                // altrimenti lasciare il record così com'è
                values.put(APAuxdb.APBaseColums.COLUMN_MAC_ADDRESS, apList.get(i).BSSID);
                values.put(APAuxdb.APBaseColums.DB, apList.get(i).level);
                values.put(APAuxdb.APBaseColums.CAPABILITIES, apList.get(i).capabilities);
                values.put(APAuxdb.APBaseColums.COLUMN_SSID, apList.get(i).SSID);
                db_write.insert(APAuxdb.APBaseColums.TABLE_NAME, null, values);
                /*Toast toast = Toast.makeText(getApplicationContext(), "SSID: " + apList.get(i).SSID + "\n" +
                                "BSSID: " + apList.get(i).BSSID + "\n" +
                                "capabilities: " + apList.get(i).capabilities + "\n" +
                                "frequency: " + apList.get(i).frequency + "\n" +
                                "level: " + apList.get(i).level + "\n" +
                                "timestamp: " + apList.get(i).timestamp
                        , Toast.LENGTH_LONG);
                toast.show();*/
            }
            String[] projection = {
                    APAuxdb.APBaseColums.COLUMN_MAC_ADDRESS,
                    APAuxdb.APBaseColums.COLUMN_SSID,
                    APAuxdb.APBaseColums.DB,
            };
            Cursor cursor = db_read.query(APAuxdb.APBaseColums.TABLE_NAME, projection, null, null, null, null, null);
            int len = cursor.getCount();
            while(cursor.moveToNext()) {
                String temp = cursor.getString(0);
                Toast toast = Toast.makeText(getApplicationContext(), temp,Toast.LENGTH_LONG);
            }
            Intent APUpdate = new Intent();
            APUpdate.setAction(Constants.LISTUPDATE);
            APUpdate.putParcelableArrayListExtra("APlist", (ArrayList<? extends Parcelable>) apList);
            sendBroadcast(APUpdate);
        }

    }
}
