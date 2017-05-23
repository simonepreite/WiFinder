package com.simonepreite.winder;

import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


public class APList extends FragmentActivity {

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
    }

}

