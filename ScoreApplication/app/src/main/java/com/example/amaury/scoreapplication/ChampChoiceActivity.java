package com.example.amaury.scoreapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import android.os.AsyncTask;
import android.widget.Toast;

public class ChampChoiceActivity extends AppCompatActivity implements View.OnClickListener{

    final static String STATE_COMPETITON_LISTE = "competition_list";
    final static String STATE_ID_LISTE = "id_list";

    protected ListView mDisplay = null;
    Button buttonCompet = null;
    String mode = null;
    Intent i = null;

    protected List<String> competitionsList = new ArrayList<String>();
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


        if (savedInstanceState!=null){
            competitionsList = savedInstanceState.getStringArrayList(STATE_COMPETITON_LISTE);
            competitionIdList = savedInstanceState.getStringArrayList(STATE_ID_LISTE);
        }else{
            i = getIntent();
            mode = i.getStringExtra("MODE");
            String url = "https://apifootball.com/api/?action=get_leagues&APIkey=0a7af79b20e1367a88d2cc1ea922772ed88fb437ef3b6048229d65753ed139c1";
            new QueryTask().execute(url);
        }

        setContentView(R.layout.competition_choice);

        buttonCompet = (Button) findViewById(R.id.buttonCompetChoice);
        buttonCompet.setOnClickListener(this);

        mDisplay = (ListView) findViewById(R.id.listViewCompet);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChampChoiceActivity.this,
                android.R.layout.simple_list_item_single_choice, competitionsList);
        mDisplay.setAdapter(adapter);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(STATE_COMPETITON_LISTE, (ArrayList)competitionsList);
        outState.putStringArrayList(STATE_ID_LISTE, (ArrayList)competitionIdList);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v){
        if(v.getId() == R.id.buttonCompetChoice) {
            Intent teamChoiceActivity = new Intent(ChampChoiceActivity.this,
                    TeamChoiceActivity.class);

            int selecteditemIndex = mDisplay.getCheckedItemPosition();
            String competitionId = competitionIdList.get(selecteditemIndex);

            teamChoiceActivity.putExtra("COMPETITION", competitionId);
            teamChoiceActivity.putExtra("MODE", mode);

            startActivity(teamChoiceActivity);
        }
    }


    // Enable Up navigation to make a proper return on parent activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    public class QueryTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String searchUrl = params[0];
            String queryResults = null;
            try {
                queryResults = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return queryResults;
        }

        @Override
        protected void onPostExecute(String queryResults) {
            if (queryResults != null && !queryResults.equals("")) {

                try {
                    JSONArray competitions = new JSONArray(queryResults);

                    for (int i = 0; i < competitions.length(); i++) {
                        JSONObject competiton = competitions.getJSONObject(i);
                        String country = competiton.getString("country_name");
                        String name = competiton.getString("league_name");
                        String id = competiton.getString("league_id");

                        competitionsList.add(country + " - " + name);
                        competitionIdList.add(id);
                    }

                } catch (final JSONException e) { }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChampChoiceActivity.this,
                        android.R.layout.simple_list_item_single_choice, competitionsList);
                mDisplay.setAdapter(adapter);
            }
        }

        protected String getResponseFromHttpUrl(String url)
                throws IOException {

            URL urlObject = new URL(url);

            HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();

            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }
    }
}
