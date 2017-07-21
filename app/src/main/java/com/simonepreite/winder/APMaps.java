package com.simonepreite.winder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simonepreite.winder.database.APInfo;
import com.simonepreite.winder.gps.GPSTracker;

import java.util.ArrayList;
import java.util.HashMap;

import static com.simonepreite.winder.database.APAuxdb.APBaseColums.*;

public class APMaps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    public GoogleMap mMap;
    private APInfo db;
    private int mode = 2;
    private String openCircleColor = "#8014EE91";
    private String protectedCirleColor = "#40fa397d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apmaps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = APInfo.getInstance(getApplicationContext());
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
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_all:
                mode = 0;
                break;
            case R.id.show_open:
                mode = 3;
                break;
            case R.id.show_closed:
                mode = -1;
                break;
            case R.id.show_range:
                mode = 2;
                break;
            default:
                mode = 2;
        }
        updateM();
        return true;
    }

    private GPSTracker getCurPos(){
        GPSTracker gps = new GPSTracker(getApplicationContext());
        if(gps.canGetLocation()) {
            gps.getLocation();
        }
        return gps;
    }

    public void updateCam(GoogleMap mMap){
        GPSTracker gps = getCurPos();
        Double lat = gps.getLatitude(); // returns latitude
        Double lon = gps.getLongitude(); // returns longitude
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
        ArrayList<HashMap<String,String>> list;
        String COLOR;
        GPSTracker gps;
        Double lat;
        Double lon;
        switch (mode) {
            case 0:
                list = db.getAllEntries();
                break;
            case 3:
                list = db.getOnlyOpen();
                break;
            case -1:
                list = db.getOnlyClosed();
                break;
            case 2:
                gps = getCurPos();
                lat = gps.getLatitude(); // returns latitude
                lon = gps.getLongitude(); // returns longitude
                list = db.getAroundMe(lat , lon);
                break;
            default:
                gps = getCurPos();
                lat = gps.getLatitude(); // returns latitude
                lon = gps.getLongitude(); // returns longitude
                list = db.getAroundMe(lat , lon);
        }

        mMap.clear();

        for(int i=0; i<list.size(); i++){
            String temp = String.valueOf(list.get(i).get("SSID"));
            LatLng curLoc = new LatLng(Double.parseDouble(list.get(i).get("LATITUDE")), Double.parseDouble(list.get(i).get("LONGITUDE")));
            if(list.get(i).get("CAPABILITIES").toLowerCase().contains("ess".toLowerCase()) || list.get(i).get("CAPABILITIES").toLowerCase().contains("wpa".toLowerCase()) || list.get(i).get("CAPABILITIES").toLowerCase().contains("wps".toLowerCase()) ||  list.get(i).get("CAPABILITIES").toLowerCase().contains("wep".toLowerCase())) {
                mMap.addMarker(new MarkerOptions()
                        .position(curLoc)
                        .title(temp)
                        .snippet(list.get(i).get("BSSID"))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                COLOR = protectedCirleColor;
            }else{
                mMap.addMarker(new MarkerOptions()
                        .position(curLoc).title(temp)
                        .snippet(list.get(i).get("BSSID"))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                COLOR = openCircleColor;
            }
            double cover = db.estimateCoverage(list.get(i).get("BSSID"));
            if(cover > 100) cover = 80;
            //Log.i("debug coverage: ", "SSID: " + list.get(i).get("SSID") + " radius: " + String.valueOf(cover));
            mMap.addCircle(new CircleOptions()
                    .center(curLoc)
                    .radius(cover)
                    .strokeColor(Color.TRANSPARENT)
                    .fillColor(Color.parseColor(COLOR))
                    .zIndex(1.0f)
                    );
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        buildAlertMessageShowDetails(marker.getTitle(), marker.getSnippet());
    }

    public class updateThread  extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void...voids) {
            while(true)
            {
                try {
                    Thread.sleep(30000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                Log.i("map thread", "update");
                            updateM();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void buildAlertMessageShowDetails(String SSID, String BSSID) {
        Cursor c = db.getAPposition(BSSID);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String Lat = "";
        String Lon = "";
        String level = "";
        String cap = "";
        int quality = 0;
        Lat = c.getString(c.getColumnIndex(LATITUDE));
        Lon = c.getString(c.getColumnIndex(LONGITUDE));
        level = c.getString(c.getColumnIndex(DB));
        quality = 2* (Integer.parseInt(level) + 100);
        if (quality > 100) quality = 100;
        c.close();
        builder.setMessage("SSID: " + SSID + "\n\nBSSID: " + BSSID + "\n\nLEVEL: " + String.valueOf(quality) + "%, " + level + " db" + "\n\nLAT: " + Lat + "\n\nLON: " + Lon)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Connect", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
