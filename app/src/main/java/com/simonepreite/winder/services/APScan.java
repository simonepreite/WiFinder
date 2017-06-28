package com.simonepreite.winder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.simonepreite.winder.threads.wifiScanThread;

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
            final List<ScanResult> apList = Wmanager.getScanResults();
            /*for (int i = 0; i < apList.size(); i++) {
                Toast toast = Toast.makeText(getApplicationContext(), "SSID: " + apList.get(i).SSID + "\n" +
                                "BSSID: " + apList.get(i).BSSID + "\n" +
                                "capabilities: " + apList.get(i).capabilities + "\n" +
                                "frequency: " + apList.get(i).frequency + "\n" +
                                "level: " + apList.get(i).level + "\n" +
                                "timestamp: " + apList.get(i).timestamp
                        , Toast.LENGTH_LONG);
                toast.show();
            }*/
            Intent APUpdate = new Intent();
            APUpdate.setAction(Constants.LISTUPDATE);
            APUpdate.putParcelableArrayListExtra("APlist", (ArrayList<? extends Parcelable>) apList);
            sendBroadcast(APUpdate);
        }

    }
}
