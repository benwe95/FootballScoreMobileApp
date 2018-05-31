package com.example.amaury.scoreapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ben on 31/05/18.
 */

public class OfflineActivity extends AppCompatActivity {

    private TextView text = null;
    protected ListView mDisplay = null;
    private DatabaseManager databaseManager;
    String mode = null;
    Intent i = null;
    private static final String TAG = "OFFLINE";

    protected List<String> countriesList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offline);
        Log.i(TAG, "onCreate");

        databaseManager = new DatabaseManager(this);

        databaseManager.insertCountry("Belgium");
        databaseManager.insertCountry("France");
        databaseManager.insertCountry("United States");
        databaseManager.insertCountry("Italy");
        databaseManager.insertCountry("Englad");
        databaseManager.insertCountry("Poland");
        databaseManager.insertCountry("Spain");
        databaseManager.insertCountry("Portugal");
        databaseManager.insertCountry("Finland");
        databaseManager.insertCountry("Nederlands");

        databaseManager.close();

        mDisplay = (ListView) findViewById(R.id.listViewOfflineCountries);

    }


    @Override
    protected void onStart() {
        i = getIntent();
        mode = i.getStringExtra("MODE");
        super.onStart();
    }

    void getCountries() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }
}