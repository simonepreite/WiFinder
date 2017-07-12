package com.simonepreite.winder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import android.location.LocationManager;
import android.util.LongSparseArray;

import com.simonepreite.winder.gps.GPSTracker;


import com.simonepreite.winder.threads.wifiScanThread;
import com.simonepreite.winder.database.APInfo;


public class APScan extends Service {

    private WifiManager Wmanager;
    private WifiReceiver Wreceiver;
    private wifiScanThread ScanThread;
    private APInfo db;

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
        ScanThread = new wifiScanThread(Wmanager);
        ScanThread.start();
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
            GPSTracker gps = new GPSTracker(context);
            db = APInfo.getInstance(getApplicationContext());
            final List<ScanResult> apList = Wmanager.getScanResults();
            for (int i = 0; i < apList.size(); i++) {
                double lat = 0;
                double lon = 0;
                if(gps.canGetLocation()) {
                    gps.getLocation();
                    lat = gps.getLatitude(); // returns latitude
                    lon = gps.getLongitude(); // returns longitude
                }
                long check = db.insertScanRes(apList.get(i).BSSID, apList.get(i).level, apList.get(i).capabilities, apList.get(i).SSID, lat, lon);
            }

            Intent APUpdate = new Intent();
            APUpdate.setAction(Constants.LISTUPDATE);
            APUpdate.putParcelableArrayListExtra("APlist", (ArrayList<? extends Parcelable>) apList);
            sendBroadcast(APUpdate);
        }

    }
}
