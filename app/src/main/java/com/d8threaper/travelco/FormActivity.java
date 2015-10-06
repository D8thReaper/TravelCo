package com.d8threaper.travelco;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.d8threaper.travelco.Forms.AddRide;
import com.d8threaper.travelco.Forms.addStay;

public class FormActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        toolbar = (Toolbar) findViewById(R.id.form_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int form = getIntent().getIntExtra("form",1);
        Fragment fragment=null;
        String title = "Add Details";
        switch (form){
            case 1:
                fragment = new AddRide();
                title = "Add Ride";
                break;
            case 2:
                fragment = new addStay();
                title = "Add Stay";
                break;
            default:
                break;
        }

        if (fragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.formContainer, fragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle(title);
        }

    }
}
