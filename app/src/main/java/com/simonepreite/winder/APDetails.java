package com.simonepreite.winder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.simonepreite.winder.fragments.APDetailsFragment;

public class APDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apdetails);
        String info = getIntent().getExtras().getString("Extra_Message");
        APDetailsFragment Obj = (APDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
        if(Obj != null) Obj.setMessage(info);
    }
}
