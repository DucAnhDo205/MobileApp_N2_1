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

import java.util.ArrayList;
import java.util.Calendar;
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
                Toast.makeText(this, "Đang vào bài học...", Toast.LENGTH_SHORT).show();
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
            List<VocabularyItem> existing = database.vocabularyDao().getVocabularyByCategory("Lesson 1", "Tiếng Anh");
            if (existing == null || existing.isEmpty()) {
                List<VocabularyItem> allItems = CSVHelper.readVocabularyFromCSV(this, "english_lessons.csv", "Tiếng Anh");
                if (allItems != null && !allItems.isEmpty()) {
                    List<VocabularyItem> lesson1 = new ArrayList<>();
                    for (int i = 0; i < Math.min(allItems.size(), 150); i++) {
                        VocabularyItem item = allItems.get(i);
                        item.setCategory("Lesson 1");
                        lesson1.add(item);
                    }
                    database.vocabularyDao().insertAll(lesson1);
                }
            }
        });
    }

    private void updateGreeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
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

    private void addDummyHistory() {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            int userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", 1);

            // Kiểm tra nếu chưa có lịch sử thì mới thêm
            if (database.progressDao().getAllHistory(userId).isEmpty()) {
                long currentTime = System.currentTimeMillis();
                long dayInMillis = 24 * 60 * 60 * 1000;

                // Thêm dữ liệu cho 5 ngày liên tiếp để test "Chuỗi ngày"
                database.progressDao().insertGameHistory(new GameHistory(userId, "Ghép từ", 10, currentTime));
                database.progressDao().insertGameHistory(new GameHistory(userId, "Sắp xếp chữ", 15, currentTime - dayInMillis));
                database.progressDao().insertGameHistory(new GameHistory(userId, "Điền từ", 8, currentTime - 2 * dayInMillis));
                database.progressDao().insertGameHistory(new GameHistory(userId, "Lật thẻ", 12, currentTime - 3 * dayInMillis));
                database.progressDao().insertGameHistory(new GameHistory(userId, "Ghép từ", 5, currentTime - 4 * dayInMillis));

                runOnUiThread(() -> Toast.makeText(this, "Đã khởi tạo dữ liệu lịch sử mẫu!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public int calculateStreak(List<GameHistory> histories) {
        if (histories == null || histories.isEmpty()) return 0;

        int streak = 0;
        Calendar cal = Calendar.getInstance();

        // Đưa về mốc 0h00 ngày hôm nay
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
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