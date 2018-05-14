package com.example.amaury.scoreapplication;

import android.content.SharedPreferences;
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

    // File variable
    File file = null;
    String fileName = "theme.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.buttonSearch:
                Intent champChoiceActivity = new Intent(MainActivity.this, ChampChoiceActivity.class);
                champChoiceActivity.putExtra("MODE", "search");
                startActivity(champChoiceActivity );
                break;

            case R.id.buttonFavorite:
                Intent favoriteActivity = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(favoriteActivity);
                break;

            case R.id.buttonSettings:
                Intent prefsFragment = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(prefsFragment);
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) {

        if (key.equals("color")) {

        }
    }
}
