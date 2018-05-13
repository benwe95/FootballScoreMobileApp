package com.example.amaury.scoreapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout;
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

public class TeamChoiceActivity extends AppCompatActivity implements View.OnClickListener{

    protected ListView mDisplay = null;
    private Button buttonTeam = null;
    private Intent i = null;
    private  String competition_id = null;
    protected List<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lorem_activity);

        mDisplay = (ListView) findViewById(R.id.listView);

        buttonTeam = (Button) findViewById(R.id.buttonTeamChoice);
        buttonTeam.setOnClickListener(this);

        i = getIntent();
        competition_id = i.getStringExtra("COMPETITION");
    }

    @Override
    protected void onStart(){
        super.onStart();
        String output = String.format("https://apifootball.com/api/?action=get_standings&league_id=%s&APIkey=964f51a4dab3b0ae2482cbb6bd9a7a162e8786bbc1f72b73ae311bbb824794dd",
                competition_id);
        new QueryTask().execute(output);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.buttonTeamChoice) {
            Intent dateChoiceActivity = new Intent(TeamChoiceActivity.this,
                    DateChoiceActivity.class);

            dateChoiceActivity.putExtra("COMPETITION", competition_id);

            int selecteditemIndex = mDisplay.getCheckedItemPosition();
            String selectedItem = names.get(selecteditemIndex);

            dateChoiceActivity.putExtra("TEAM", selectedItem);

            startActivity(dateChoiceActivity);
        }
    }


    public class QueryTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(TeamChoiceActivity.this,"download",Toast.LENGTH_LONG).show();
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

                try{
                    JSONArray teams = new JSONArray(queryResults);

                    for (int i = 0; i < teams.length(); i++){
                        JSONObject team = teams.getJSONObject(i);
                        String name = team.getString("team_name");
                        names.add(name);
                    }

                }catch (final JSONException e) { }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeamChoiceActivity.this,
                        android.R.layout.simple_list_item_single_choice, names);
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
