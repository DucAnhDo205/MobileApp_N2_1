package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.adapter.GrammarLessonAdapter;
import com.duolingo.app.models.GrammarLesson;

import java.util.ArrayList;
import java.util.List;

public class GrammarListActivity extends AppCompatActivity {

    private RecyclerView rvLessons;
    private ImageView btnBack;
    private List<GrammarLesson> lessonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_list);

        // 1. Ánh xạ View
        rvLessons = findViewById(R.id.rv_grammar_lessons);
        btnBack = findViewById(R.id.btn_back_list);

        // 2. Nút quay lại màn hình chính
        btnBack.setOnClickListener(v -> finish());

        // 3. Khởi tạo dữ liệu mẫu (Mock Data)
        initData();

        // 4. Thiết lập danh sách hiển thị
        setupRecyclerView();
    }

    private void initData() {
        lessonList = new ArrayList<>();
        // LƯU Ý: Cái ID (Tham số đầu tiên) phải khớp 100% với cột 'category' trong file CSV nhé!
        lessonList.add(new GrammarLesson("Present Simple", "Thì Hiện tại đơn", "Present Simple"));
        lessonList.add(new GrammarLesson("Present Continuous", "Thì Hiện tại tiếp diễn", "Present Continuous"));
        lessonList.add(new GrammarLesson("Past Simple", "Thì Quá khứ đơn", "Past Simple"));
        lessonList.add(new GrammarLesson("Past Continuous", "Thì Quá khứ tiếp diễn", "Past Continuous"));
        lessonList.add(new GrammarLesson("Present Perfect", "Thì Hiện tại hoàn thành", "Present Perfect"));
        lessonList.add(new GrammarLesson("Future Simple", "Thì Tương lai đơn", "Future Simple"));
    }

    private void setupRecyclerView() {
        // Khởi tạo Adapter và bắt sự kiện Click
        GrammarLessonAdapter adapter = new GrammarLessonAdapter(lessonList, lesson -> {
            // MẤU CHỐT: Bấm vào bài nào, gửi ID bài đó sang GrammarActivity
            Intent intent = new Intent(GrammarListActivity.this, GrammarActivity.class);
            intent.putExtra("CATEGORY_ID", lesson.getCategoryId());
            startActivity(intent);
        });

        // Gắn Layout kiểu danh sách dọc
        rvLessons.setLayoutManager(new LinearLayoutManager(this));
        rvLessons.setAdapter(adapter);
    }
}