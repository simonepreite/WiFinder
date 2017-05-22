package com.simonepreite.winder.fragments;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.simonepreite.winder.R;

import java.util.ArrayList;
import java.util.List;

public class APlistFragment extends Fragment {

    private FloatingActionButton fab;

    private OnFragmentInteractionListener mListener;

    public APlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void createList(WifiManager wifiManager, View view){
        //final Intent splashIntent = getIntent();
        List<ScanResult> apList = wifiManager.getScanResults();
        //ArrayList<ScanResult> apList = (ArrayList<ScanResult>) splashIntent.getSerializableExtra("apList");
        final List<String> apString = new ArrayList<>();
        for (ScanResult ap : apList){
            if(ap.SSID != "")
                apString.add(ap.SSID);
                //Toast.makeText(getActivity(), ap.SSID, Toast.LENGTH_SHORT).show();
        }

        /*for (String ap : apString){
            Toast.makeText(this, ap, Toast.LENGTH_SHORT).show();
        }*/

        //final ListView APShow = (ListView) getActivity().findViewById(R.id.listView);
        final ListView APShow = (ListView) view.findViewById(R.id.listView);
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
        /*APShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String titoloriga = (String) parent.getItemAtPosition(position);
                Log.d("List", "Ho cliccato sull'elemento con titolo " + titoloriga);
                Toast.makeText(getActivity().getBaseContext(), "Ho cliccato sull'elemento con titolo " + titoloriga, Toast.LENGTH_LONG).show();
            }
        });

        Toast.makeText(getActivity().getBaseContext(), "updated", Toast.LENGTH_SHORT).show();*/
    }



}
