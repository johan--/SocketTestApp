package com.surinov.alexander.sockettestapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.surinov.alexander.sockettestapp.R;
import com.surinov.alexander.sockettestapp.ui.sports.SportsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.fragmentContainer) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new SportsFragment())
                    .commit();
        }
    }
}