package com.simonepreite.winder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.simonepreite.winder.database.AndroidDatabaseManager;

public class APList extends AppCompatActivity {

    private WifiManager Wmanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Wmanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int orientation = this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_aplist_landscape);
            Wmanager.startScan();
            FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fabMap2);
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Click action
                    Intent intent = new Intent(APList.this, APMaps.class);
                    startActivity(intent);
                }
            });

            FloatingActionButton dbfab2 =(FloatingActionButton) findViewById(R.id.fabDB2);

            dbfab2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent dbmanager = new Intent(APList.this ,AndroidDatabaseManager.class);
                    startActivity(dbmanager);
                }
            });

        }else{
            setContentView(R.layout.activity_aplist);
            Wmanager.startScan();
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMap1);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Click action
                    Intent intent = new Intent(APList.this, APMaps.class);
                    startActivity(intent);
                }
            });
            FloatingActionButton dbfab = (FloatingActionButton) findViewById(R.id.fabDB);

            dbfab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent dbmanager = new Intent(APList.this ,AndroidDatabaseManager.class);
                    startActivity(dbmanager);
                }
            });

        }

    }
}
