package com.simonepreite.winder;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

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
            apString.add(ap.SSID);
            //Toast.makeText(this, ap.toString(), Toast.LENGTH_SHORT).show();
        }

        /*for (String ap : apString){
            Toast.makeText(this, ap, Toast.LENGTH_SHORT).show();
        }*/

        final ListView APShow = (ListView)findViewById(id.listView);
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, layout.row, R.id.textViewList, apString);
        APShow.setAdapter(adapter);

    }
}
