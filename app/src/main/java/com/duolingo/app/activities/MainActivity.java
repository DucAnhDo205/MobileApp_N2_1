package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.GameHistory;
import com.duolingo.app.models.User;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.CSVHelper;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.duolingo.app.utils.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textGreeting, textUserName;
    private VocaVerseDatabase database;
    private MaterialCardView cardStartStudy;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        NavigationHelper.setup(this, nav, R.id.nav_study);

        // Khởi tạo Database và View
        database = VocaVerseDatabase.getDatabase(this);
        textGreeting = findViewById(R.id.text_greeting);
        textUserName = findViewById(R.id.text_user_name);
        cardStartStudy = findViewById(R.id.card_start_study);


        initializeLessonData();
        updateGreeting();
        loadUserData();

        if (cardStartStudy != null) {
            cardStartStudy.setOnClickListener(v -> {
                // Chuyển hướng đến LessonSelectionActivity để chọn bài học trước khi vào Flashcard
                Intent intent = new Intent(MainActivity.this, LessonSelectionActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // Cập nhật lại màu icon khi trang được lôi từ dưới lên
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        NavigationHelper.setup(this, nav, R.id.nav_study);
    }

    private void initializeLessonData() {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            // Kiểm tra xem database đã có dữ liệu chưa bằng cách đếm số bài học (ví dụ: bài 1)
            List<VocabularyItem> existing = database.vocabularyDao().getVocabularyByLessonSync(1, "Tiếng Anh");
            
            if (existing == null || existing.isEmpty()) {
                List<VocabularyItem> allItems = CSVHelper.readVocabularyFromCSV(this, "english_lessons.csv", "Tiếng Anh");
                if (allItems != null && !allItems.isEmpty()) {
                    database.vocabularyDao().insertAll(allItems);
                }
            }
        });
    }

    private void updateGreeting() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        int timeOfDay = c.get(java.util.Calendar.HOUR_OF_DAY);
        String greeting;

        if (timeOfDay >= 0 && timeOfDay < 12) greeting = "Chào buổi sáng,";
        else if (timeOfDay >= 12 && timeOfDay < 16) greeting = "Chào buổi trưa,";
        else if (timeOfDay >= 16 && timeOfDay < 21) greeting = "Chào buổi chiều,";
        else greeting = "Chào buổi tối,";

        textGreeting.setText(greeting);
    }

    private void loadUserData() {
        int userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", -1);
        if (userId != -1) {
            VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
                User user = database.userDao().getUserById(userId);
                if (user != null) {
                    runOnUiThread(() -> textUserName.setText(user.getFullName() + " 👋"));
                }
            });
        }
    }

    public int calculateStreak(List<GameHistory> histories) {
        if (histories == null || histories.isEmpty()) return 0;

        int streak = 0;
        java.util.Calendar cal = java.util.Calendar.getInstance();

        // Đưa về mốc 0h00 ngày hôm nay
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        long todayStart = cal.getTimeInMillis();

        for (int i = 0; i < 30; i++) { // Kiểm tra tối đa 30 ngày gần đây
            long targetDayStart = todayStart - (i * 24 * 60 * 60 * 1000L);
            long targetDayEnd = targetDayStart + (24 * 60 * 60 * 1000L);

            boolean played = false;
            for (GameHistory h : histories) {
                if (h.PlayedAt >= targetDayStart && h.PlayedAt < targetDayEnd) {
                    played = true;
                    break;
                }
            }

            if (played) streak++;
            else if (i == 0) continue; // Nếu hôm nay chưa chơi thì chưa ngắt streak ngay
            else break; // Nếu một ngày trước đó không chơi -> Ngắt chuỗi
        }
        return streak;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_study);
        }
    }
}
