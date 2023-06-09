package com.example.history_quiz;

import android.content.Context;
public class DatabaseManager {
    private static DatabaseManager instance;
    private MyDBHandler dbHandler;

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
