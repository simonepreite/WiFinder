package com.simonepreite.winder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.simonepreite.winder.services.APScan;
import com.simonepreite.winder.services.Constants;

import java.util.ArrayList;
import java.util.List;


public class APList extends FragmentActivity {

    private ScanReceiver serviceAPUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceAPUpdate = new ScanReceiver();
        int orientation = this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_aplist_landscape);
        }
        else{
            setContentView(R.layout.activity_aplist);
        }
        Log.i("service", "pre service");
        startScan();
        IntentFilter statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        //LocalBroadcastManager.getInstance(this).registerReceiver(serviceAPUpdate, statusIntentFilter);
        //registerReceiver(serviceAPUpdate, new IntentFilter(Constants.BROADCAST_ACTION));
    }

    public void startScan(){
        final Intent scan = new Intent(this, APScan.class);
        startService(scan);
    }


    class ScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //String result = intent.getStringExtra(Constants.EXTENDED_DATA_STATUS);
            List<ScanResult> apList = (List<ScanResult>) intent.getSerializableExtra("APlist");
            for (int i = 0; i < apList.size(); i++) {
                Toast toast = Toast.makeText(getApplicationContext(), "SSID: " + apList.get(i).SSID + "\n" +
                                "BSSID: " + apList.get(i).BSSID + "\n" +
                                "capabilities: " + apList.get(i).capabilities + "\n" +
                                "frequency: " + apList.get(i).frequency + "\n" +
                                "level: " + apList.get(i).level + "\n" +
                                "timestamp: " + apList.get(i).timestamp
                        , Toast.LENGTH_LONG);
                toast.show();
            }


        }
    }

}
