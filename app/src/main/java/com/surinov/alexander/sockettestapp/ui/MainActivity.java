package com.surinov.alexander.sockettestapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.surinov.alexander.sockettestapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.fragmentContainer1) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer1, SimpleFragment.newInstance(true))
                    .replace(R.id.fragmentContainer2, SimpleFragment.newInstance(false))
                    .commit();
        }
    }
}