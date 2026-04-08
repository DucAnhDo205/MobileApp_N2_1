package com.duolingo.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.adapter.ExamNavigationAdapter;
import com.duolingo.app.models.ExamQuestion;
import com.duolingo.app.utils.ExamDataRepository;
import com.duolingo.app.utils.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;

public class ComprehensiveExamActivity extends AppCompatActivity {
    private List<ExamQuestion> questions;
    private int currentIndex = 0;
    private ExamNavigationAdapter navAdapter;
    private RadioGroup rgOptions;
    private TextView tvQuestionContent, tvQuestionNumber, tvTimer, tvExplanation;
    private CountDownTimer timer;
    private String difficulty;
    private boolean isReviewMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprehensive_exam);
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        NavigationHelper.setup(this, nav, R.id.nav_test);

        difficulty = getIntent().getStringExtra("DIFFICULTY_LEVEL");
        if (difficulty == null) difficulty = "EASY";

        questions = ExamDataRepository.getQuestions(difficulty);

        initViews();
        setupNavigation();
        displayQuestion(0);
        // -------------------------------------------

        startTimer(ExamDataRepository.getTimeLimit(difficulty) * 60 * 1000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // Cập nhật lại màu icon khi trang được lôi từ dưới lên
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        NavigationHelper.setup(this, nav, R.id.nav_test);
    }

    private void initViews() {
        tvQuestionContent = findViewById(R.id.tv_question_content);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvTimer = findViewById(R.id.tv_timer);
        tvExplanation = findViewById(R.id.tv_explanation);
        rgOptions = findViewById(R.id.rg_options);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_submit).setOnClickListener(v -> {
            if (isReviewMode) finish();
            else showSubmitDialog();
        });
    }

    private void setupNavigation() {
        RecyclerView rv = findViewById(R.id.rv_navigation);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // Khởi tạo navAdapter ở đây để tránh lỗi Null
        navAdapter = new ExamNavigationAdapter(questions, 0, this::displayQuestion);
        rv.setAdapter(navAdapter);
    }

    private void displayQuestion(int index) {
        currentIndex = index;
        ExamQuestion q = questions.get(index);

        tvQuestionNumber.setText("CÂU HỎI " + (index + 1));
        tvQuestionContent.setText(q.getQuestion());

        rgOptions.setOnCheckedChangeListener(null);
        rgOptions.clearCheck();

        for (int i = 0; i < 4; i++) {
            RadioButton rb = (RadioButton) rgOptions.getChildAt(i);
            rb.setText(q.getOptions().get(i));
            rb.setEnabled(!isReviewMode);

            rb.setTextColor(Color.BLACK);
            rb.setBackgroundResource(R.drawable.bg_option_selector);

            if (isReviewMode) {
                if (i == q.getCorrectIndex()) {
                    rb.setBackgroundResource(R.drawable.bg_nav_correct);
                    rb.setTextColor(Color.WHITE);
                } else if (i == q.getUserSelectedAnswer()) {
                    rb.setBackgroundResource(R.drawable.bg_nav_wrong);
                    rb.setTextColor(Color.WHITE);
                }
            }
        }

        if (q.getUserSelectedAnswer() != -1) {
            ((RadioButton) rgOptions.getChildAt(q.getUserSelectedAnswer())).setChecked(true);
        }

        if (isReviewMode) {
            tvExplanation.setVisibility(View.VISIBLE);
            tvExplanation.setText("Giải thích: " + q.getExplanation());
        } else {
            tvExplanation.setVisibility(View.GONE);
            rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
                int ans = group.indexOfChild(findViewById(checkedId));
                questions.get(currentIndex).setUserSelectedAnswer(ans);
                if (navAdapter != null) navAdapter.notifyItemChanged(currentIndex);
            });
        }

        if (navAdapter != null) {
            navAdapter.setCurrentIndex(index);
        }
    }

    private void performNavigation(int itemId) {
        if (itemId == R.id.nav_study) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (itemId == R.id.nav_community) {
            startActivity(new Intent(this, GameMenuActivity.class));
            finish();
        }
    }

    private void showSubmitDialog() {
        int answered = 0;
        for (ExamQuestion q : questions) if (q.getUserSelectedAnswer() != -1) answered++;

        new AlertDialog.Builder(this)
                .setTitle("Nộp bài")
                .setMessage("Bạn đã làm " + answered + "/" + questions.size() + " câu. Bạn có muốn nộp bài không?")
                .setPositiveButton("Nộp bài", (d, w) -> calculateResult())
                .setNegativeButton("Làm tiếp", null)
                .show();
    }

    private void calculateResult() {
        if (timer != null) timer.cancel();
        int score = 0;
        for (ExamQuestion q : questions) {
            if (q.getUserSelectedAnswer() == q.getCorrectIndex()) score++;
        }

        saveProgress(score);

        new AlertDialog.Builder(this)
                .setTitle("Hoàn thành!")
                .setMessage("Điểm của bạn: " + score + "/" + questions.size())
                .setCancelable(false)
                .setPositiveButton("Xem đáp án", (d, w) -> {
                    isReviewMode = true;
                    if (navAdapter != null) {
                        navAdapter.setReviewMode(true);
                        navAdapter.notifyDataSetChanged();
                    }
                    ((Button)findViewById(R.id.btn_submit)).setText("THOÁT");
                    displayQuestion(0);
                })
                .setNegativeButton("Thoát", (d, w) -> finish())
                .show();
    }

    private void saveProgress(int score) {
        SharedPreferences pref = getSharedPreferences("VocaVerse_Exam", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("DONE_" + difficulty, true);
        editor.putInt("SCORE_" + difficulty, score);
        editor.apply();
    }

    private void startTimer(long millis) {
        timer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long l) {
                tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", (l / 1000) / 60, (l / 1000) % 60));
            }
            @Override
            public void onFinish() {
                Toast.makeText(ComprehensiveExamActivity.this, "Hết giờ!", Toast.LENGTH_SHORT).show();
                calculateResult();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}