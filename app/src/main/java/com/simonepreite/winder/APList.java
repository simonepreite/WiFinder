package com.simonepreite.winder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.simonepreite.winder.services.APScan;
import com.simonepreite.winder.services.Constants;


public class APList extends FragmentActivity {

    private MyBroadcastReceiver serviceAPUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_aplist_landscape);
        }
        else{
            setContentView(R.layout.activity_aplist);
        }
        Log.i("service", "pre service");
        startScan();
        IntentFilter statusIntentFilter = new IntentFilter(
               Constants.BROADCAST_ACTION);
        serviceAPUpdate = new MyBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(serviceAPUpdate, statusIntentFilter);
    }

    public void startScan(){
        final Intent scan = new Intent(this, APScan.class);
        startService(scan);
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        public MyBroadcastReceiver(){
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(Constants.EXTENDED_DATA_STATUS);
            Log.d("service return", result);
        }
    }

}

