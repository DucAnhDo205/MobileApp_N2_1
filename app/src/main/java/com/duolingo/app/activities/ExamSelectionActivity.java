package com.duolingo.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExamSelectionActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_selection);

        findViewById(R.id.card_easy).setOnClickListener(v -> startExam("EASY"));
        findViewById(R.id.card_medium).setOnClickListener(v -> startExam("MEDIUM"));
        findViewById(R.id.card_hard).setOnClickListener(v -> startExam("HARD"));

        setupMainNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProgressUI();

        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_test);
        }
    }

    private void updateProgressUI() {
        SharedPreferences pref = getSharedPreferences("VocaVerse_Exam", MODE_PRIVATE);

        updateLevelProgress(pref, "EASY", R.id.pb_easy, R.id.tv_status_easy, 10);
        updateLevelProgress(pref, "MEDIUM", R.id.pb_medium, R.id.tv_status_medium, 15);
        updateLevelProgress(pref, "HARD", R.id.pb_hard, R.id.tv_status_hard, 20);
    }

    private void updateLevelProgress(SharedPreferences pref, String level, int pbId, int tvId, int total) {
        ProgressBar pb = findViewById(pbId);
        TextView tv = findViewById(tvId);

        if (pb == null || tv == null) return;

        boolean isDone = pref.getBoolean("DONE_" + level, false);
        int score = pref.getInt("SCORE_" + level, 0);

        if (isDone) {
            pb.setMax(total);
            pb.setProgress(score);
            tv.setText("Hoàn thành: " + score + "/" + total);
            tv.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            pb.setMax(total);
            pb.setProgress(0);
            tv.setText("Chưa hoàn thành");
            tv.setTextColor(Color.parseColor("#757575"));
        }
    }

    private void startExam(String level) {
        Intent intent = new Intent(this, ComprehensiveExamActivity.class);
        intent.putExtra("DIFFICULTY_LEVEL", level);
        startActivity(intent);
    }

    private void setupMainNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView == null) return;

        bottomNavigationView.setSelectedItemId(R.id.nav_test);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_test) return true;

            if (id == R.id.nav_study) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            }

            if (id == R.id.nav_community) {
                Intent intent = new Intent(this, GameMenuActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            }

            if (id == R.id.nav_profile) {
                // Thêm Toast hoặc Activity Hồ sơ nếu cần
                return false;
            }

            return false;
        });
    }
}