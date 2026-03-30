package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
import com.duolingo.app.models.User;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class GameMenuActivity extends AppCompatActivity {

    private TextView textUserName;
    private VocaVerseDatabase database;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        database = VocaVerseDatabase.getDatabase(this);
        textUserName = findViewById(R.id.text_user_name);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupNavigation();
        loadUserData();
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
            if (id == R.id.nav_study) {
                navigateTo(MainActivity.class, true);
                return true;
            } else if (id == R.id.nav_community) {
                return true;
            } else if (id == R.id.nav_test || id == R.id.nav_profile) {
                Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
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

    private void loadUserData() {
        int userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", -1);
        if (userId != -1) {
            VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
                User user = database.userDao().getUserById(userId);
                if (user != null) {
                    runOnUiThread(() -> {
                        String name = (user.getFullName() != null && !user.getFullName().isEmpty())
                                ? user.getFullName() : user.getUsername();
                        textUserName.setText(name + " 👋");
                    });
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

}