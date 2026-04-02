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

        rvLessons = findViewById(R.id.rv_grammar_lessons);
        btnBack = findViewById(R.id.btn_back_list);

        btnBack.setOnClickListener(v -> finish());

        initData();
        setupRecyclerView();
    } // <--- CHÍNH CÁI NGOẶC NÀY LÀ KẺ GÂY TỘI LỖI ĐÂY!

    private void initData() {
        lessonList = new ArrayList<>();

        // --- NHÓM HIỆN TẠI ---
        lessonList.add(new GrammarLesson("Present Simple", "Thì Hiện tại đơn", "Present Simple"));
        lessonList.add(new GrammarLesson("Present Continuous", "Thì Hiện tại tiếp diễn", "Present Continuous"));
        lessonList.add(new GrammarLesson("Present Perfect", "Thì Hiện tại hoàn thành", "Present Perfect"));
        lessonList.add(new GrammarLesson("Present Perfect Continuous", "Thì Hiện tại HT tiếp diễn", "Present Perfect Continuous"));

        // --- NHÓM QUÁ KHỨ ---
        lessonList.add(new GrammarLesson("Past Simple", "Thì Quá khứ đơn", "Past Simple"));
        lessonList.add(new GrammarLesson("Past Continuous", "Thì Quá khứ tiếp diễn", "Past Continuous"));
        lessonList.add(new GrammarLesson("Past Perfect", "Thì Quá khứ hoàn thành", "Past Perfect"));
        lessonList.add(new GrammarLesson("Past Perfect Continuous", "Thì Quá khứ HT tiếp diễn", "Past Perfect Continuous"));

        // --- NHÓM TƯƠNG LAI ---
        lessonList.add(new GrammarLesson("Future Simple", "Thì Tương lai đơn", "Future Simple"));
        lessonList.add(new GrammarLesson("Future Continuous", "Thì Tương lai tiếp diễn", "Future Continuous"));
        lessonList.add(new GrammarLesson("Future Perfect", "Thì Tương lai hoàn thành", "Future Perfect"));
        lessonList.add(new GrammarLesson("Future Perfect Continuous", "Thì Tương lai HT tiếp diễn", "Future Perfect Continuous"));
    }

    private void setupRecyclerView() {
        GrammarLessonAdapter adapter = new GrammarLessonAdapter(lessonList, lesson -> {
            Intent intent = new Intent(GrammarListActivity.this, GrammarActivity.class);
            intent.putExtra("CATEGORY_ID", lesson.getCategoryId());
            startActivity(intent);
        });

        rvLessons.setLayoutManager(new LinearLayoutManager(this));
        rvLessons.setAdapter(adapter);
    }
}