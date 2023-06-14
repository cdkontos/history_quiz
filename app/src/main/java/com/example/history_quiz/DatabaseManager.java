package com.example.history_quiz;

import android.content.Context;
/**
 * This is the Database Manager class of the History Quiz app.
 * Its job is to manage the database handler.
 * It guarantees the use of only one instance.
 * It also allows access to it and helps with the interaction of the database.
 * @author Georgios Hakobyan
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private final MyDBHandler dbHandler;

    private DatabaseManager(Context context) {
        dbHandler = new MyDBHandler(context.getApplicationContext());
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    public MyDBHandler getDBHandler() {
        return dbHandler;
    }
}
