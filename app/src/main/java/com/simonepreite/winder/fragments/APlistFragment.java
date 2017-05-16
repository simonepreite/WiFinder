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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link APlistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link APlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class APlistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FloatingActionButton fab;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public APlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment APlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static APlistFragment newInstance(String param1, String param2) {
        APlistFragment fragment = new APlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aplist, container, false);

        final WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        fab = (FloatingActionButton) view.findViewById(R.id.fabSync);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createList(wifiManager);
            }
        });
        createList(wifiManager);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void createList(WifiManager wifiManager){
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

        final ListView APShow = (ListView) getActivity().findViewById(R.id.listView);
        APShow.setAdapter(null);
        APShow.deferNotifyDataSetChanged();
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.row, R.id.textViewList, apString);
        APShow.setAdapter(adapter);
        APShow.deferNotifyDataSetChanged();

        APShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String titoloriga = (String) parent.getItemAtPosition(position);
                Log.d("List", "Ho cliccato sull'elemento con titolo " + titoloriga);
                Toast.makeText(getActivity().getBaseContext(), "Ho cliccato sull'elemento con titolo " + titoloriga, Toast.LENGTH_LONG).show();
            }
        });

        Toast.makeText(getActivity().getBaseContext(), "updated", Toast.LENGTH_SHORT).show();
    }



}
