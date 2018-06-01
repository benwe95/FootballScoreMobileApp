package com.example.amaury.scoreapplication;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private Button buttonSearch = null;
    private Button buttonFavorite = null;
    private Button buttonSettings = null;
    private Button buttonOffline = null;

    // File variable
    File file = null;
    String fileName = "theme.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Interface SharedPreference for accessing and modifying preference data returned by
          "getSharedPreferences(String, int)". Each particular set of preferences (color, langage,.)
          has a single instance of this class that represent it.
          Modifications are done with the SharedPreferences.Editor object that ensure the preference
          values remain in a consistant state.

          The PreferenceManager is used to help create Preference hierarchies from activities or XML
          Its method getDefaultSharedPreferences return an instance of type SharedPreference that
          points to the default file used by the preference
         */
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        /*The instance of SharedPreference is used as an observer that listen to values of the pref.
          "registerOnSharedPreferenceChangeListener" registers a callback to be invoked when a
          change happens to a preference.
         */
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // "getString(String key, String defValue)" retrieves a String value from the preferences.
        String theme = sharedPreferences.getString("color", "");

        switch (theme){
            case "blue":
                setTheme(R.style.AppTheme);
                break;

            case "red":
                setTheme(R.style.AppThemeRed);
                break;

            case "green":
                setTheme(R.style.AppThemeGreen);
                break;
        }

        setContentView(R.layout.activity_main);

        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        buttonFavorite = (Button) findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(this);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(this);
        buttonOffline = (Button) findViewById(R.id.buttonOffline);
        buttonOffline.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /* Handle the events associated to the buttons */
    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.buttonSearch:
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                    Intent champChoiceActivity =
                            new Intent(MainActivity.this, ChampChoiceActivity.class);
                    champChoiceActivity.putExtra("MODE", "search");
                    startActivity(champChoiceActivity);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No available connection",
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.buttonFavorite:
                Intent favoriteActivity = new Intent(MainActivity.this,
                        FavoriteActivity.class);
                startActivity(favoriteActivity);
                break;

            case R.id.buttonSettings:
                Intent prefsFragment = new Intent(MainActivity.this,
                        SettingsActivity.class);
                startActivity(prefsFragment);
                break;

            case R.id.buttonOffline:
                Intent offlineActivity = new Intent(MainActivity.this,
                        OfflineActivity.class);
                startActivity(offlineActivity);
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) {

        if (key.equals("color")) {

        }
    }


    void updateDatabase(){

        DatabaseManager databaseManager = new DatabaseManager(this);


    }
}
