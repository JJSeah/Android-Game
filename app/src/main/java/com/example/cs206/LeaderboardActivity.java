package com.example.cs206;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
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

        dbHelper = new DatabaseHelper(this);
        leaderboardTable = findViewById(R.id.leaderboard_table);

        List<Long> timeSpentList = dbHelper.getTimeSpentList();

        for (int i = 0; i < timeSpentList.size(); i++) {

            // Apply TableRow style
            TableRow row = new TableRow(new ContextThemeWrapper(this, R.style.LeaderboardTableRowStyle));

            TextView timeView = new TextView(this);
            // Apply TextView style
            timeView.setTextAppearance(this, R.style.LeaderboardTextViewStyle);

            timeView.setText(i + 1 + ". " + String.valueOf(timeSpentList.get(i)) + " seconds");
            row.addView(timeView);

            leaderboardTable.addView(row);

//            TableRow row = new TableRow(this);
//            TextView timeView = new TextView(this);
//
//            timeView.setText(String.valueOf(timeSpentList.get(i)));
//            row.addView(timeView);
//
//            leaderboardTable.addView(row);
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
}