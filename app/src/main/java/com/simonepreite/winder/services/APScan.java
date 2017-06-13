package com.simonepreite.winder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class APScan extends Service {

    private WifiManager wifiManager;
    private WifiReceiver wifiReceiver;
    private ServiceHandler serviceHandler;

    public static final String RETURN_VALUE = "return value service";

    public APScan() {
    }


    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            try {
                Thread.sleep(10000); //10 secondi
                // database update

            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }

    }



    @Override
    public void onCreate(){
        wifiReceiver = new WifiReceiver();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION), null, serviceHandler);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();



        //Message msg = serviceHandler.obtainMessage();
        //msg.arg1 = startId;
        //serviceHandler.sendMessage(msg);

        //Scanning();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*public void Scanning(){
        Intent APUpdate = new Intent();
        APUpdate.setAction(Constants.BROADCAST_ACTION);
        APUpdate.putExtra(Constants.EXTENDED_DATA_STATUS, 1);
        sendBroadcast(APUpdate);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
    }
*/




    // con un broadcast receiver l'app viene notificata dal sistema ogni qualvolta si verifica
    // un determinato evento (per il quale il receiver si è registrato)
    //
    class WifiReceiver extends BroadcastReceiver {

        // executes when scan results are available (foreach scan ? or only the first time?)
        @Override
        public void onReceive(Context context, Intent intent) {
            final List<ScanResult> apList = wifiManager.getScanResults();
            // a chi spedisco questi dati? devo usarli per aggiornare PRIMA il database e poi la mappa
            // per rendere più accurata la posizione
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
            APUpdate.setAction(Constants.LIST_VIEW);
            APUpdate.putParcelableArrayListExtra("APlist", (ArrayList<? extends Parcelable>) apList);
            sendBroadcast(APUpdate);
        }

    }
}
