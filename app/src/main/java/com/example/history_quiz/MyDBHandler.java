package com.example.history_quiz;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import android.database.Cursor;
import android.util.Log;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * This is the MyDBHandler class that is responsible for managing the SQL database of the History Quiz app.
 * @author Georgios Hakobyan
 */

public class MyDBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="HistoryQuiz.db";
    private static final int DATABASE_VERSION=1;


    // We have 3 Entities: Players,Questions,Answers


    //table for the players and its attributes,IDplayer(primary key)
    private static final String TABLE_PLAYERS="Players";
    private static final String COLUMN_ID_PLAYER="IDPlayer";
    private static final String COLUMN_USERNAME="Username";
    private static final String COLUMN_POINTS="Points";


    //table for the Questions and its attributes, Question_ID(primary key)
    private static final String TABLE_QUESTIONS="Questions";
    private static final String COLUMN_QUESTION_ID="Question_ID";
    private static final String COLUMN_TEXT="Question_Text";

    //table for the Answers/Choices and its attributes, Answer_ID(primary key)
    private static final String TABLE_ANSWERS="Answers";
    private static final String COLUMN_ANSWERS_ID="Answers_ID";
    private static final String COLUMN_ANSWERS_TEXT="Answers_Text";
    private static final String COLUMN_IS_CORRECT="Answers_Is_Correct";



    public MyDBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating the table for the Players
        String playersQuery =
                "CREATE TABLE " + TABLE_PLAYERS +
                        " (" + COLUMN_ID_PLAYER + " INTEGER PRIMARY KEY, " +
                        COLUMN_USERNAME + " TEXT, " +
                        COLUMN_POINTS + " INTEGER);";
        db.execSQL(playersQuery);

        //Creating the table for the Questions
            String QuestionsQuery=
                    "CREATE TABLE " + TABLE_QUESTIONS +
                            " (" + COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             COLUMN_TEXT + " TEXT);";
             db.execSQL(QuestionsQuery);

        //Creating the table for all the Answer options. Question_ID is a foreign key
        String AnswersQuery=
                "CREATE TABLE " + TABLE_ANSWERS +
                        " (" + COLUMN_ANSWERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_QUESTION_ID + " INTEGER, " +
                        COLUMN_ANSWERS_TEXT + " TEXT, " +
                        COLUMN_IS_CORRECT + " INTEGER);";
        db.execSQL(AnswersQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //dropping and recreating the tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        onCreate(db);
    }


    /**
     * This method is used whenever a username is typed in the {@link R.layout#activity_rules}.
     * When it receives the username it checks if the username already exists in the database.
     * If the username doesn't exist it randomly generates an ID number for the player and it initializes his points to 0.
     * @param username The name of the player that is requested to be added.
     * @author Georgios Hakobyan
     */
        //3 Methods will be used for the players in the database: Adding,Getting and Checking if the player exists in the db
        public void AddPlayer(String username){
            SQLiteDatabase db=this.getWritableDatabase();

            try{
                db.beginTransaction();
                //to add a player, first we have to check if he exists already and his randomly generated id as well
                int idPlayer = generateRandomId();
                boolean idExists= playerIdExists(db,idPlayer);
                while(idExists){
                    idPlayer=generateRandomId();
                    idExists=playerIdExists(db,idPlayer);
                }
                if(PlayerNotExists(db,username)){
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_ID_PLAYER, idPlayer);
                    values.put(COLUMN_USERNAME, username);
                    values.put(COLUMN_POINTS, 0);
                    db.insert(TABLE_PLAYERS, null, values);
                }
                db.setTransactionSuccessful();
            }catch(Exception e){
                Log.e("Quiz Database", "Error adding players",e);
            }finally{
                db.endTransaction();
            }
        }


    /**
     * This method is used to check if a given ID already exists in the database.
     * @param db SQL database instance.
     * @param idPlayer The ID that needs to be checked.
     * @return {@code true} if this ID exists, {@code false} if it doesn't.
     * @author Georgios Hakobyan
     */
    private boolean playerIdExists(SQLiteDatabase db, int idPlayer) {
        Cursor cursor = db.query(TABLE_PLAYERS,
                new String[]{COLUMN_ID_PLAYER},
                COLUMN_ID_PLAYER + " = ?",
                new String[]{String.valueOf(idPlayer)},
                null, null, null);

        boolean exists = (cursor != null && cursor.moveToFirst());

        if (cursor != null) {
            cursor.close();
        }

        return exists;
    }

    /**
     * This method is used to generate a random number for the ID of the new player.
     * @return the generated number.
     * @author Georgios Hakobyan
     */
    private int generateRandomId() {
        Random random = new Random();
        return random.nextInt(1000); // Adjust the range as per your requirements
    }

    /**
     * This method checks if the username given exists already in the database.
     * @param db The SQL database instance.
     * @param username The username that needs to be checked.
     * @return {@code true} if this username doesn't exist, {@code false} if it does.
     * @author Georgios Hakobyan
     */
    private boolean PlayerNotExists(SQLiteDatabase db,String username) {
        Cursor cursor1 = db.query(TABLE_PLAYERS,
                new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + " = ?",
                new String[]{username},
                null, null, null);

        boolean exists = (cursor1 != null && cursor1.moveToFirst());

        if (cursor1 != null) {
            cursor1.close();
        }

        return !exists;
    }

    /**
     * This method is used to retrieve the leaderboard from the database. It sorts all the players from highest to lowest points earned.
     * @return A list that includes all the player objects from the database sorted based on their total points.
     * @author Georgios Hakobyan
     */
    public List<Player> getLeaderboard() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_ID_PLAYER + ", " + COLUMN_USERNAME + ", " + COLUMN_POINTS +
                " FROM " + TABLE_PLAYERS +
                " ORDER BY " + COLUMN_POINTS + " DESC";

        List<Player> leaderboard = new ArrayList<>();

        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
                int pointsIndex = cursor.getColumnIndex(COLUMN_POINTS);

                do {
                    if (usernameIndex != -1 && pointsIndex != -1) {
                        String username = cursor.getString(usernameIndex);
                        int points = cursor.getInt(pointsIndex);
                        Player player = new Player(username, points);
                        leaderboard.add(player);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Quiz Database", "Error presenting leaderboard", e);
        }

        return leaderboard;
    }

    /**
     * This method returns the current points of a specific player.
     * @param username The username of the player that his score is needed.
     * @return The score of the player.
     * @author Georgios Hakobyan
     */
    public int getPlayerScore(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int score = 0;

        String[] projection = {COLUMN_POINTS};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_PLAYERS, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int scoreColumnIndex = cursor.getColumnIndex(COLUMN_POINTS);
            if (scoreColumnIndex != -1) {
                score = cursor.getInt(scoreColumnIndex);
            }
        }
        cursor.close();

        return score;
    }


    /**
     * This method is used to update the points of a player. It receives the recent score and adds his previous.
     * @param username The username of the player.
     * @param score The recent score.
     * @author Georgios Hakobyan
     */
    public void updatePlayerScore(String username, int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Retrieve the current score from the database
        int currentScore = getPlayerScore(username);

        // The new total score
        int totalScore = currentScore + score;

        // Update the total score in the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_POINTS, totalScore);

        String whereClause = COLUMN_USERNAME + " = ?";
        String[] whereArgs = { username };

        db.update(TABLE_PLAYERS, values, whereClause, whereArgs);

        db.close();
    }







}
