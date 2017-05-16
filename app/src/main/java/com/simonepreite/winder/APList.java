package com.simonepreite.winder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.widget.AdapterView;
import android.widget.Toast;
import android.util.Log;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;
import static com.simonepreite.winder.R.*;

public class APList extends AppCompatActivity {

    //private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_aplist);
        /*final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        fab = (FloatingActionButton) findViewById(id.fabSync);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createList(wifiManager);
            }
        });
        createList(wifiManager);*/
    }

    /*public void createList(WifiManager wifiManager){
        //final Intent splashIntent = getIntent();

        List<ScanResult> apList = wifiManager.getScanResults();
        //ArrayList<ScanResult> apList = (ArrayList<ScanResult>) splashIntent.getSerializableExtra("apList");
        final List<String> apString = new ArrayList<>();
        for (ScanResult ap : apList){
            if(ap.SSID != "")
                apString.add(ap.SSID);
            //Toast.makeText(this, ap.toString(), Toast.LENGTH_SHORT).show();
        }

        /*for (String ap : apString){
            Toast.makeText(this, ap, Toast.LENGTH_SHORT).show();
        }*/

        /*final ListView APShow = (ListView)findViewById(id.listView);
        APShow.setAdapter(null);
        APShow.deferNotifyDataSetChanged();
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, layout.row, R.id.textViewList, apString);
        APShow.setAdapter(adapter);
        APShow.deferNotifyDataSetChanged();

        APShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String titoloriga = (String) parent.getItemAtPosition(position);
                Log.d("List", "Ho cliccato sull'elemento con titolo" + titoloriga);
                Toast.makeText(getBaseContext(), "Ho cliccato sull'elemento con titolo" + titoloriga, Toast.LENGTH_LONG).show();
            }
        });

        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
    }*/
}
