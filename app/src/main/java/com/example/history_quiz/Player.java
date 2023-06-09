package com.example.history_quiz;

public class Player {
    private String Username;
    private int Points;

    public Player(String Username, int Points) {
        this.Username = Username;
        this.Points = Points;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public void setTotalPoints(int points){
        this.Points=points;
    }

    public String getUsername() {
        return Username;
    }

    public int getTotalPoints() {
        return Points;
    }
}

