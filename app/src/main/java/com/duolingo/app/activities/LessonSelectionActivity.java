package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.duolingo.app.R;
import com.duolingo.app.viewmodels.FlashcardViewModel;
import java.util.List;

public class LessonSelectionActivity extends AppCompatActivity {

    private FlashcardViewModel flashcardViewModel;
    private ListView listView;
    private String language = "Tiếng Anh"; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_selection);

        listView = findViewById(R.id.list_view_lessons);
        flashcardViewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);

        flashcardViewModel.getLessonNumbers(language).observe(this, lessons -> {
            if (lessons != null) {
                LessonAdapter adapter = new LessonAdapter(lessons);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    int selectedLesson = lessons.get(position);
                    Intent intent = new Intent(LessonSelectionActivity.this, FlashcardActivity.class);
                    intent.putExtra("lesson_number", selectedLesson);
                    intent.putExtra("language", language);
                    startActivity(intent);
                });
            }
        });
    }

    private class LessonAdapter extends ArrayAdapter<Integer> {
        public LessonAdapter(List<Integer> lessons) {
            super(LessonSelectionActivity.this, R.layout.item_lesson, lessons);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lesson, parent, false);
            }
            Integer lessonNum = getItem(position);
            TextView textView = convertView.findViewById(R.id.text_lesson_name);
            textView.setText("Bài học " + lessonNum);
            return convertView;
        }
    }
}
