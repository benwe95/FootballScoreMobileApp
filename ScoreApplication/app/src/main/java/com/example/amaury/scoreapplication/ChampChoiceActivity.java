package com.example.amaury.scoreapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ChampChoiceActivity extends AppCompatActivity implements View.OnClickListener{

    protected ListView mDisplay = null;
    Button buttonCompet = null;

    protected List<String> competitionsList = new ArrayList<String>();
    protected List<String> competitionIdList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competition_choice);

        buttonCompet = (Button) findViewById(R.id.buttonCompetChoice);
        buttonCompet.setOnClickListener(this);

        mDisplay = (ListView) findViewById(R.id.listViewCompet);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = "https://apifootball.com/api/?action=get_leagues&APIkey=964f51a4dab3b0ae2482cbb6bd9a7a162e8786bbc1f72b73ae311bbb824794dd";
        new QueryTask().execute(url);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.buttonCompetChoice) {
            Intent teamChoiceActivity = new Intent(ChampChoiceActivity.this,
                    TeamChoiceActivity.class);

            int selecteditemIndex = mDisplay.getCheckedItemPosition();
            String competitionId = competitionIdList.get(selecteditemIndex);

            teamChoiceActivity.putExtra("COMPETITION", competitionId);

            startActivity(teamChoiceActivity);
        }
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