package com.simonepreite.winder;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simonepreite.winder.database.APInfo;
import com.simonepreite.winder.fragments.APlistFragment;
import com.simonepreite.winder.gps.GPSTracker;
import com.simonepreite.winder.services.Constants;
import com.simonepreite.winder.threads.updateMap;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class APMaps extends AppCompatActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    private APInfo db;
    private updateMap upMap;
    private ciao APUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //APUpdate = new ListReceiver();
        setContentView(R.layout.activity_apmaps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //APUpdate = new ciao();
        //this.registerReceiver(APUpdate, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        db = APInfo.getInstance(getApplicationContext());
        //registerReceiver(APUpdate, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        //new PositionUpdate1().execute();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            updateM();
            new updateThread().execute();
            updateCam(mMap);
        }

    }

    public void updateCam(GoogleMap mMap){
        GPSTracker gps = new GPSTracker(getApplicationContext());
        double lat = 0;
        double lon = 0;
        if(gps.canGetLocation()) {
            gps.getLocation();
            lat = gps.getLatitude(); // returns latitude
            lon = gps.getLongitude(); // returns longitude
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 13));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void updateM(){
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
    }

    class ciao extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateM();
        }
    }

    public class updateThread  extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void...voids) {
            while(true)
            {
                try {
                    Thread.sleep(15000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                updateM();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
