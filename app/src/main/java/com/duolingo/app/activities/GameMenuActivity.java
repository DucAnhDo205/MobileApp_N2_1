package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class GameMenuActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupNavigation();
        setupGameClickListeners();

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(GameMenuActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    private void setupNavigation() {
        if (bottomNavigationView == null) return;

        bottomNavigationView.setSelectedItemId(R.id.nav_community);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_community) return true;

            if (id == R.id.nav_study) {
                navigateTo(MainActivity.class, true);
                return true;
            }

            if (id == R.id.nav_test) {
                navigateTo(ExamSelectionActivity.class, false);
                return true;
            }

            if (id == R.id.nav_profile) {
                Toast.makeText(this, "Chức năng Hồ sơ đang phát triển", Toast.LENGTH_SHORT).show();
                return false;
            }
            return false;
        });
    }

    private void setupGameClickListeners() {
        setGameClick(R.id.card_game_ghep_tu, GhepTuActivity.class, "fade");
        setGameClick(R.id.card_game_quiz, QuizGameActivity.class, "slide");
        setGameClick(R.id.card_game_memory_game, MemoryGameActivity.class, "fade");
        setGameClick(R.id.card_game_sap_xep, ScrambleGameActivity.class, "slide");
    }

    private void setGameClick(int viewId, Class<?> activityClass, String animType) {
        MaterialCardView card = findViewById(viewId);
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, activityClass);
                startActivity(intent);
                if (animType.equals("fade")) {
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            });
        }
    }

    private void navigateTo(Class<?> activityClass, boolean finishCurrent) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        overridePendingTransition(0, 0);
        if (finishCurrent) finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_community);
        }
    }
}