package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.User;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.CSVHelper;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textGreeting, textUserName;
    private VocaVerseDatabase database;
    private MaterialCardView cardStartStudy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = VocaVerseDatabase.getDatabase(this);
        textGreeting = findViewById(R.id.text_greeting);
        textUserName = findViewById(R.id.text_user_name);
        cardStartStudy = findViewById(R.id.card_start_study);

        initializeLessonData();

        updateGreeting();
        loadUserData();

        if (cardStartStudy != null) {
            cardStartStudy.setOnClickListener(v -> {
                // Giả định StudyActivity tồn tại
                // Intent intent = new Intent(MainActivity.this, StudyActivity.class);
                // startActivity(intent);
                Toast.makeText(this, "Đang vào bài học...", Toast.LENGTH_SHORT).show();
            });
        }
        
        // Cần xử lý BottomNavigationView nếu ID tồn tại
    }

    private void initializeLessonData() {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            // Kiểm tra xem đã có dữ liệu Lesson 1 chưa
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
}
