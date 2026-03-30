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
import com.duolingo.app.adapter.MemoryAdapter;
import com.duolingo.app.models.GameHistory;
import com.duolingo.app.models.MemoryItem;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGameActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MemoryAdapter adapter;
    private List<MemoryItem> itemList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView tvProgressText;
    private VocaVerseDatabase database;

    private int firstPos = -1;
    private int matchedCount = 0;
    private boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        initViews();
        setupOnBackPressed();
        loadData();
    }

    private void initViews() {
        database = VocaVerseDatabase.getDatabase(this);
        recyclerView = findViewById(R.id.rv_memory);
        progressBar = findViewById(R.id.pb_game_progress);
        tvProgressText = findViewById(R.id.tv_progress_text);

        TextView tvTitle = findViewById(R.id.tv_game_title);
        if (tvTitle != null) tvTitle.setText("Lật thẻ nhớ");

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        if (progressBar != null) progressBar.setMax(6);
    }

    private void setupOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void loadData() {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            List<VocabularyItem> vocabs = database.vocabularyDao().getRandomVocabulary(6);
            if (vocabs.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(this, "Chưa có dữ liệu từ vựng!", Toast.LENGTH_SHORT).show());
                return;
            }

            itemList.clear();
            for (VocabularyItem v : vocabs) {
                itemList.add(new MemoryItem(v.getWord(), v.getWord(), true));
                itemList.add(new MemoryItem(v.getMeaning(), v.getWord(), false));
            }
            Collections.shuffle(itemList);

            runOnUiThread(() -> {
                adapter = new MemoryAdapter(itemList, this::handleCardClick);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    private void handleCardClick(int position) {
        if (isBusy) return;

        MemoryItem current = itemList.get(position);
        if (current.isFaceUp() || current.isMatched()) return;

        current.setFaceUp(true);
        adapter.notifyItemChanged(position);

        if (firstPos == -1) {
            firstPos = position;
        } else {
            isBusy = true;
            MemoryItem first = itemList.get(firstPos);
            final int p1 = firstPos;
            final int p2 = position;

            if (first.getOriginalWord().equals(current.getOriginalWord()) && first.isEnglish() != current.isEnglish()) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    handleMatchSuccess(first, current, p1, p2);
                    isBusy = false;
                }, 500);
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    handleMatchFailure(first, current, p1, p2);
                    isBusy = false;
                }, 1000);
            }
            firstPos = -1;
        }
    }

    private void handleMatchSuccess(MemoryItem first, MemoryItem second, int p1, int p2) {
        first.setMatched(true);
        second.setMatched(true);
        matchedCount++;

        if (progressBar != null) progressBar.setProgress(matchedCount, true);
        if (tvProgressText != null) tvProgressText.setText(matchedCount + "/6");

        adapter.notifyItemChanged(p1);
        adapter.notifyItemChanged(p2);

        if (matchedCount == 6) saveProgress();
    }

    private void handleMatchFailure(MemoryItem first, MemoryItem second, int p1, int p2) {
        first.setFaceUp(false);
        second.setFaceUp(false);
        adapter.notifyItemChanged(p1);
        adapter.notifyItemChanged(p2);
    }

    private void saveProgress() {
        int userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", -1);
        GameHistory history = new GameHistory(userId, "Lật thẻ nhớ", 20, System.currentTimeMillis());

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            database.progressDao().insertGameHistory(history);
            runOnUiThread(() -> showGameOverDialog(20));
        });
    }

    private void showGameOverDialog(int earnedStars) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_over);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);

        TextView tvScore = dialog.findViewById(R.id.tv_score_result);
        if (tvScore != null) tvScore.setText("Bạn đã nhận được +" + earnedStars + " sao!");

        MaterialButton btnPlayAgain = dialog.findViewById(R.id.btn_play_again);
        MaterialButton btnExit = dialog.findViewById(R.id.btn_exit_game);

        btnPlayAgain.setOnClickListener(v -> {
            dialog.dismiss();
            resetGame();
        });

        btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void resetGame() {
        matchedCount = 0;
        firstPos = -1;
        isBusy = false;
        if (progressBar != null) progressBar.setProgress(0);
        if (tvProgressText != null) tvProgressText.setText("0/6");
        loadData();
    }
}