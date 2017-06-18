package com.simonepreite.winder;

import android.content.Context;
import android.content.res.Configuration;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class APList extends FragmentActivity {

    private WifiManager Wmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Wmanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int orientation = this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_aplist_landscape);
            Wmanager.startScan();
        }
        else{
            setContentView(R.layout.activity_aplist);
            Wmanager.startScan();
        }
    }
}
