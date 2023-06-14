package com.example.history_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the MainActivity class for the layout {@link R.layout#activity_main} of the History Quiz app.
 * It is the home screen of the app where the user will first be taken.
 * The user can be redirected to the {@link RulesActivity} layout {@link R.layout#activity_rules}.
 * and the {@link LeaderboardActivity} layout {@link R.layout#activity_leaderboard} to check the rules.
 * and start the quiz or check the leaderboard respectively.
 * The database that will be used for the app gets initialized here.
 * @author Christos Kontos
 * @author Leonidas Christoforou
 */
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