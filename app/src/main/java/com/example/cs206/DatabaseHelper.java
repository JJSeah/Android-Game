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
    private static final String COLUMN_TIME_LEFT = "time_left";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYER_SCORE_TABLE = "CREATE TABLE " + TABLE_PLAYER_SCORE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_SCORE + " INTEGER," + COLUMN_TIME_LEFT + " INTEGER)";
        db.execSQL(CREATE_PLAYER_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER_SCORE);
        onCreate(db);
    }

    // Method to insert
    public void insertScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, score);

        // Insert a new row
        db.insert(TABLE_PLAYER_SCORE, null, values);

        db.close();
    }

    public void insertTimeLeft(long timeLeftInMillis) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME_LEFT, timeLeftInMillis);

        // Insert a new row
        db.insert(TABLE_PLAYER_SCORE, null, values);

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

    public List<Long> getTimeLeftList() {
        List<Long> timeLeftList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLAYER_SCORE, new String[]{COLUMN_TIME_LEFT}, null, null, null, null, COLUMN_TIME_LEFT + " DESC", "10");
        while (cursor.moveToNext()) {
            timeLeftList.add(cursor.getLong(0));
        }
        cursor.close();

        return timeLeftList;
    }

}