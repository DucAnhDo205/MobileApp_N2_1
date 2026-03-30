package com.duolingo.app.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.adapter.LetterAdapter;
import com.duolingo.app.models.GameHistory;
import com.duolingo.app.models.LetterItem;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScrambleGameActivity extends AppCompatActivity {
    private TextView tvScrambled, tvUserAnswer, tvQuestionCount, tvCorrectCount, tvProgressText;
    private RecyclerView rvLetters;
    private ProgressBar progressBar;
    private LetterAdapter adapter;
    private List<LetterItem> currentLetters = new ArrayList<>();
    private String originalWord = "";
    private StringBuilder userAttempt = new StringBuilder();
    private VocaVerseDatabase database;

    private int currentQuestion = 1;
    private int totalStars = 0;
    private boolean isHintUsed = false;
    private final int TOTAL_QUESTIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scramble_game);

        initViews();
        setupOnBackPressed();
        loadNewQuestion();
    }

    private void initViews() {
        database = VocaVerseDatabase.getDatabase(this);
        tvScrambled = findViewById(R.id.tv_scrambled_word);
        tvUserAnswer = findViewById(R.id.tv_user_answer);
        tvQuestionCount = findViewById(R.id.tv_question_count);
        tvCorrectCount = findViewById(R.id.tv_correct_count);
        tvProgressText = findViewById(R.id.tv_progress_text);
        progressBar = findViewById(R.id.pb_game_progress);
        rvLetters = findViewById(R.id.rv_letters);

        TextView tvHeaderTitle = findViewById(R.id.tv_game_title);
        if (tvHeaderTitle != null) tvHeaderTitle.setText("Sắp xếp chữ");

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_clear).setOnClickListener(v -> clearAnswer());
        findViewById(R.id.btn_check).setOnClickListener(v -> checkAnswer());
        findViewById(R.id.btn_hint).setOnClickListener(v -> showHint());

        if (progressBar != null) progressBar.setMax(TOTAL_QUESTIONS);
    }

    private void setupOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void loadNewQuestion() {
        isHintUsed = false;
        userAttempt.setLength(0);
        updateUIState();

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            List<VocabularyItem> vocabs = database.vocabularyDao().getRandomVocabulary(1);
            if (!vocabs.isEmpty()) {
                originalWord = vocabs.get(0).getWord().toUpperCase();
                List<String> chars = new ArrayList<>();
                for (char c : originalWord.toCharArray()) chars.add(String.valueOf(c));

                List<String> scrambled = new ArrayList<>(chars);
                while (scrambled.size() > 1 && scrambled.equals(chars)) {
                    Collections.shuffle(scrambled);
                }

                currentLetters.clear();
                for (String s : scrambled) currentLetters.add(new LetterItem(s));

                runOnUiThread(() -> {
                    tvScrambled.setText(android.text.TextUtils.join(" ", scrambled));
                    setupRecyclerView();
                });
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new LetterAdapter(currentLetters, position -> {
            LetterItem item = currentLetters.get(position);
            if (!item.isSelected()) {
                item.setSelected(true);
                userAttempt.append(item.getCharacter());
                tvUserAnswer.setText(userAttempt.toString());
                adapter.notifyItemChanged(position);
            }
        });
        rvLetters.setLayoutManager(new GridLayoutManager(this, 5));
        rvLetters.setAdapter(adapter);
    }

    private void checkAnswer() {
        if (userAttempt.toString().equals(originalWord)) {
            int earned = isHintUsed ? 5 : 10;
            totalStars += earned;
            tvCorrectCount.setText("Sao: " + totalStars);
            Toast.makeText(this, "Chính xác! +" + earned + " sao", Toast.LENGTH_SHORT).show();

            if (currentQuestion < TOTAL_QUESTIONS) {
                currentQuestion++;
                loadNewQuestion();
            } else {
                saveGameResults();
            }
        } else {
            Toast.makeText(this, "Chưa đúng, thử lại nhé!", Toast.LENGTH_SHORT).show();
            clearAnswer();
        }
    }

    private void showHint() {
        if (isHintUsed) return;
        isHintUsed = true;

        tvUserAnswer.setText(originalWord);
        tvUserAnswer.setTextColor(Color.parseColor("#FFA000"));
        Toast.makeText(this, "Gợi ý: Điểm câu này sẽ bị giảm!", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            tvUserAnswer.setText(userAttempt.toString());
            tvUserAnswer.setTextColor(Color.parseColor("#3F51B5"));
        }, 2000);
    }

    private void clearAnswer() {
        userAttempt.setLength(0);
        tvUserAnswer.setText("");
        tvUserAnswer.setTextColor(Color.parseColor("#3F51B5"));
        for (LetterItem item : currentLetters) item.setSelected(false);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void updateUIState() {
        tvUserAnswer.setText("");
        tvQuestionCount.setText("Câu " + currentQuestion + "/" + TOTAL_QUESTIONS);
        if (tvProgressText != null) tvProgressText.setText(currentQuestion + "/" + TOTAL_QUESTIONS);
        if (progressBar != null) progressBar.setProgress(currentQuestion, true);
    }

    private void saveGameResults() {
        int userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", -1);
        GameHistory history = new GameHistory(userId, "Sắp xếp chữ", totalStars, System.currentTimeMillis());

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            database.progressDao().insertGameHistory(history);
            runOnUiThread(() -> showGameOverDialog(totalStars));
        });
    }

    private void showGameOverDialog(int finalStars) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_over);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);

        TextView tvScore = dialog.findViewById(R.id.tv_score_result);
        if (tvScore != null) tvScore.setText("Bạn đã nhận được +" + finalStars + " sao");

        dialog.findViewById(R.id.btn_play_again).setOnClickListener(v -> {
            dialog.dismiss();
            resetFullGame();
        });

        dialog.findViewById(R.id.btn_exit_game).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void resetFullGame() {
        currentQuestion = 1;
        totalStars = 0;
        tvCorrectCount.setText("Sao: 0");
        loadNewQuestion();
    }
}