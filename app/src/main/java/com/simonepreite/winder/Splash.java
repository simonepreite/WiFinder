package com.simonepreite.winder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import android.net.wifi.WifiManager;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.widget.Toast;

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
                startActivity();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void startActivity() {
        final Intent intent = new Intent(this, APList.class);

        startActivity(intent);
        finish();
    }
}
