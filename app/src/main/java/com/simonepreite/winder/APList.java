package com.simonepreite.winder;

import 	android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ViewUtils;
import 	android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.lang.Object;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.List;

import static com.simonepreite.winder.R.*;


public class APList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.list_layout);

        final Intent splashIntent = getIntent();

        ArrayList<ScanResult> apList = (ArrayList<ScanResult>) splashIntent.getSerializableExtra("apList");
        final List<String> apString = new ArrayList<>();
        for (ScanResult ap : apList){
            Log.d("AP", ap.toString());
            apString.add(ap.toString());
            //Toast.makeText(this, ap.toString(), Toast.LENGTH_SHORT).show();
        }
        for (String ap : apString){
            Toast.makeText(this, ap, Toast.LENGTH_SHORT).show();
        }

        final ListView APShow = (ListView)findViewById(id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, apString);
        //Toast.makeText(this, ap.SSID + " " + ap.level, Toast.LENGTH_SHORT).show();
        //APShow.setAdapter(adapter);
    }
}
