package com.example.amaury.scoreapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import javax.xml.transform.Result;

public class DateChoiceActivity extends Activity implements View.OnClickListener{

    DatePicker from = null;
    DatePicker to = null;
    Button buttonDate = null;
    Intent i = null;
    String competitionId = null;
    String teamName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.date_choice);

        from = (DatePicker) findViewById(R.id.from);
        to = (DatePicker) findViewById(R.id.to);

        buttonDate = (Button) findViewById(R.id.dateChoice);
        buttonDate.setOnClickListener(this);

        i = getIntent();
        competitionId = i.getStringExtra("COMPETITION");
        teamName = i.getStringExtra("TEAM");

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
            String toDay = String.valueOf(to.getDayOfMonth());

            String toDate = toYear + "-" + toMonth + "-" + toDay;

            ResultActivity.putExtra("TO", toDate);

            startActivity(ResultActivity);
        }
    }
}
