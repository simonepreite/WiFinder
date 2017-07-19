package com.simonepreite.winder;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import android.net.wifi.WifiManager;
import android.location.LocationManager;

import android.content.Context;

import com.simonepreite.winder.services.APScan;

public class Splash extends AppCompatActivity {

    //public APInfo database = new APInfo(this);
    private boolean gps = true;
    private final int SPLASH_DISPLAY_LENGTH = 1500;
    private final static int REQUEST_GPS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);


        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        if(!enabled){
            buildAlertMessageNoGps();
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startScan();
                    startInit();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
        //startScan();
        /*if(!enabled) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            while (!enabled) {
                enabled = service
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                //intent.addFlags();
                //finish();
            }
        }*/
        //else {
        //}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_GPS:
                LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                boolean enabled = service
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(!enabled){
                    finish();

                }else {
                    startScan();
                    startInit();
                }
                break;

        }
    }



    public void startInit() {
        final Intent intent = new Intent(this, APList.class);
        startActivity(intent);
        finish();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Non hai attivo il tuo GPS, vuoi attivarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_GPS);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        gps = false;
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void startScan(){

        final Intent scan = new Intent(this, APScan.class);
        startService(scan);
    }
}
