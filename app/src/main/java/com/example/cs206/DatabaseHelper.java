package com.example.cs206;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_state.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_PLAYER_SCORE = "player_score";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYER_SCORE_TABLE = "CREATE TABLE " + TABLE_PLAYER_SCORE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_SCORE + " INTEGER)";
        db.execSQL(CREATE_PLAYER_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER_SCORE);
        onCreate(db);
    }

    // Method to insert or update player's score
    public void insertOrUpdateScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, score);

        // Check if the row already exists
        if (db.update(TABLE_PLAYER_SCORE, values, COLUMN_ID + "=1", null) == 0) {
            // If the row does not exist, insert a new row
            values.put(COLUMN_ID, 1);
            db.insert(TABLE_PLAYER_SCORE, null, values);
        }

        db.close();
    }
    // Method to get the top scores
    public List<Integer> getTopScores() {
        List<Integer> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLAYER_SCORE, new String[]{COLUMN_SCORE}, null, null, null, null, COLUMN_SCORE + " DESC", "10");
        while (cursor.moveToNext()) {
            scores.add(cursor.getInt(0));
        }
        cursor.close();

        return scores;
    }
}