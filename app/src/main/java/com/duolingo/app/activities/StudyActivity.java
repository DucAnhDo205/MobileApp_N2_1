package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.persistence.VocaVerseDatabase;

import java.util.ArrayList;
import java.util.List;

public class StudyActivity extends AppCompatActivity {

    private ListView listViewCategories;
    private TextView textTitle;
    private String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        listViewCategories = findViewById(R.id.list_view_categories);
        textTitle = findViewById(R.id.text_study_title);

        // Lấy ngôn ngữ người dùng đã chọn (ví dụ: "Tiếng Anh")
        selectedLanguage = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE)
                .getString("selected_language", "Tiếng Anh");
        
        textTitle.setText("Bài học " + selectedLanguage);

        loadCategories();
    }

    private void loadCategories() {
        VocaVerseDatabase database = VocaVerseDatabase.getDatabase(this);
        // Tự động lấy tất cả các chủ đề đang có trong cơ sở dữ liệu
        database.vocabularyDao().getCategories(selectedLanguage).observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                        android.R.layout.simple_list_item_1, categories);
                listViewCategories.setAdapter(adapter);

                listViewCategories.setOnItemClickListener((parent, view, position, id) -> {
                    String category = categories.get(position);
                    // Khi bấm vào một bài học (ví dụ "Động vật"), sẽ chuyển sang màn hình Flashcard
                    Intent intent = new Intent(StudyActivity.this, FlashcardActivity.class);
                    intent.putExtra("CATEGORY", category);
                    startActivity(intent);
                });
            }
        });
    }
}
