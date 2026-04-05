package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
// ... các import cũ ...
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.adapter.PronunciationAdapter;
import com.duolingo.app.models.PronunciationLesson;
import java.util.ArrayList;
import java.util.List;

public class PronunciationListActivity extends AppCompatActivity {

    private RecyclerView rvLessons;
    private PronunciationAdapter adapter;
    private List<PronunciationLesson> lessonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronunciation_list);

        rvLessons = findViewById(R.id.rvPronunciationLevels);

        // 1. Tạo dữ liệu giả để test (Sau này em sẽ lấy từ Database giống như Grammar)
        lessonList = new ArrayList<>();
        lessonList.add(new PronunciationLesson("1", "Chào hỏi cơ bản", "Dễ", 0));
        lessonList.add(new PronunciationLesson("2", "Giới thiệu bản thân", "Dễ", 0));
        lessonList.add(new PronunciationLesson("3", "Chủ đề Du lịch", "Trung bình", 0));

        // 2. Thiết lập Adapter
        // Xóa cái Toast cũ đi, thay bằng đoạn này:
        adapter = new PronunciationAdapter(lessonList, lesson -> {
            Intent intent = new Intent(PronunciationListActivity.this, PronunciationActivity.class);
            // Có thể truyền thêm ID bài học sang để biết đường móc database
            intent.putExtra("LESSON_ID", lesson.getId());
            startActivity(intent);
        });
        rvLessons.setLayoutManager(new LinearLayoutManager(this));
        rvLessons.setAdapter(adapter);
    }
}