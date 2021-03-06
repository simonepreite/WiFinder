package com.simonepreite.winder.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.simonepreite.winder.APDetails;

import com.simonepreite.winder.R;
import com.simonepreite.winder.services.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class APlistFragment extends Fragment {

    private ListReceiver APUpdate;
    private View v;

    public APlistFragment() {
        v = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        APUpdate = new ListReceiver();
        v = inflater.inflate(R.layout.fragment_aplist, container, false);
        getActivity().registerReceiver(APUpdate, new IntentFilter(Constants.LISTUPDATE));
        return v;
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(APUpdate);
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    class ListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final List<ScanResult> apList = (List<ScanResult>) intent.getSerializableExtra("APlist");

            ArrayList<HashMap<String, String>> apInfo = new ArrayList<>();
            for (ScanResult ap : apList){
                if(ap.SSID != "") {
                    HashMap<String,String> APelement = new HashMap<>();
                    int quality = 2* (ap.level + 100);
                    if (quality > 100) quality = 100; // formula is approximate
                    APelement.put("RouterName", ap.SSID);
                    APelement.put("level", String.valueOf(quality+"%"));
                    APelement.put("RouterMac", ap.BSSID);
                    apInfo.add(APelement);
                }
            }

            final ListView APShow = (ListView) getActivity().findViewById(R.id.listView);
            APShow.setAdapter(null);
            final SimpleAdapter adapter =
                    new SimpleAdapter(getActivity(), apInfo, R.layout.row, new String[] {"RouterName", "level"}, new int[] {R.id.textViewList, R.id.subItemList});
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

                    // cercare dettagli nel database anzichè nella lista dell'ultimo scan

                    HashMap<String, String> reverse = (HashMap<String, String>)  parent.getItemAtPosition(position);

                    String info = null;

                    for (ScanResult ap : apList){
                        if(ap.BSSID == reverse.get("RouterMac")) {
                            int quality = 2* (ap.level + 100);
                            if (quality > 100) quality = 100; // formula is approximate
                            info = ap.SSID + "\n" + ap.BSSID + "\n" + ap.capabilities + "\n" + quality + "%\n";
                        }
                    }
                    int orientation = getActivity().getResources().getConfiguration().orientation;
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

}
