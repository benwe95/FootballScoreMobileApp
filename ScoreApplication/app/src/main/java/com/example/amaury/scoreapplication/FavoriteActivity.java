package com.example.amaury.scoreapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends Activity implements View.OnClickListener {
    protected ListView mDisplay = null;
    private Button buttonSearch = null;
    private Button buttonAddFavorite = null;

    File file = null;
    String originFile = "favorites.txt";

    protected List<String> teamsList = new ArrayList<String>();
    protected List<String> competitionIdList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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

        setContentView(R.layout.activity_favorite);
        mDisplay = (ListView) findViewById(R.id.listViewTeams);

        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        buttonAddFavorite = (Button) findViewById(R.id.buttonAddFavorite);
        buttonAddFavorite.setOnClickListener(this);

        file = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/ " + getPackageName() + "/files/" + originFile);

    }

    @Override
    public void onStart(){
        super.onStart();

        try {
            // Read from the internal Storage
            FileInputStream input = openFileInput(originFile);
            int value;
            // use string to build the string
            StringBuffer readed = new StringBuffer();

            // Read the character one by one
            while((value = input.read()) != -1) {
                readed.append((char)value);
            }

            String string = readed.toString();
            String[] teamLines = string.split(";");

            for(int i = 0; i < teamLines.length; i++)
            {
                String[] tokens= teamLines[i].split(",");

                competitionIdList.add(tokens[0]);
                teamsList.add(tokens[1]);

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FavoriteActivity.this,
                    android.R.layout.simple_list_item_single_choice, teamsList);
            mDisplay.setAdapter(adapter);


            if(input != null) {
                input.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(FavoriteActivity.this, "file not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.buttonSearch:
                Intent dateChoiceActivity = new Intent(FavoriteActivity.this, DateChoiceActivity.class);
                int selecteditemIndex = mDisplay.getCheckedItemPosition();
                String competitionId = competitionIdList.get(selecteditemIndex);
                String teamName = teamsList.get(selecteditemIndex);
                dateChoiceActivity.putExtra("COMPETITION", competitionId);
                dateChoiceActivity.putExtra("TEAM", teamName);
                startActivity(dateChoiceActivity );
                break;

            case R.id.buttonAddFavorite:
                Intent champChoiceActivity = new Intent(FavoriteActivity.this, ChampChoiceActivity.class);
                champChoiceActivity.putExtra("MODE", "addFavorite");
                startActivity(champChoiceActivity);
                break;
        }
    }
}
