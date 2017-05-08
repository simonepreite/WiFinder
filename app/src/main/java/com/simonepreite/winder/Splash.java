package com.simonepreite.winder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import android.net.wifi.WifiManager;
import android.content.Context;
import android.net.wifi.ScanResult;

import java.io.Serializable;
import java.util.List;

public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                sendWiFiData(wifiManager);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void sendWiFiData(WifiManager wifiManager) {
        List<ScanResult> apList = wifiManager.getScanResults();

        final Intent intent = new Intent(this, APList.class);
        intent.putExtra("apList", (Serializable)apList);

        startActivity(intent);
        finish();
    }
}
