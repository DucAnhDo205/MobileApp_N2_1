package com.duolingo.app.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.GrammarQuestion;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private LinearLayout layoutQuestionContainer;
    private MaterialButton btnSubmitExam;

    private List<GrammarQuestion> practiceList = new ArrayList<>();

    // Mảng lưu trữ đáp án người dùng chọn cho 20 câu
    private String[] userAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_practice);

        initViews();
        loadPracticeData();
    }

    private void initViews() {
        findViewById(R.id.btn_back_practice).setOnClickListener(v -> finish());
        progressBar = findViewById(R.id.progress_bar_practice);
        layoutQuestionContainer = findViewById(R.id.layout_question_container);
        btnSubmitExam = findViewById(R.id.btn_submit_exam);

        // Bấm nộp bài -> Hiện cảnh báo
        btnSubmitExam.setOnClickListener(v -> showSubmitConfirmDialog());
    }

    private void loadPracticeData() {
        String category = getIntent().getStringExtra("CATEGORY_ID");
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            List<GrammarQuestion> allQs = VocaVerseDatabase.getDatabase(this).grammarDao().getQuestionsByCategory(category);
            runOnUiThread(() -> {
                if (allQs != null && !allQs.isEmpty()) {
                    Collections.shuffle(allQs);
                    // Lấy 20 câu
                    practiceList = allQs.size() > 20 ? allQs.subList(0, 20) : allQs;

                    // Khởi tạo mảng lưu đáp án (ban đầu là rỗng)
                    userAnswers = new String[practiceList.size()];
                    for(int i=0; i<userAnswers.length; i++) userAnswers[i] = "";

                    progressBar.setMax(practiceList.size());
                    progressBar.setProgress(0);

                    // Gọi hàm tự động sinh giao diện
                    buildQuestionList();
                }
            });
        });
    }

    private void buildQuestionList() {
        layoutQuestionContainer.removeAllViews(); // Xóa sạch rác cũ nếu có

        // Vòng lặp đẻ ra 20 câu hỏi
        for (int i = 0; i < practiceList.size(); i++) {
            final int questionIndex = i; // Lưu lại vị trí câu hỏi
            GrammarQuestion q = practiceList.get(i);

            // Bơm cái khuôn item_practice_question vào bộ nhớ
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_practice_question, layoutQuestionContainer, false);

            // Ánh xạ các thành phần BÊN TRONG cái khuôn đó
            TextView tvStep = itemView.findViewById(R.id.tv_item_step);
            TextView tvQuestion = itemView.findViewById(R.id.tv_item_question);
            MaterialButton btnA = itemView.findViewById(R.id.btn_item_a);
            MaterialButton btnB = itemView.findViewById(R.id.btn_item_b);
            MaterialButton btnC = itemView.findViewById(R.id.btn_item_c);
            MaterialButton btnD = itemView.findViewById(R.id.btn_item_d);

            // Đổ dữ liệu
            tvStep.setText("Câu " + (i + 1));
            tvQuestion.setText(q.getQuestionText().replace("\\n", "\n"));
            btnA.setText(q.getOptionA());
            btnB.setText(q.getOptionB());
            btnC.setText(q.getOptionC());
            btnD.setText(q.getOptionD());

            // Xử lý sự kiện click cho 4 nút trong câu hỏi này
            MaterialButton[] buttons = {btnA, btnB, btnC, btnD};
            for (MaterialButton btn : buttons) {
                btn.setOnClickListener(v -> {
                    // 1. Reset màu 4 nút của câu này về trắng
                    for (MaterialButton b : buttons) {
                        b.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
                        b.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    }

                    // 2. Tô màu cam nút được chọn
                    btn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF5722")));
                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FBE9E7")));

                    // 3. Ghi chép lại đáp án vào mảng
                    userAnswers[questionIndex] = btn.getText().toString().toLowerCase();

                    // 4. Tính toán xem đã làm được bao nhiêu câu để tăng Progress Bar
                    updateProgressBar();
                });
            }

            // Gắn cái khuôn đã bơm đầy dữ liệu vào màn hình chính
            layoutQuestionContainer.addView(itemView);
        }
    }

    private void updateProgressBar() {
        int count = 0;
        for (String ans : userAnswers) {
            if (!ans.isEmpty()) count++; // Đếm số câu đã chọn đáp án
        }
        progressBar.setProgress(count);
    }

    private void showSubmitConfirmDialog() {
        // Cảnh báo nếu chưa làm hết
        int answeredCount = 0;
        for (String ans : userAnswers) {
            if (!ans.isEmpty()) answeredCount++;
        }

        String message = "Bạn có chắc chắn muốn nộp bài?";
        if (answeredCount < practiceList.size()) {
            message = "Bạn mới làm được " + answeredCount + "/" + practiceList.size() + " câu. Vẫn còn câu trống kìa. Chắc chắn muốn nộp sớm không?";
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("XÁC NHẬN NỘP BÀI")
                .setMessage(message)
                .setPositiveButton("CHẮC CHẮN", (dialog, which) -> calculateAndShowScore())
                .setNegativeButton("QUAY LẠI", (dialog, which) -> dialog.dismiss()) // Đóng popup, làm tiếp
                .show();
    }

    private void calculateAndShowScore() {
        int finalScore = 0;
        for (int i = 0; i < practiceList.size(); i++) {
            String correctAns = practiceList.get(i).getCorrectAnswer().toLowerCase().trim();
            String userAns = userAnswers[i].trim();
            if (userAns.equalsIgnoreCase(correctAns)) {
                finalScore++;
            }
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("KẾT QUẢ BÀI THI 🏆")
                .setMessage("Điểm số của bạn: " + finalScore + " / " + practiceList.size() + "\nTuyệt vời!")
                .setCancelable(false)
                .setPositiveButton("VỀ DANH SÁCH", (dialog, which) -> finish()) // Nộp xong đá về danh sách
                .show();
    }
}