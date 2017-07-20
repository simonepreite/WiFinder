package com.simonepreite.winder.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.simonepreite.winder.R;

import java.util.ArrayList;

public class APDetailsFragment extends Fragment  {


    ViewGroup root;
    private OnFragmentInteractionListener mListener;

    public APDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_apdetails, null, false);

        return root;
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

    public void setMessage(String msg){
        ArrayList<String> apInfo = new ArrayList<>();

        final ListView APdetails = (ListView) getActivity().findViewById(R.id.details);

        APdetails.setAdapter(null);

        String info[] = msg.split("\\n");
        for (String elem : info){
            apInfo.add(elem);
        }

        ArrayAdapter det = new ArrayAdapter(getActivity(), R.layout.row2, R.id.row, apInfo);

        if(APdetails != null) {
            APdetails.setAdapter(det);
            APdetails.deferNotifyDataSetChanged();
        }

        APdetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }
}
