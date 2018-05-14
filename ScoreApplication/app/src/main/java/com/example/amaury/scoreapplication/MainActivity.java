package com.example.amaury.scoreapplication;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button buttonSearch = null;
    private Button buttonFavorite = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        buttonFavorite = (Button) findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(this);
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
        }
    }
}
