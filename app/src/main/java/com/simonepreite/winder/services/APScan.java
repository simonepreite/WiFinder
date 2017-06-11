package com.simonepreite.winder.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class APScan extends IntentService {

    public static final String RETURN_VALUE = "return value service";

    public APScan() {
        super("com.simonepreite.winder.services.APScan");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate(){
        //Toast.makeText(this, "ciao service", Toast.LENGTH_LONG).show();
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        final List<ScanResult> apList = wifiManager.getScanResults();
        for (ScanResult ap : apList){
            //Toast.makeText(this, ap.SSID, Toast.LENGTH_LONG).show();
        }
        Intent APUpdate = new Intent(Constants.BROADCAST_ACTION);
        APUpdate.putExtra(Constants.EXTENDED_DATA_STATUS, 1);
        sendBroadcast(APUpdate);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
}
