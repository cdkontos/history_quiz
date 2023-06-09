package com.example.history_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.ScrollView;
import androidx.cardview.widget.CardView;
import android.widget.ImageView;
import java.util.List;
public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        CardView cardBest = findViewById(R.id.cardBest);
        ImageView medal = findViewById(R.id.medal);
        CardView cardFirst = findViewById(R.id.cardFirst);
        CardView cardSecond = findViewById(R.id.cardSecond);
        CardView cardThird = findViewById(R.id.cardThird);
        TextView nameFirst = findViewById(R.id.name_first);
        TextView pointsFirst = findViewById(R.id.points_first);
        TextView nameSecond = findViewById(R.id.name_second);
        TextView pointsSecond = findViewById(R.id.points_second);
        TextView nameThird = findViewById(R.id.name_third);
        TextView pointsThird = findViewById(R.id.points_third);
        ScrollView scrollBoard = findViewById(R.id.scrollBoard);
       MyDBHandler dbHelper = new MyDBHandler(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Player> leaderboard = dbHelper.getLeaderboard();

        // Check if there are any players in the leaderboard
        if (leaderboard.size() > 0) {
            // Set data for the first player
            Player firstPlayer = leaderboard.get(0);
            nameFirst.setText(firstPlayer.getUsername());
            pointsFirst.setText(String.valueOf(firstPlayer.getTotalPoints()));
        }

        // Check if there are at least two players in the leaderboard
        if (leaderboard.size() > 1) {
            // Set data for the second player
            Player secondPlayer = leaderboard.get(1);
            nameSecond.setText(secondPlayer.getUsername());
            pointsSecond.setText(String.valueOf(secondPlayer.getTotalPoints()));
        }

        // Check if there are at least three players in the leaderboard
        if (leaderboard.size() > 2) {
            // Set data for the third player
            Player thirdPlayer = leaderboard.get(2);
            nameThird.setText(thirdPlayer.getUsername());
            pointsThird.setText(String.valueOf(thirdPlayer.getTotalPoints()));
        }

        // Create views for the remaining players
        for (int i = 3; i < leaderboard.size(); i++) {
            Player player = leaderboard.get(i);
            // Inflate the layout for each player
            CardView playerCard = (CardView) getLayoutInflater().inflate(R.layout.activity_leaderboard, null);
            TextView leaderPos = playerCard.findViewById(R.id.leader_pos);
            TextView leaderName = playerCard.findViewById(R.id.leader_name);
            TextView playerPoints = playerCard.findViewById(R.id.points);

            leaderPos.setText(String.valueOf(i + 1));
            leaderName.setText(player.getUsername());
            playerPoints.setText(String.valueOf(player.getTotalPoints()));

            // Add the player card to the scrollable leaderboard view
            scrollBoard.addView(playerCard);
        }

        }
}