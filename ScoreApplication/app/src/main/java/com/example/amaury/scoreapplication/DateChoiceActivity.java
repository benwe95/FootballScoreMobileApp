package com.example.amaury.scoreapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import javax.xml.transform.Result;

public class DateChoiceActivity extends AppCompatActivity
        implements View.OnClickListener{

    DatePicker from = null;
    DatePicker to = null;
    Button buttonDate = null;
    Intent i = null;
    String competitionId = null;
    String teamName = null;

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

        setContentView(R.layout.date_choice);

        from = (DatePicker) findViewById(R.id.from);
        to = (DatePicker) findViewById(R.id.to);

        buttonDate = (Button) findViewById(R.id.dateChoice);
        buttonDate.setOnClickListener(this);

        i = getIntent();
        competitionId = i.getStringExtra("COMPETITION");
        teamName = i.getStringExtra("TEAM");
        Log.i("DEBUG", "Date - Team name: " + teamName);

    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.dateChoice) {
            Intent ResultActivity = new Intent(DateChoiceActivity.this,
                    ResultActivity.class);

            ResultActivity.putExtra("COMPETITION", competitionId);
            ResultActivity.putExtra("TEAM", teamName);

            String fromYear = String.valueOf(from.getYear());
            String fromMonth = String.valueOf(from.getMonth());
            String fromDay = String.valueOf(from.getDayOfMonth());

            String fromDate = fromYear + "-" + fromMonth + "-" + fromDay;

            ResultActivity.putExtra("FROM", fromDate);

            String toYear = String.valueOf(to.getYear());
            String toMonth = String.valueOf(to.getMonth());
            Log.i("DATE", "Date - toMonth: "+String.valueOf(to.getMonth()));
            String toDay = String.valueOf(to.getDayOfMonth());

            String toDate = toYear + "-" + toMonth + "-" + toDay;

            ResultActivity.putExtra("TO", toDate);

            startActivity(ResultActivity);
        }
    }


    // Enable Up navigation to make a proper return on parent activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home) {
//            Log.i("DEBUG", "date item: "+ item);
//            Log.i("DEBUG", "item.getItemId: "+ item.getItemId());
//            Log.i("DEBUG", "R.id.home: "+ R.id.home);
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
