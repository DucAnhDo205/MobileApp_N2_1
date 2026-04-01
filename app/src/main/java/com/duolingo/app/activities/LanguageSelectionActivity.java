package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.adapter.LanguageAdapter;
import com.duolingo.app.models.Language;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.CSVHelper;
import com.duolingo.app.persistence.VocaVerseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LanguageSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLanguages;
    private LanguageAdapter languageAdapter;
    private List<Language> languageList;
    private Button buttonNext;
    private String selectedLanguageName = "Tiếng Anh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        recyclerViewLanguages = findViewById(R.id.recycler_view_languages);
        buttonNext = findViewById(R.id.button_next);
        
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewLanguages.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewLanguages);

        languageList = new ArrayList<>();
        languageList.add(new Language("Tiếng Anh", R.drawable.flag_of_the_united_kingdom, R.drawable.ic_animal_placeholder));
        languageList.add(new Language("Tiếng Nhật", R.drawable.ic_flag_japan, R.drawable.ic_animal_placeholder));
        languageList.add(new Language("Tiếng Trung", R.drawable.ic_flag_china, R.drawable.ic_animal_placeholder));
        languageList.add(new Language("Tiếng Hàn", R.drawable.ic_flag_korea, R.drawable.ic_animal_placeholder));

        languageAdapter = new LanguageAdapter(languageList, position -> {
            recyclerViewLanguages.smoothScrollToPosition(position);
            selectedLanguageName = languageList.get(position).getName();
            saveSelectedLanguage(languageList.get(position));
        });
        recyclerViewLanguages.setAdapter(languageAdapter);

        recyclerViewLanguages.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerViewLanguages.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = recyclerViewLanguages.getWidth();
                int itemWidth = (int) (260 * getResources().getDisplayMetrics().density);
                int padding = (width - itemWidth) / 2;
                recyclerViewLanguages.setPadding(padding, 0, padding, 0);
                recyclerViewLanguages.scrollToPosition(0);
            }
        });

        buttonNext.setOnClickListener(v -> {
            importLessons();
        });
    }

    private void importLessons() {
        VocaVerseDatabase database = VocaVerseDatabase.getDatabase(this);
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            // Sử dụng file đã lọc study_lessons.csv
            String fileName = "study_lessons.csv"; 
            
            List<VocabularyItem> items = CSVHelper.readVocabularyFromCSV(this, fileName, selectedLanguageName);
            if (!items.isEmpty()) {
                for (VocabularyItem item : items) {
                    database.vocabularyDao().insert(item);
                }
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "Khởi tạo lộ trình học " + selectedLanguageName + " thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LanguageSelectionActivity.this, SetupGoalActivity.class);
                    startActivity(intent);
                });
            } else {
                runOnUiThread(() -> {
                    Intent intent = new Intent(LanguageSelectionActivity.this, SetupGoalActivity.class);
                    startActivity(intent);
                });
            }
        });
    }

    private void saveSelectedLanguage(Language language) {
        getSharedPreferences("VocaVersePrefs", MODE_PRIVATE)
                .edit()
                .putString("selected_language", language.getName())
                .putInt("selected_language_flag", language.getFlagResourceId())
                .apply();
    }
}
