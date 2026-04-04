package com.duolingo.app.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.ListeningQuestion;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Locale;

public class ListeningActivity extends AppCompatActivity {

    // Views cho Player
    private ImageView btnPlayPause;
    private SeekBar seekbarAudio;
    private TextView tvAudioTime;

    // Views cho Transcript & UI
    private TextView btnToggleTranscript, tvTranscriptContent, tvListenLevel;
    private LinearLayout layoutQuestionsContainer;
    private MaterialButton btnSubmitListening;
    private boolean isTranscriptVisible = false;

    // Biến điều khiển Audio
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable runnable;

    // Dữ liệu
    private ListeningQuestion currentLesson;
    private String[] userAnswers = new String[4]; // Mảng chứa 4 đáp án

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        initViews();
        // MỚI SỬA: Bắt lấy cái Intent từ ListeningListActivity truyền sang
        String selectedLevel = getIntent().getStringExtra("LEVEL_KEY");
        if (selectedLevel == null) selectedLevel = "Dễ"; // Phòng hờ lỗi thì mặc định là Dễ

        // Nạp bài nghe theo đúng Level người dùng chọn
        loadListeningData(selectedLevel);
    }

    private void initViews() {
        findViewById(R.id.btn_back_listen).setOnClickListener(v -> finish());
        tvListenLevel = findViewById(R.id.tv_listen_level);

        // Player
        btnPlayPause = findViewById(R.id.btn_play_pause);
        seekbarAudio = findViewById(R.id.seekbar_audio);
        tvAudioTime = findViewById(R.id.tv_audio_time);

        // Transcript
        btnToggleTranscript = findViewById(R.id.btn_toggle_transcript);
        tvTranscriptContent = findViewById(R.id.tv_transcript_content);

        // Nơi chứa câu hỏi & Nút nộp
        layoutQuestionsContainer = findViewById(R.id.layout_listening_questions_container);
        btnSubmitListening = findViewById(R.id.btn_submit_listening);

        // Xử lý nút ẩn/hiện Transcript
        btnToggleTranscript.setOnClickListener(v -> {
            isTranscriptVisible = !isTranscriptVisible;
            if (isTranscriptVisible) {
                tvTranscriptContent.setVisibility(View.VISIBLE);
                btnToggleTranscript.setText("🙈 Ẩn Kịch bản (Transcript)");
            } else {
                tvTranscriptContent.setVisibility(View.GONE);
                btnToggleTranscript.setText("👁️ Xem Kịch bản (Transcript)");
            }
        });

        // Xử lý nộp bài
        btnSubmitListening.setOnClickListener(v -> calculateAndShowScore());
    }

    private void loadListeningData(String level) {
        for (int i = 0; i < 4; i++) userAnswers[i] = "";

        // Bật Radar xem nó có nhận đúng chữ A1, B1, C1 không
        Toast.makeText(this, "Đang tải dữ liệu cho mã: " + level, Toast.LENGTH_SHORT).show();

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            List<ListeningQuestion> lessons = VocaVerseDatabase.getDatabase(this)
                    .listeningDao().getQuestionsByLevel(level);

            runOnUiThread(() -> {
                if (lessons != null && !lessons.isEmpty()) {
                    currentLesson = lessons.get(0);

                    // Cập nhật lại Level hiển thị
                    tvListenLevel.setText("Level: " + currentLesson.level);
                    tvTranscriptContent.setText(currentLesson.transcript.replace("\\n", "\n"));

                    setupAudioPlayer(currentLesson.audioFile);
                    buildQuestionList();
                } else {
                    // Nếu vẫn không thấy, nó sẽ gào lên lỗi này
                    Toast.makeText(this, "CHÚ Ý: Không tìm thấy bài nào có mã " + level + " trong Database!", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    // --- LOGIC MEDIA PLAYER (ÂM THANH) ---
    private void setupAudioPlayer(String fileName) {
        // Thủ thuật IT: Biến chuỗi tên "listen_a1_01" thành ID tài nguyên thực sự trong thư mục raw
        int resId = getResources().getIdentifier(fileName, "raw", getPackageName());

        if (resId == 0) {
            Toast.makeText(this, "Lỗi: Không tìm thấy file âm thanh " + fileName, Toast.LENGTH_LONG).show();
            return;
        }

        mediaPlayer = MediaPlayer.create(this, resId);
        seekbarAudio.setMax(mediaPlayer.getDuration());

        // Cập nhật UI thời lượng
        updateTimeText(0, mediaPlayer.getDuration());

        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(R.drawable.ic_play);
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause);
                updateSeekBar();
            }
        });

        // Người dùng tự kéo thanh SeekBar
        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) mediaPlayer.seekTo(progress);
                updateTimeText(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Khi chạy hết bài tự động reset
        mediaPlayer.setOnCompletionListener(mp -> {
            btnPlayPause.setImageResource(R.drawable.ic_play);
            seekbarAudio.setProgress(0);
        });
    }

    private void updateSeekBar() {
        seekbarAudio.setProgress(mediaPlayer.getCurrentPosition());
        updateTimeText(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
        if (mediaPlayer.isPlaying()) {
            runnable = this::updateSeekBar;
            handler.postDelayed(runnable, 1000); // Lặp lại mỗi giây
        }
    }

    private void updateTimeText(int currentMs, int totalMs) {
        String current = String.format(Locale.getDefault(), "%02d:%02d",
                (currentMs / 1000) / 60, (currentMs / 1000) % 60);
        String total = String.format(Locale.getDefault(), "%02d:%02d",
                (totalMs / 1000) / 60, (totalMs / 1000) % 60);
        tvAudioTime.setText(current + " / " + total);
    }

    // --- TÁI SỬ DỤNG KHUÔN ĐỂ RẢI 4 CÂU HỎI ---
    private void buildQuestionList() {
        layoutQuestionsContainer.removeAllViews();

        // Đóng gói dữ liệu 4 câu thành mảng để lặp cho code sạch
        String[] qTexts = {currentLesson.q1Text, currentLesson.q2Text, currentLesson.q3Text, currentLesson.q4Text};
        String[] qAs = {currentLesson.q1A, currentLesson.q2A, currentLesson.q3A, currentLesson.q4A};
        String[] qBs = {currentLesson.q1B, currentLesson.q2B, currentLesson.q3B, currentLesson.q4B};
        String[] qCs = {currentLesson.q1C, currentLesson.q2C, currentLesson.q3C, currentLesson.q4C};
        String[] qDs = {currentLesson.q1D, currentLesson.q2D, currentLesson.q3D, currentLesson.q4D};

        for (int i = 0; i < 4; i++) {
            if (qTexts[i] == null || qTexts[i].isEmpty()) continue; // Bỏ qua nếu câu hỏi rỗng

            final int questionIndex = i;
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_practice_question, layoutQuestionsContainer, false);

            TextView tvStep = itemView.findViewById(R.id.tv_item_step);
            TextView tvQuestion = itemView.findViewById(R.id.tv_item_question);
            MaterialButton btnA = itemView.findViewById(R.id.btn_item_a);
            MaterialButton btnB = itemView.findViewById(R.id.btn_item_b);
            MaterialButton btnC = itemView.findViewById(R.id.btn_item_c);
            MaterialButton btnD = itemView.findViewById(R.id.btn_item_d);

            tvStep.setText("Câu hỏi " + (i + 1));
            tvQuestion.setText(qTexts[i]);
            btnA.setText(qAs[i]);
            btnB.setText(qBs[i]);
            btnC.setText(qCs[i]);
            btnD.setText(qDs[i]);

            MaterialButton[] buttons = {btnA, btnB, btnC, btnD};
            for (MaterialButton btn : buttons) {
                btn.setOnClickListener(v -> {
                    for (MaterialButton b : buttons) {
                        b.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
                        b.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    }
                    btn.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#3B82F6"))); // Xanh dương
                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFF6FF")));
                    userAnswers[questionIndex] = btn.getText().toString().toLowerCase();
                });
            }
            layoutQuestionsContainer.addView(itemView);
        }
    }

    // --- CHẤM ĐIỂM ---
    private void calculateAndShowScore() {
        int score = 0;
        int totalQs = 0;
        String[] corrects = {currentLesson.q1Correct, currentLesson.q2Correct, currentLesson.q3Correct, currentLesson.q4Correct};

        for (int i = 0; i < 4; i++) {
            if (corrects[i] == null || corrects[i].isEmpty()) continue;
            totalQs++;
            if (userAnswers[i].trim().equalsIgnoreCase(corrects[i].trim())) {
                score++;
            }
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("KẾT QUẢ BÀI NGHE 🎧")
                .setMessage("Điểm số của bạn: " + score + " / " + totalQs + "\n\nHãy xem lại Transcript để rút kinh nghiệm nhé!")
                .setCancelable(false)
                .setPositiveButton("HOÀN THÀNH", (dialog, which) -> finish())
                .show();
    }

    // Quan trọng số 1 của dân làm hệ thống: Dọn rác bộ nhớ khi đóng màn hình!
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) handler.removeCallbacks(runnable);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}