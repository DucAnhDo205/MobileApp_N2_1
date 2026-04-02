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
import java.util.Collections;
import java.util.List;

public class GrammarActivity extends AppCompatActivity {

    // --- Khai báo View ---
    private ImageView btnBack;
    private ProgressBar progressBar;

    // Biến cho phần Lý thuyết
    private LinearLayout layoutTheoryHeader, layoutTheoryContent;
    private ImageView ivTheoryArrow;
    private TextView tvTheoryTitle, tvTheoryContent, tvTheoryStructure, tvTheoryHint;
    private boolean isTheoryExpanded = false; // Trạng thái mở mặc định

    // Biến cho khung phản hồi đáy
    private LinearLayout layoutFeedbackBottom, layoutFeedbackMessage;
    private ImageView ivFeedbackIcon;
    private TextView tvFeedbackTitle, tvFeedbackSubtitle;

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

        // Ánh xạ View của Khung phản hồi đáy
        layoutFeedbackBottom = findViewById(R.id.layout_feedback_bottom);
        layoutFeedbackMessage = findViewById(R.id.layout_feedback_message);
        ivFeedbackIcon = findViewById(R.id.iv_feedback_icon);
        tvFeedbackTitle = findViewById(R.id.tv_feedback_title);
        tvFeedbackSubtitle = findViewById(R.id.tv_feedback_subtitle);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Logic Đóng/Mở Lý thuyết
        // Logic Đóng/Mở Lý thuyết chuẩn xác
        layoutTheoryHeader.setOnClickListener(v -> {
            isTheoryExpanded = !isTheoryExpanded; // Đảo trạng thái

            if (isTheoryExpanded) {
                // KHI MỞ: Hiện nội dung, xoay mũi tên ngược lên (^)
                layoutTheoryContent.setVisibility(View.VISIBLE);
                ivTheoryArrow.setRotation(0f);
            } else {
                // KHI ĐÓNG: Giấu nội dung, mũi tên chĩa xuống (v)
                layoutTheoryContent.setVisibility(View.GONE);
                ivTheoryArrow.setRotation(180f);
            }
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
                    // 1. Trộn ngẫu nhiên danh sách câu hỏi
                    Collections.shuffle(questionList);

                    // 2. Cắt lấy đúng 15 câu đầu tiên
                    if (questionList.size() > 15) {
                        questionList = questionList.subList(0, 15);
                    }

                    progressBar.setMax(questionList.size()); // Giờ max sẽ là 15

                    // Nạp lý thuyết (Giữ nguyên)
                    GrammarQuestion firstQ = questionList.get(0);
                    tvTheoryTitle.setText("LÝ THUYẾT\n" + firstQ.getTheoryTitle());
                    tvTheoryContent.setText(firstQ.getTheoryContent());
                    tvTheoryStructure.setText("Cấu trúc: " + firstQ.getTheoryStructure());
                    tvTheoryHint.setText("Dấu hiệu: " + firstQ.getTheoryHint());

                    displayQuestion(currentIndex);
                } else {
                    Toast.makeText(this, "Chưa có dữ liệu cho bài: " + finalCategory, Toast.LENGTH_SHORT).show();
                    // Để mở phần comment finish() nếu em muốn nó tự văng ra khi trống data
                    // finish();
                }
            });
        });
    }

    private void displayQuestion(int index) {
        // Giấu khung phản hồi đi, đưa về trạng thái trong suốt
        layoutFeedbackBottom.setBackgroundColor(Color.TRANSPARENT);
        layoutFeedbackMessage.setVisibility(View.GONE);
        tvFeedbackSubtitle.setVisibility(View.GONE);

        // Reset trạng thái máy
        isAnswerChecked = false;
        selectedAnswer = "";
        btnCheckAnswer.setText("KIỂM TRA ĐÁP ÁN");
        btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4D0FF")));
        btnCheckAnswer.setEnabled(false);
        resetButtonStyles();

        GrammarQuestion q = questionList.get(index);

        // Update Text
        tvStep.setText("Câu " + (index + 1) + " / " + questionList.size());
        tvQuestion.setText(q.getQuestionText().replace("\\n", "\n"));

        // Gán đáp án
        btnA.setText(q.getOptionA());
        btnB.setText(q.getOptionB());
        btnC.setText(q.getOptionC());
        btnD.setText(q.getOptionD());

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

        // MỚI THÊM: Đổi nút Kiểm tra sang màu Xanh đậm và MỞ KHÓA
        btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3B82F6")));
        btnCheckAnswer.setEnabled(true);
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
        // Chặn lỗi người dùng bấm Kiểm tra khi chưa chọn đáp án
        if (selectedAnswer.isEmpty()) {
            Toast.makeText(this, "Em chưa chọn đáp án nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        GrammarQuestion currentQ = questionList.get(currentIndex);
        isAnswerChecked = true;

        if (selectedAnswer.trim().equalsIgnoreCase(currentQ.getCorrectAnswer().trim())) {
            // KHI ĐÚNG
            layoutFeedbackBottom.setBackgroundColor(Color.parseColor("#E8F5E9")); // Nền xanh ngọc nhạt
            layoutFeedbackMessage.setVisibility(View.VISIBLE);

            ivFeedbackIcon.setImageResource(R.drawable.ic_check); // Đổi thành dấu tick
            ivFeedbackIcon.setColorFilter(Color.parseColor("#4CAF50")); // Tick màu xanh lá

            tvFeedbackTitle.setText("Chính xác!");
            tvFeedbackTitle.setTextColor(Color.parseColor("#4CAF50"));
            tvFeedbackSubtitle.setVisibility(View.GONE);

            btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            btnCheckAnswer.setText("TIẾP TỤC");
        } else {
            // KHI SAI
            layoutFeedbackBottom.setBackgroundColor(Color.parseColor("#FFEBEE")); // Nền đỏ hồng nhạt
            layoutFeedbackMessage.setVisibility(View.VISIBLE);

            ivFeedbackIcon.setImageResource(R.drawable.ic_close); // Đổi thành dấu X
            ivFeedbackIcon.setColorFilter(Color.parseColor("#F44336")); // X màu đỏ

            tvFeedbackTitle.setText("Sai mất rồi!");
            tvFeedbackTitle.setTextColor(Color.parseColor("#F44336"));

            tvFeedbackSubtitle.setVisibility(View.VISIBLE);
            tvFeedbackSubtitle.setText("Đáp án đúng: " + currentQ.getCorrectAnswer().toUpperCase());
            tvFeedbackSubtitle.setTextColor(Color.parseColor("#F44336"));

            btnCheckAnswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            btnCheckAnswer.setText("TIẾP TỤC");

            highlightCorrectAnswer(currentQ.getCorrectAnswer()); // Vẫn tô viền ô đúng
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
            Toast.makeText(this, "Chúc mừng em đã hoàn thành chủ đề này xuất sắc!", Toast.LENGTH_LONG).show();
            finish(); // Đóng màn hình, quay về danh sách
        }
    }
}