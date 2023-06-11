package com.example.history_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button playButton = findViewById(R.id.playButton);
        MyDBHandler dbhandler = DatabaseManager.getInstance(this).getDBHandler();
        playButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RulesActivity.class)));

        final Button leaderboardButton = findViewById(R.id.leaderboardButton);
        leaderboardButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LeaderboardActivity.class)));
    }

}