package com.example.amaury.scoreapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;;
import android.widget.Toast;

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

public class ResultActivity extends AppCompatActivity {

    protected ListView mDisplay = null;
    private Intent i = null;
    private String competition_id = null;
    protected List<String> names = new ArrayList<String>();
    String competitionId = null;
    String teamName = null;
    String from = null;
    String to = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("DEBUG", "Result - Enter onCreate()");

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

        setContentView(R.layout.result_activity);

        mDisplay = (ListView) findViewById(R.id.simpletext);


        i = getIntent();
        competitionId = i.getStringExtra("COMPETITION");
        teamName = i.getStringExtra("TEAM");
        from = i.getStringExtra("FROM");
        to = i.getStringExtra("TO");

        Log.i("DEBUG", "Result - teamName: " +i.getStringExtra("TEAM"));


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("DEBUG", "Result - onStart()");
        Log.i("DEBUG", "Result - compet id: " + competitionId);
        Log.i("DEBUG", "Result - from: " + from);
        Log.i("DEBUG", "Result - to: " +to);

        String url = String.format("https://apifootball.com/api/?action=get_events&from=%s&to=%s&league_id=%s&APIkey=0a7af79b20e1367a88d2cc1ea922772ed88fb437ef3b6048229d65753ed139c1",
                from, to, competitionId);
        Log.i("DEBUG", "http: " + url);
        new QueryTask().execute(url);
    }


    public class QueryTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ResultActivity.this, "download", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i("DEBUG", "Result - QUERY - doInBackground()");

            String searchUrl = params[0];
            String queryResults = null;
            try {
                queryResults = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("DEBUG", "Result - QUERY - queryResults" + queryResults);

            return queryResults;
        }

        @Override
        protected void onPostExecute(String queryResults) {
            if (queryResults != null && !queryResults.equals("")) {

                try {
                    JSONArray matchs = new JSONArray(queryResults);

                    Log.i("DEBUG", "Result - JSON matchs: " + matchs);


                    for (int i = 0; i < matchs.length(); i++) {
                        JSONObject team = matchs.getJSONObject(i);
                        String name_home = team.getString("match_hometeam_name");
                        String name_away = team.getString("match_awayteam_name");
                        String goals_home = team.getString("match_hometeam_score");
                        String goals_away = team.getString("match_awayteam_score");
                        if(name_home.equals(teamName) || name_away.equals(teamName)) {
                            names.add(name_home + " - " + name_away + " : " + goals_home + " - " + goals_away);
                        }
                    }

                } catch (final JSONException e) {
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResultActivity.this,
                        android.R.layout.simple_list_item_1, names);
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


    // Enable Up navigation to make a proper return on parent activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
