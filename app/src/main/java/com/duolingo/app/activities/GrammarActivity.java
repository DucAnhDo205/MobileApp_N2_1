package com.duolingo.app.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class GrammarActivity extends AppCompatActivity {

    // --- Khai báo View ---
    private ImageView btnBack;
    private ProgressBar progressBar;

    // Lý thuyết (Đóng/Mở)
    private LinearLayout layoutTheoryHeader, layoutTheoryContent;
    private ImageView ivTheoryArrow;
    private TextView tvTheoryTitle, tvTheoryContent, tvTheoryStructure, tvTheoryHint;
    private boolean isTheoryExpanded = true; // Trạng thái mở mặc định

    // Câu hỏi & Đáp án
    private TextView tvStep, tvQuestion;
    private MaterialButton btnA, btnB, btnC, btnD, btnCheckAnswer;

    // --- Biến kiểm soát logic ---
    private List<GrammarQuestion> questionList = new ArrayList<>();
    private int currentIndex = 0;
    private String selectedAnswer = "";
    private boolean isAnswerChecked = false;

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

        // UI Lý thuyết
        layoutTheoryHeader = findViewById(R.id.layout_theory_header);
        layoutTheoryContent = findViewById(R.id.layout_theory_content);
        ivTheoryArrow = findViewById(R.id.iv_theory_arrow);
        tvTheoryTitle = findViewById(R.id.tv_theory_title);
        tvTheoryContent = findViewById(R.id.tv_theory_content);
        tvTheoryStructure = findViewById(R.id.tv_theory_structure);
        tvTheoryHint = findViewById(R.id.tv_theory_hint);

        // UI Câu hỏi
        tvStep = findViewById(R.id.tv_step);
        tvQuestion = findViewById(R.id.tv_question);

        // UI Nút
        btnA = findViewById(R.id.btn_a);
        btnB = findViewById(R.id.btn_b);
        btnC = findViewById(R.id.btn_c);
        btnD = findViewById(R.id.btn_d);
        btnCheckAnswer = findViewById(R.id.btn_check_answer);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Logic Đóng/Mở Lý thuyết
        layoutTheoryHeader.setOnClickListener(v -> {
            isTheoryExpanded = !isTheoryExpanded;
            layoutTheoryContent.setVisibility(isTheoryExpanded ? View.VISIBLE : View.GONE);
            ivTheoryArrow.setRotation(isTheoryExpanded ? 0 : 180); // Xoay mũi tên 180 độ
        });

        // Click chọn đáp án
        btnA.setOnClickListener(v -> selectOption(btnA));
        btnB.setOnClickListener(v -> selectOption(btnB));
        btnC.setOnClickListener(v -> selectOption(btnC));
        btnD.setOnClickListener(v -> selectOption(btnD));

        // Nút Kiểm tra / Tiếp tục
        btnCheckAnswer.setOnClickListener(v -> {
            if (!isAnswerChecked) {
                checkAnswer(); // Lần 1: Chấm điểm
            } else {
                nextQuestion(); // Lần 2: Qua câu
            }
        });
    }

    private void loadQuestionsFromDB() {
        String category = getIntent().getStringExtra("CATEGORY_ID");
        if (category == null || category.isEmpty()) category = "Present Simple";

        final String finalCategory = category;
        VocaVerseDatabase db = VocaVerseDatabase.getDatabase(this);

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            questionList = db.grammarDao().getQuestionsByCategory(finalCategory);

            runOnUiThread(() -> {
                if (questionList != null && !questionList.isEmpty()) {
                    progressBar.setMax(questionList.size());

                    // Nạp lý thuyết
                    GrammarQuestion firstQ = questionList.get(0);
                    tvTheoryTitle.setText("LÝ THUYẾT\n" + firstQ.getTheoryTitle());
                    tvTheoryContent.setText(firstQ.getTheoryContent());
                    tvTheoryStructure.setText("Cấu trúc: " + firstQ.getTheoryStructure());
                    tvTheoryHint.setText("Dấu hiệu: " + firstQ.getTheoryHint());

                    displayQuestion(currentIndex);
                } else {
                    Toast.makeText(this, "Chưa có dữ liệu cho bài: " + finalCategory, Toast.LENGTH_SHORT).show();
                    //finish();
                }
            });
        });
    }

    private void displayQuestion(int index) {
        // Reset trạng thái máy
        isAnswerChecked = false;
        selectedAnswer = "";
        btnCheckAnswer.setText("KIỂM TRA ĐÁP ÁN");
        btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4D0FF")));
        resetButtonStyles();

        GrammarQuestion q = questionList.get(index);

        // Update Text
        tvStep.setText("Câu " + (index + 1) + " / " + questionList.size());
        tvQuestion.setText(q.getQuestionText().replace("\\n", "\n"));

        // Gán đáp án (Lưu ý: in hoa để giống UI của em)
        btnA.setText(q.getOptionA().toUpperCase());
        btnB.setText(q.getOptionB().toUpperCase());
        btnC.setText(q.getOptionC().toUpperCase());
        btnD.setText(q.getOptionD().toUpperCase());

        // Update Thanh tiến trình
        progressBar.setProgress(index + 1);
    }

    private void selectOption(MaterialButton selectedBtn) {
        if (isAnswerChecked) return; // Nếu đã check thì cấm đổi đáp án

        resetButtonStyles();

        // Nổi bật nút được chọn (Viền xanh, nền xanh nhạt)
        selectedBtn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#2196F3")));
        selectedBtn.setStrokeWidth(4);
        selectedBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E3F2FD")));

        // Lưu lại đáp án (chuyển về chữ thường để so sánh với Database)
        selectedAnswer = selectedBtn.getText().toString().toLowerCase();
    }

    private void resetButtonStyles() {
        MaterialButton[] buttons = {btnA, btnB, btnC, btnD};
        for (MaterialButton btn : buttons) {
            btn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
            btn.setStrokeWidth(2);
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        }
    }

    private void checkAnswer() {
        if (selectedAnswer.isEmpty()) {
            Toast.makeText(this, "Hương ơi, chọn một đáp án đã nhé!", Toast.LENGTH_SHORT).show();
            return;
        }

        GrammarQuestion currentQ = questionList.get(currentIndex);
        isAnswerChecked = true; // Chuyển state sang đã check

        // So sánh (dùng equalsIgnoreCase để bỏ qua viết hoa/thường)
        if (selectedAnswer.trim().equalsIgnoreCase(currentQ.getCorrectAnswer().trim())) {
            // ĐÚNG: Nút check thành màu Xanh lá
            btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            btnCheckAnswer.setText("CHÍNH XÁC - TIẾP TỤC");
        } else {
            // SAI: Nút check thành màu Đỏ
            btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            btnCheckAnswer.setText("SAI RỒI - TIẾP TỤC");
            // Gọi hàm tô viền xanh lá cho đáp án đúng để học viên biết
            highlightCorrectAnswer(currentQ.getCorrectAnswer());
        }
    }

    private void highlightCorrectAnswer(String correctStr) {
        MaterialButton[] buttons = {btnA, btnB, btnC, btnD};
        for (MaterialButton btn : buttons) {
            // So sánh kĩ chữ hoa/thường
            if (btn.getText().toString().trim().equalsIgnoreCase(correctStr.trim())) {
                btn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                btn.setStrokeWidth(5);
                btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E8F5E9"))); // Xanh lá nhạt
            }
        }
    }

    private void nextQuestion() {
        currentIndex++;
        if (currentIndex < questionList.size()) {
            // Sang câu tiếp theo
            displayQuestion(currentIndex);
        } else {
            // Hết bài
            progressBar.setProgress(questionList.size());
            Toast.makeText(this, "Chúc mừng Hương! Em đã hoàn thành chủ đề này xuất sắc!", Toast.LENGTH_LONG).show();
            finish(); // Đóng màn hình, quay về danh sách
        }
    }
}