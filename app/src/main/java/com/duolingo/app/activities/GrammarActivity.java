package com.duolingo.app.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.GrammarQuestion;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class GrammarActivity extends AppCompatActivity {

    // --- Khai báo View ---
    private ImageView btnBack;
    private ProgressBar progressBar;
    private TextView tvTheoryTitle, tvTheoryContent, tvTheoryStructure, tvTheoryHint;
    private TextView tvStep, tvQuestion;
    private MaterialButton btnA, btnB, btnC, btnD, btnCheckAnswer;

    // --- Biến kiểm soát logic ---
    private List<GrammarQuestion> questionList = new ArrayList<>();
    private int currentIndex = 0;
    private String selectedAnswer = "";
    private boolean isAnswerChecked = false; // Trạng thái: Đã bấm kiểm tra chưa?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);

        initViews();
        setupListeners();
        loadQuestionsFromDB();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progress_bar);

        // Header Lý thuyết
        tvTheoryContent = findViewById(R.id.tv_theory_content); // Em nhớ đặt ID cho các TextView trong thẻ CardView ở file XML nhé

        // Nội dung câu hỏi
        tvStep = findViewById(R.id.tv_step);
        tvQuestion = findViewById(R.id.tv_question);

        // 4 Đáp án & Nút Check
        btnA = findViewById(R.id.btn_a);
        btnB = findViewById(R.id.btn_b);
        btnC = findViewById(R.id.btn_c);
        btnD = findViewById(R.id.btn_d);
        btnCheckAnswer = findViewById(R.id.btn_check_answer);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnA.setOnClickListener(v -> selectOption(btnA));
        btnB.setOnClickListener(v -> selectOption(btnB));
        btnC.setOnClickListener(v -> selectOption(btnC));
        btnD.setOnClickListener(v -> selectOption(btnD));

        btnCheckAnswer.setOnClickListener(v -> {
            if (!isAnswerChecked) {
                checkAnswer(); // Lần bấm 1: Kiểm tra đúng sai
            } else {
                nextQuestion(); // Lần bấm 2: Chuyển câu tiếp theo
            }
        });
    }

    // Trong GrammarActivity.java
    private void loadQuestionsFromDB() {
        // Hứng dữ liệu từ GrammarListActivity gửi qua
        String category = getIntent().getStringExtra("CATEGORY_ID");
        if (category == null) category = "Present Simple"; // Phòng hờ lỗi thì lấy bài đầu tiên

        final String finalCategory = category;
        VocaVerseDatabase db = VocaVerseDatabase.getDatabase(this);
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            // Truy vấn câu hỏi theo đúng cái Category đã chọn
            questionList = db.grammarDao().getQuestionsByCategory(finalCategory);

            runOnUiThread(() -> {
                if (questionList != null && !questionList.isEmpty()) {
                    progressBar.setMax(questionList.size());
                    displayQuestion(currentIndex);
                } else {
                    Toast.makeText(this, "Chưa có dữ liệu bài: " + finalCategory, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void displayQuestion(int index) {
        // Reset trạng thái
        isAnswerChecked = false;
        selectedAnswer = "";
        btnCheckAnswer.setText("KIỂM TRA ĐÁP ÁN");
        btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4D0FF"))); // Trở về màu gốc
        resetButtonStyles();

        GrammarQuestion q = questionList.get(index);

        // Đổ dữ liệu lên UI
        // (Nếu XML của em chưa có ID cho phần lý thuyết, em tạm comment dòng này lại để test câu hỏi trước nhé)
        // tvTheoryContent.setText(q.getTheoryContent());

        tvStep.setText("Câu " + (index + 1) + " / " + questionList.size());
        tvQuestion.setText(q.getQuestionText());

        btnA.setText(q.getOptionA());
        btnB.setText(q.getOptionB());
        btnC.setText(q.getOptionC());
        btnD.setText(q.getOptionD());

        // Update Progress Bar
        progressBar.setProgress(index);
    }

    // --- Logic chọn 1 đáp án ---
    private void selectOption(MaterialButton selectedBtn) {
        if (isAnswerChecked) return; // Đã check đáp án rồi thì không cho chọn lại

        resetButtonStyles(); // Xóa viền các nút khác

        // Đổi màu viền và nền nút được chọn thành màu xanh nhạt
        selectedBtn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#2196F3")));
        selectedBtn.setStrokeWidth(4);
        selectedBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E3F2FD")));

        selectedAnswer = selectedBtn.getText().toString();
    }

    private void resetButtonStyles() {
        MaterialButton[] buttons = {btnA, btnB, btnC, btnD};
        for (MaterialButton btn : buttons) {
            btn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
            btn.setStrokeWidth(2);
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE)); // Nền trắng
        }
    }

    // --- Logic Kiểm tra đúng sai ---
    private void checkAnswer() {
        if (selectedAnswer.isEmpty()) {
            Toast.makeText(this, "Em chưa chọn đáp án nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        GrammarQuestion currentQ = questionList.get(currentIndex);
        isAnswerChecked = true; // Chuyển trạng thái

        if (selectedAnswer.equals(currentQ.getCorrectAnswer())) {
            // Đúng: Đổi nút Check thành màu xanh lá
            btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            btnCheckAnswer.setText("CHÍNH XÁC - CÂU TIẾP THEO");
        } else {
            // Sai: Đổi nút Check thành màu đỏ
            btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            btnCheckAnswer.setText("SAI RỒI - CÂU TIẾP THEO");
            // Highlight đáp án đúng để học viên biết (Tuỳ chọn bổ sung)
            highlightCorrectAnswer(currentQ.getCorrectAnswer());
        }
    }

    private void highlightCorrectAnswer(String correctStr) {
        MaterialButton[] buttons = {btnA, btnB, btnC, btnD};
        for (MaterialButton btn : buttons) {
            if (btn.getText().toString().equals(correctStr)) {
                btn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                btn.setStrokeWidth(4);
                btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E8F5E9")));
            }
        }
    }

    // --- Logic chuyển câu ---
    private void nextQuestion() {
        currentIndex++;
        if (currentIndex < questionList.size()) {
            displayQuestion(currentIndex);
        } else {
            progressBar.setProgress(questionList.size()); // Full cây
            Toast.makeText(this, "Chúc mừng em đã hoàn thành bài học!", Toast.LENGTH_LONG).show();
            // Ở đây em có thể chuyển sang màn hình Report (Báo cáo điểm)
            finish();
        }
    }
}