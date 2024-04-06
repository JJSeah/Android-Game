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

public class LeaderboardActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TableLayout leaderboardTable;

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
            row.setPadding(20, 20, 20, 20);

            TextView timeView = new TextView(this);
            String score = String.valueOf(timeSpentList.get(i));
            score += "s remaining";

            timeView.setText(score);
            timeView.setGravity(Gravity.CENTER);
            timeView.setTextSize(18); // Adjust text size as needed
            timeView.setTextColor(Color.WHITE); // Adjust text color as needed

            timeView.setPadding(10, 10, 10, 10);
            row.addView(timeView);

            leaderboardTable.addView(row);

            if (i != timeSpentList.size() - 1) {
                View space = new View(this);
                space.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20));
                leaderboardTable.addView(space);
            }
        }


        Button btnRestart = findViewById(R.id.btn_back);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderboardActivity.this, MainMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Add this line
                startActivity(intent);
                finish();
            }
        });

//        Button btnRestart = findViewById(R.id.btn_restart);
//        btnRestart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LeaderboardActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

//        Button btnExit = findViewById(R.id.btn_exit);
//        btnExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}