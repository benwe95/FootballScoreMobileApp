package com.example.amaury.scoreapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FavoriteActivity extends Activity implements View.OnClickListener {
    private Button buttonSearch = null;
    private Button buttonAddFavorite = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);

        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        buttonAddFavorite = (Button) findViewById(R.id.buttonAddFavorite);
        buttonAddFavorite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.buttonSearch:
                Intent dateChoiceActivity = new Intent(FavoriteActivity.this, DateChoiceActivity.class);
                dateChoiceActivity.putExtra("COMPETITION", "");
                dateChoiceActivity.putExtra("TEAM", "");
                startActivity(dateChoiceActivity );
                break;

            case R.id.buttonAddFavorite:
                break;
        }
    }
}
