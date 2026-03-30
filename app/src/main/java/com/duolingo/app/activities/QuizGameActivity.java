package com.duolingo.app.activities;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
import com.duolingo.app.models.GameHistory;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizGameActivity extends AppCompatActivity {
    private TextView tvQuestion, tvProgressText;
    private MaterialButton[] optionButtons = new MaterialButton[4];
    private VocabularyItem correctAnswer;
    private int currentQuestion = 1;
    private int score = 0;
    private boolean isProcessing = false;
    private VocaVerseDatabase database;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);

        initViews();
        setupOnBackPressed();
        loadNewQuiz();
    }

    private void initViews() {
        database = VocaVerseDatabase.getDatabase(this);
        tvQuestion = findViewById(R.id.tv_quiz_question);
        tvProgressText = findViewById(R.id.tv_progress_text);
        progressBar = findViewById(R.id.pb_game_progress);

        optionButtons[0] = findViewById(R.id.btn_option_1);
        optionButtons[1] = findViewById(R.id.btn_option_2);
        optionButtons[2] = findViewById(R.id.btn_option_3);
        optionButtons[3] = findViewById(R.id.btn_option_4);

        TextView tvTitle = findViewById(R.id.tv_game_title);
        if (tvTitle != null) tvTitle.setText("Trắc nghiệm");

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        if (progressBar != null) {
            progressBar.setMax(10);
            progressBar.setProgress(1);
        }
    }

    private void setupOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void loadNewQuiz() {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            List<VocabularyItem> randomList = database.vocabularyDao().getRandomVocabulary(4);
            if (randomList.size() < 4) return;

            correctAnswer = randomList.get(0);
            List<VocabularyItem> options = new ArrayList<>(randomList);
            Collections.shuffle(options);

            runOnUiThread(() -> {
                tvQuestion.setText(correctAnswer.getWord());
                updateProgressUI();
                for (int i = 0; i < 4; i++) {
                    VocabularyItem item = options.get(i);
                    optionButtons[i].setText(item.getMeaning());
                    resetButtonStyles(optionButtons[i]);
                    optionButtons[i].setOnClickListener(v -> checkAnswer(item, (MaterialButton) v));
                }
                isProcessing = false;
            });
        });
    }

    private void checkAnswer(VocabularyItem selected, MaterialButton btn) {
        if (isProcessing) return;
        isProcessing = true;

        if (selected.getWord().equals(correctAnswer.getWord())) {
            btn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            btn.setTextColor(Color.parseColor("#4CAF50"));
            score += 10;
        } else {
            btn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#F44336")));
            btn.setTextColor(Color.parseColor("#F44336"));
            showCorrectAnswer();
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (currentQuestion < 10) {
                currentQuestion++;
                loadNewQuiz();
            } else {
                saveFinalScore();
            }
        }, 1000);
    }

    private void showCorrectAnswer() {
        for (MaterialButton b : optionButtons) {
            if (b.getText().toString().equals(correctAnswer.getMeaning())) {
                b.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                b.setTextColor(Color.parseColor("#4CAF50"));
            }
        }
    }

    private void resetButtonStyles(MaterialButton btn) {
        btn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#D1D1D1")));
        btn.setTextColor(Color.parseColor("#424242"));
        btn.setEnabled(true);
    }

    private void updateProgressUI() {
        if (progressBar != null) progressBar.setProgress(currentQuestion, true);
        if (tvProgressText != null) tvProgressText.setText(currentQuestion + "/10");
    }

    private void saveFinalScore() {
        int userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", -1);
        GameHistory history = new GameHistory(userId, "Trắc nghiệm", score, System.currentTimeMillis());

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            database.progressDao().insertGameHistory(history);
            runOnUiThread(() -> showGameOverDialog(score));
        });
    }

    private void showGameOverDialog(int finalScore) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_over);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);

        TextView tvScore = dialog.findViewById(R.id.tv_score_result);
        if (tvScore != null) tvScore.setText("Bạn đã đạt được " + finalScore + " sao!");

        dialog.findViewById(R.id.btn_play_again).setOnClickListener(v -> {
            dialog.dismiss();
            resetQuizGame();
        });

        dialog.findViewById(R.id.btn_exit_game).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void resetQuizGame() {
        currentQuestion = 1;
        score = 0;
        loadNewQuiz();
    }
}