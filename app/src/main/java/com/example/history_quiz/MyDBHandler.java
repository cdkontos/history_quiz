package com.example.history_quiz;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import android.database.Cursor;
import android.util.Log;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
public class MyDBHandler extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME="HistoryQuiz.db";
    private static final int DATABASE_VERSION=1;


    // We have 3 Entities: Players,Questions,Answers
    //table for the players and its attributes, username(primary key)
    private static final String TABLE_PLAYERS="Players";
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
        this.context= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating the table for the Players
            String PlayersQuery=
                    "CREATE TABLE " + TABLE_PLAYERS +
                            " (" + COLUMN_USERNAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_POINTS + " INTEGER);";
            db.execSQL(PlayersQuery);

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

        //3 Methods will be used for the players in the database: Adding,Getting and Checking if the player exists in the db
        public void AddPlayer(String Username){
            SQLiteDatabase db=this.getWritableDatabase();

            try{
                db.beginTransaction();
                //to add a player, first we have to check if he exists already
                if(PlayerNotExists(db,Username)){
                    db.execSQL("INSERT INTO " + TABLE_PLAYERS + "(" + COLUMN_USERNAME + ", " +
                            COLUMN_POINTS + ") VALUES('" + Username + "', 0)");
                }
                db.setTransactionSuccessful();
            }catch(Exception e){
                Log.e("Quiz Database", "Error adding players",e);
            }finally{
                db.endTransaction();
            }
        }

    public Player getPlayer(String Username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_USERNAME,
                COLUMN_POINTS
        };

        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { Username };

        Cursor cursor = db.query(TABLE_PLAYERS, columns, selection, selectionArgs, null, null, null);

        Player player = null;
        if (cursor != null && cursor.moveToFirst()) {
            int UsernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int PointsIndex = cursor.getColumnIndex(COLUMN_POINTS);

            if (UsernameIndex != -1 && PointsIndex != -1) {
                String name = cursor.getString(UsernameIndex);
                int Points = cursor.getInt(PointsIndex);

                player = new Player(name, Points);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return player;
    }


    private boolean PlayerNotExists(SQLiteDatabase db,String Username) {
        Cursor cursor1 = db.query(TABLE_PLAYERS,
                new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + " = ?",
                new String[]{Username},
                null, null, null);

        boolean exists = (cursor1 != null && cursor1.moveToFirst());

        if (cursor1 != null) {
            cursor1.close();
        }

        return !exists;
    }

   //a method for the presentation of the leaderboard
    public List<Player> getLeaderboard() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_USERNAME + ", " + COLUMN_POINTS +
                " FROM " + TABLE_PLAYERS +
                " ORDER BY " + COLUMN_POINTS + " DESC";

        List<Player> leaderboard = new ArrayList<>();

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
                int pointsIndex = cursor.getColumnIndex(COLUMN_POINTS);

                do {
                    if (usernameIndex != -1 && pointsIndex != -1) {
                        String username = cursor.getString(usernameIndex);
                        int points = cursor.getInt(pointsIndex);
                        Player player = new Player(username,points);
                        leaderboard.add(player);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Quiz Database", "Error retrieving leaderboard", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return leaderboard;
    }

   //We will need a Player class for actions like adding points,searching for a player etc.
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
       public void addPoints(int points) {
           Points += points;

           // Updating the points of the player in the database
           SQLiteDatabase db = getWritableDatabase();
           db.execSQL("UPDATE " + TABLE_PLAYERS +
                   " SET " + COLUMN_POINTS + " = " + Points +
                   " WHERE " + COLUMN_USERNAME + " = '" + Username + "'");
       }

       public String getUsername() {
           return Username;
       }

       public int getTotalPoints() {
           return Points;
       }
   }








}
