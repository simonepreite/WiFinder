package com.simonepreite.winder.threads;

import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by smogarch on 18/06/17.
 */

public class wifiScanThread extends Thread {
    WifiManager wifi;

    public wifiScanThread(WifiManager wifiIstance){
        this.wifi = wifiIstance;
    }

    public void run(){
        for(;;){
            try {
                sleep(30000);
                this.wifi.startScan();
                //Log.i("thread", "scan");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
