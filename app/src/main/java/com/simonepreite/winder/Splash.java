package com.simonepreite.winder;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import android.net.wifi.WifiManager;
import android.location.LocationManager;

import android.content.Context;

import com.simonepreite.winder.services.APScan;
import com.simonepreite.winder.database.APInfo;

public class Splash extends AppCompatActivity {

    public APInfo database = new APInfo(this);

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);


        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            finish();
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    start_init();
                }
            }, SPLASH_DISPLAY_LENGTH);
            startScan();
        }
    }

    public void startScan(){

        final Intent scan = new Intent(this, APScan.class);
        startService(scan);
    }

    public void start_init() {
        final Intent intent = new Intent(this, APList.class);
        startActivity(intent);
        finish();
    }
}
