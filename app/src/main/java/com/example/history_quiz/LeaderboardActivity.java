package com.example.history_quiz;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;
/**
 * This is the Leaderboard class for the layout {@link R.layout#activity_leaderboard} of the History Quiz app.
 * It is the screen of the app that shows the total points of every player.
 * The players are ranked based on their total points in total, for every quiz they have taken.
 * @author Georgios Hakobyan
 * @author Leonidas Christoforou
 */
public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        TextView nameFirst = findViewById(R.id.name_first);
        TextView pointsFirst = findViewById(R.id.points_first);
        TextView nameSecond = findViewById(R.id.name_second);
        TextView pointsSecond = findViewById(R.id.points_second);
        TextView nameThird = findViewById(R.id.name_third);
        TextView pointsThird = findViewById(R.id.points_third);
        MyDBHandler dbHelper = new MyDBHandler(this);
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

        LinearLayout container = findViewById(R.id.container);
        container.removeAllViews();
        // Create views for the remaining players
        for (int i = 3; i < leaderboard.size(); i++) {
            Player player = leaderboard.get(i);
            // Inflate the layout for each player
            ConstraintLayout playerCard = (ConstraintLayout) getLayoutInflater().inflate(R.layout.player_card_layout, null);
            TextView leaderPos = playerCard.findViewById(R.id.leader_pos);
            TextView leaderName = playerCard.findViewById(R.id.leader_name);
            TextView playerPoints = playerCard.findViewById(R.id.points);

            leaderPos.setText(String.valueOf(i + 1));
            leaderName.setText(player.getUsername());
            playerPoints.setText(String.valueOf(player.getTotalPoints()));

            // Add the player card to the scrollable leaderboard view
            container.addView(playerCard);
        }

    }
}