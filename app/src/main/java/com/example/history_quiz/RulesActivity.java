package com.example.history_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        final Button playButton = findViewById(R.id.playButton2);
        final EditText usernameEditText = findViewById(R.id.editTextText); // Assuming the username EditText has the ID "usernameEditText"

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                Intent intent = new Intent(RulesActivity.this, QuizActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }
}