package com.duolingo.app.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.adapter.MatchAdapter;
import com.duolingo.app.models.GameHistory;
import com.duolingo.app.models.MatchItem;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GhepTuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private List<MatchItem> itemList = new ArrayList<>();
    private VocaVerseDatabase database;
    private ProgressBar progressBar;
    private TextView tvProgressText, tvTitle;
    private int firstPos = -1;
    private int matchedCount = 0;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghep_tu);

        initViews();
        setupOnBackPressed();
        loadData();
    }

    private void initViews() {
        database = VocaVerseDatabase.getDatabase(this);
        recyclerView = findViewById(R.id.rv_match);
        progressBar = findViewById(R.id.pb_game_progress);
        tvProgressText = findViewById(R.id.tv_progress_text);
        tvTitle = findViewById(R.id.tv_game_title);

        if (tvTitle != null) tvTitle.setText("Ghép cặp từ vựng");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        progressBar.setMax(6);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
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
            itemList.clear();
            for (VocabularyItem v : vocabs) {
                itemList.add(new MatchItem(v.getWord(), v.getWord(), true));
                itemList.add(new MatchItem(v.getMeaning(), v.getWord(), false));
            }
            Collections.shuffle(itemList);

            runOnUiThread(() -> {
                adapter = new MatchAdapter(itemList, this::handleLogic);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    private void handleLogic(int position) {
        if (isProcessing) return;

        MatchItem current = itemList.get(position);
        if (current.isMatched() || position == firstPos) return;

        current.setSelected(true);
        adapter.notifyItemChanged(position);

        if (firstPos == -1) {
            firstPos = position;
        } else {
            isProcessing = true;
            MatchItem first = itemList.get(firstPos);
            final int p1 = firstPos;
            final int p2 = position;

            if (first.getOriginalWord().equals(current.getOriginalWord()) && first.isEnglish() != current.isEnglish()) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    handleMatchSuccess(first, current, p1, p2);
                    isProcessing = false;
                }, 300);
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    handleMatchFailure(first, current, p1, p2);
                    isProcessing = false;
                }, 500);
            }
            firstPos = -1;
        }
    }

    private void handleMatchSuccess(MatchItem first, MatchItem second, int p1, int p2) {
        first.setMatched(true);
        second.setMatched(true);
        matchedCount++;

        progressBar.setProgress(matchedCount, true);
        tvProgressText.setText(matchedCount + "/6");

        adapter.notifyItemChanged(p1);
        adapter.notifyItemChanged(p2);

        if (matchedCount == 6) saveProgress();
    }

    private void handleMatchFailure(MatchItem first, MatchItem second, int p1, int p2) {
        first.setSelected(false);
        second.setSelected(false);
        adapter.notifyItemChanged(p1);
        adapter.notifyItemChanged(p2);
    }

    private void saveProgress() {
        int userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", -1);
        GameHistory history = new GameHistory(userId, "Ghép từ", 10, System.currentTimeMillis());

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            database.progressDao().insertGameHistory(history);
            runOnUiThread(this::showGameOverDialog);
        });
    }

    private void showGameOverDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_over);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);

        MaterialButton btnPlayAgain = dialog.findViewById(R.id.btn_play_again);
        MaterialButton btnExit = dialog.findViewById(R.id.btn_exit_game);
        TextView tvScore = dialog.findViewById(R.id.tv_score_result);

        if (tvScore != null) tvScore.setText("Bạn đã nhận được +10 sao");

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
        isProcessing = false;
        progressBar.setProgress(0);
        tvProgressText.setText("0/6");
        loadData();
    }
}