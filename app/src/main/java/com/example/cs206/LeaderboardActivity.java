package com.example.cs206;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);

        List<Integer> scores = dbHelper.getTopScores();
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scores);
        listView.setAdapter(adapter);

        Button btnRestart = findViewById(R.id.btn_restart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart your game here
                System.out.println("Restarting game...");
                Intent intent = new Intent(LeaderboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnExit = findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}