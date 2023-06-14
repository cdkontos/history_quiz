package com.example.history_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the RulesActivity class for the layout {@link R.layout#activity_rules} of the History Quiz app.
 * The quiz rules are shown to the user, as well as the field to type their username.
 * and the button to start the quiz and take the user to the {@link QuizActivity} layout {@link R.layout#activity_quiz}.
 * @author Christos Kontos
 * @author Georgios Hakobyan
 * @author Leonidas Christoforou
 */
public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        final Button playButton = findViewById(R.id.playButton2);
        final EditText usernameEditText = findViewById(R.id.editTextText); // Assuming the username EditText has the ID "usernameEditText"

        playButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            DatabaseManager databaseManager = DatabaseManager.getInstance(getApplicationContext());
            MyDBHandler myDBHandler = databaseManager.getDBHandler();
            myDBHandler.AddPlayer(username);
            Intent intent = new Intent(RulesActivity.this, QuizActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}