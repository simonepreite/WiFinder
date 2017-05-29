package com.simonepreite.winder.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.simonepreite.winder.APDetails;



import com.simonepreite.winder.R;

import java.util.ArrayList;
import java.util.List;

public class APlistFragment extends Fragment {

    private FloatingActionButton fab;

    private OnFragmentInteractionListener mListener;

    public APlistFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_aplist, container, false);
        final WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        fab = (FloatingActionButton) v.findViewById(R.id.fabSync);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createList(wifiManager, v);
            }
        });
        createList(wifiManager, v);
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void createList(WifiManager wifiManager, View view){

        final List<ScanResult> apList = wifiManager.getScanResults();
        final List<String> apString = new ArrayList<>();
        for (ScanResult ap : apList){
            if(ap.SSID != "")
                apString.add(ap.SSID);
                //Toast.makeText(getActivity(), ap.SSID, Toast.LENGTH_SHORT).show();
        }

        final ListView APShow = (ListView) view.findViewById(R.id.listView);
        APShow.setAdapter(null);
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.row, R.id.textViewList, apString);
        Toast.makeText(getActivity(), String.valueOf(adapter.getCount()), Toast.LENGTH_LONG).show();
        if(APShow != null) {
            APShow.setAdapter(adapter);
            APShow.deferNotifyDataSetChanged();
        }
        else{
            Toast.makeText(getActivity(), "APShow is null", Toast.LENGTH_SHORT).show();
        }

        APShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getActivity(), APDetails.class);

                final String reverse = (String) parent.getItemAtPosition(position);

                //final ScanResult reverse = (ScanResult) parent.getItemAtPosition(position);
                String info = null; //reverse.SSID + "\n" + reverse.BSSID + "\n" + reverse.capabilities + "\n";

                for (ScanResult ap : apList){
                    if(ap.SSID != reverse)
                        info = ap.SSID + "\n" + ap.BSSID + "\n" + ap.capabilities + "\n";

                    //non è la soluzione finale ancora perchè se ho due ap con lo stesso ssid prende l'ultimo in lista e non quello cliccato

                        //apString.add(ap.SSID);
                    //Toast.makeText(getActivity(), ap.SSID, Toast.LENGTH_SHORT).show();
                }

                int orientation = getActivity().getResources().getConfiguration().orientation;
                //Toast.makeText(getActivity(), "Ho cliccato sull'elemento con titolo " + info, Toast.LENGTH_LONG).show();
                APDetailsFragment Obj = (APDetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    intent.putExtra("Extra_Message", info);
                    startActivity(intent);
                }
                else{
                    Obj.setMessage(info);
                }
            }
        });

    }



}
