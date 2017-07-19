package com.simonepreite.winder.threads;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simonepreite.winder.database.APInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by smogarch on 12/07/17.
 */

public class updateMap extends Thread{

    GoogleMap mMap;
    private APInfo db;

    public updateMap(GoogleMap mMapIstance, APInfo dbIstance ){
        this.mMap = mMapIstance;
        this.db = dbIstance;
    }

    public void run(){
        for(;;) {
            try {
                sleep(5000);

                ArrayList<HashMap<String,String>> list = db.getAllEntries();
                mMap.clear();
                for(int i=0; i<list.size(); i++){
                    String temp = String.valueOf(list.get(i).get("SSID"));
                    LatLng curLoc = new LatLng(Double.parseDouble(list.get(i).get("LATITUDE")), Double.parseDouble(list.get(i).get("LONGITUDE")));
                    if(list.get(i).get("CAPABILITIES").toLowerCase().contains("ess".toLowerCase()) || list.get(i).get("CAPABILITIES").toLowerCase().contains("wpa".toLowerCase()) || list.get(i).get("CAPABILITIES").toLowerCase().contains("wps".toLowerCase()) || list.get(i).get("CAPABILITIES").toLowerCase().contains("wps".toLowerCase()) || list.get(i).get("CAPABILITIES").toLowerCase().contains("wep".toLowerCase())) {
                        mMap.addMarker(new MarkerOptions().position(curLoc).title(temp).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }else{
                        mMap.addMarker(new MarkerOptions().position(curLoc).title(temp).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                    double cover = db.estimateCoverage(list.get(i).get("BSSID"));
                    if(cover > 100) cover = 80;
                    Log.i("debug coverage: ", "SSID: " + list.get(i).get("SSID") + " radius: " + String.valueOf(cover));
                    mMap.addCircle(new CircleOptions()
                            .center(curLoc)
                            .radius(cover)
                            .strokeColor(Color.TRANSPARENT)
                            .fillColor(Color.parseColor("#8014EE91"))
                            .zIndex(1.0f)
                    );
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }



}
