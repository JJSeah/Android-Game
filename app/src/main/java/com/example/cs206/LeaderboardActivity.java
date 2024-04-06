package com.example.cs206;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * LeaderboardActivity class that represents the leaderboard screen.
 */
public class LeaderboardActivity extends AppCompatActivity {
    private static final int ROW_PADDING = 20;
    private static final int TEXT_SIZE = 18;
    private static final int TEXT_COLOR = Color.WHITE;
    private static final int SPACE_HEIGHT = 20;

    private DatabaseHelper dbHelper;
    private TableLayout leaderboardTable;

    /**
     * Method that is called when the activity is starting.
     * This is where most initialization should go.
     *
     * @param savedInstanceState This is a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        EdgeToEdge.enable(this);
        dbHelper = new DatabaseHelper(this);
        leaderboardTable = findViewById(R.id.leaderboard_table);

        List<Long> timeSpentList = dbHelper.getTimeSpentList();
        for (int i = 0; i < timeSpentList.size(); i++) {
            // Create TableRow
            TableRow row = new TableRow(this);
            row.setPadding(ROW_PADDING, ROW_PADDING, ROW_PADDING, ROW_PADDING);

            TextView timeView = new TextView(this);
            String score = String.valueOf(timeSpentList.get(i));
            score += "s remaining";

            timeView.setText(score);
            timeView.setGravity(Gravity.CENTER);
            timeView.setTextSize(TEXT_SIZE);
            timeView.setTextColor(TEXT_COLOR);

            timeView.setPadding(10, 10, 10, 10);
            row.addView(timeView);

            leaderboardTable.addView(row);

            if (i != timeSpentList.size() - 1) {
                View space = new View(this);
                space.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, SPACE_HEIGHT));
                leaderboardTable.addView(space);
            }
        }

        Button backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderboardActivity.this, MainMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Method that is called when the activity is destroyed.
     * It closes the dbHelper.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}