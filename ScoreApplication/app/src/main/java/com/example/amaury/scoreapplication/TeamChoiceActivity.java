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
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import android.os.AsyncTask;
import android.os.Environment;

public class TeamChoiceActivity extends AppCompatActivity implements View.OnClickListener{

    // Layout variable
    protected ListView mDisplay = null;
    private Button buttonTeam = null;

    // Intent variable
    private Intent i = null;
    private  String competition_id = null;
    String mode = null;

    // List for the ListView
    protected List<String> names = new ArrayList<String>();

    // File variable
    File file = null;
    String destinationFile = "favorites.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Log.i("DEBUG", "Enter onCreate()");

        // Set the theme
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


        Log.i("DEBUG", "savedInstanceState: " + savedInstanceState);

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey("competition_id_save")) {
                competition_id = savedInstanceState.getString("competition_id_save");
                mode = savedInstanceState.getString("mode_save");
                /*names = savedInstanceState.getStringArrayList("names_save");
                // Create the adaptater and give it to the adaptaterView
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeamChoiceActivity.this,
                        android.R.layout.simple_list_item_single_choice, names);
                mDisplay.setAdapter(adapter);*/
            }
        }else{
            // Get data from the intent
            i = getIntent();
            competition_id = i.getStringExtra("COMPETITION");
            mode = i.getStringExtra("MODE");
            String output = String.format("https://apifootball.com/api/?action=get_standings&league_id=%s&APIkey=0a7af79b20e1367a88d2cc1ea922772ed88fb437ef3b6048229d65753ed139c1",
                    competition_id);
            new QueryTask().execute(output);
        }


        // Set the layout and the listenener
        setContentView(R.layout.lorem_activity);

        mDisplay = (ListView) findViewById(R.id.listView);

        buttonTeam = (Button) findViewById(R.id.buttonTeamChoice);
        buttonTeam.setOnClickListener(this);

        // File to save favorite (if favorite mode)
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/ " + getPackageName() + "/files/" + destinationFile);
    }

//
//    @Override
//    protected void onStart(){
//        super.onStart();
//        Log.i("DEBUG", "Enter onStart()");
//        Log.i("DEBUG", "intent i: " + i);
//        Log.i("DEBUG", "competition_id: " + competition_id);
//        String output = String.format("https://apifootball.com/api/?action=get_standings&league_id=%s&APIkey=0a7af79b20e1367a88d2cc1ea922772ed88fb437ef3b6048229d65753ed139c1",
//                competition_id);
//        new QueryTask().execute(output);
//    }


    @Override
    public void onClick(View v){
        // Check the button id
        if(v.getId() == R.id.buttonTeamChoice) {
            // Recuperate the name of selected item
            int selecteditemIndex = mDisplay.getCheckedItemPosition();
            String selectedItem = names.get(selecteditemIndex);

            // If we are in the score search mode
            if(mode.equals("search")) {
                // Send data and go to the date choice activity
                Intent dateChoiceActivity = new Intent(TeamChoiceActivity.this,
                        DateChoiceActivity.class);

                dateChoiceActivity.putExtra("COMPETITION", competition_id);
                dateChoiceActivity.putExtra("TEAM", selectedItem);

                startActivity(dateChoiceActivity);
            }

            //
            else if(mode.equals("addFavorite")){
               try {
                    // Internal Storage
                    FileOutputStream output = openFileOutput(destinationFile, MODE_APPEND);
                    String toWrite = competition_id + "," + selectedItem + ";";

                    // Write the data in hard
                    output.write(toWrite.getBytes());

                    if(output != null) {
                        output.close();
                    }
                   Toast.makeText(TeamChoiceActivity.this, "favorite stored", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(TeamChoiceActivity.this, "The storage has failed", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Return to the favorites activity
                Intent favoriteActivity = new Intent(TeamChoiceActivity.this,
                        FavoriteActivity.class);
                startActivity(favoriteActivity);
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


    /* If the onDestroy() method is called (ex: rotation of screen) then the activity
     * would be re-created "from blank" (state of variables is not automatically stored)
     *
     * This onSaveInstanceState method saves the current state (before onStop() is called)
     * in a Bundle to save information about each View object in your activity layout.
     * This bundle is then used for the re-creation of the activity (method onCreate() )
     * with its previous state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("DEBUG", "Enter onSaveInstanceState method");
        outState.putString("comeptition_id_save", competition_id);
        outState.putString("mode_save", mode);
        outState.putStringArrayList("names_save", (ArrayList)names);

        Log.i("DEBUG", "outState: "+ outState);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("DEBUG", "Enter onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("DEBUG", "Enter onDestroy()");
    }


    public class QueryTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(TeamChoiceActivity.this,"download",Toast.LENGTH_LONG).show();
        }

        @Override
        // Get the url and executate the http request
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
        // After request processing
        protected void onPostExecute(String queryResults) {
            if (queryResults != null && !queryResults.equals("")) {

                try{
                    // Get the list of the teams and go through
                    JSONArray teams = new JSONArray(queryResults);

                    for (int i = 0; i < teams.length(); i++){
                        JSONObject team = teams.getJSONObject(i);
                        String name = team.getString("team_name");
                        names.add(name);
                    }

                }catch (final JSONException e) { }

                // Create the adaptater and give it to the adaptaterView
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeamChoiceActivity.this,
                        android.R.layout.simple_list_item_single_choice, names);
                mDisplay.setAdapter(adapter);
            }
        }

        // Get http request
        protected String getResponseFromHttpUrl(String url)
            throws IOException {

                URL urlObject = new URL(url);

                // Create connection to the url
                HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();

                try {
                    InputStream in = urlConnection.getInputStream();

                    // Scan the response
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
