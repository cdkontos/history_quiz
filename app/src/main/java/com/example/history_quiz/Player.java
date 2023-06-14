package com.example.history_quiz;

/**
 * Player object that represents a player in the History Quiz.
 * @author Georgios Hakobyan
 */
public class Player {
    private String Username;
    private int Points;

    /**
     * Constructor for the Player object.
     * @param Username The username of the player.
     * @param Points The total points of the player.
     * @author Georgios Hakobyan
     */
    public Player(String Username, int Points) {
        this.Username = Username;
        this.Points = Points;
    }

    /**
     * Getter for the username of the player.
     * @return The username of the player.
     * @author Georgios Hakobyan
     */
    public String getUsername() {
        return Username;
    }

    /**
     * Getter for the total points of the player.
     * @return The total points of the player.
     * @author Georgios Hakobyan
     */
    public int getTotalPoints() {
        return Points;
    }
}

