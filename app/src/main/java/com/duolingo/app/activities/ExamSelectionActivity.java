package com.duolingo.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
import com.duolingo.app.utils.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExamSelectionActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_selection);
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        NavigationHelper.setup(this, nav, R.id.nav_test);

        findViewById(R.id.card_easy).setOnClickListener(v -> startExam("EASY"));
        findViewById(R.id.card_medium).setOnClickListener(v -> startExam("MEDIUM"));
        findViewById(R.id.card_hard).setOnClickListener(v -> startExam("HARD"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProgressUI();

        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_test);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        NavigationHelper.setup(this, nav, R.id.nav_test);
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

}