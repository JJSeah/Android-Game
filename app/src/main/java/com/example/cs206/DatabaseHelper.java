package com.example.cs206;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the DatabaseHelper class for the application.
 * It extends SQLiteOpenHelper, which is a helper class to manage database creation and version management.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_state.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_PLAYER_SCORE = "player_score";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_TIME_SPENT = "time_left";
    private static final String TOP_LIMIT = "10";

    /**
     * DatabaseHelper constructor.
     * @param context The context to use to open or create the database.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYER_SCORE_TABLE = "CREATE TABLE " + TABLE_PLAYER_SCORE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_SCORE + " INTEGER," + COLUMN_TIME_SPENT + " INTEGER)";
        db.execSQL(CREATE_PLAYER_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER_SCORE);
        onCreate(db);
    }

    /**
     * Inserts a new row into the database.
     * @param score The score to insert.
     * @param timeSpentInMillis The time spent to insert.
     */
    public void insertData(int score, long timeSpentInMillis) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SCORE, score);
            values.put(COLUMN_TIME_SPENT, timeSpentInMillis);

            // Insert a new row
            db.insert(TABLE_PLAYER_SCORE, null, values);
        }
    }

    /**
     * Retrieves the top scores from the database.
     * @return A list of the top scores.
     */
    public List<Integer> getTopScores() {
        List<Integer> scores = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.query(TABLE_PLAYER_SCORE, new String[]{COLUMN_SCORE}, null, null, null, null, COLUMN_SCORE + " DESC", TOP_LIMIT);
            while (cursor.moveToNext()) {
                scores.add(cursor.getInt(0));
            }
        }
        return scores;
    }

    /**
     * Retrieves the list of time spent from the database.
     * @return A list of time spent.
     */
    public List<Long> getTimeSpentList() {
        List<Long> timeSpentList = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.query(TABLE_PLAYER_SCORE, new String[]{COLUMN_TIME_SPENT}, null, null, null, null, COLUMN_TIME_SPENT + " DESC", TOP_LIMIT);
            while (cursor.moveToNext()) {
                timeSpentList.add(cursor.getLong(0));
            }
        }
        return timeSpentList;
    }
}