package com.duolingo.app.fragments;

import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.duolingo.app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class PronunciationQuestionFragment extends Fragment {

    private String transcript;
    private TextView txtTranscript, txtRecordingStatus;
    private FloatingActionButton btnRecord;

    // Hai "vũ khí" cốt lõi để nhận diện giọng nói
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private TextToSpeech tts;

    public PronunciationQuestionFragment(String transcript) {
        this.transcript = transcript;
    }

    public interface OnQuestionAnsweredListener {
        void onAnswered(int score);
    }

    private OnQuestionAnsweredListener listener;

    public void setOnQuestionAnsweredListener(OnQuestionAnsweredListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pronunciation_question, container, false);

        // 1. Ánh xạ View
        txtTranscript = view.findViewById(R.id.txtTranscript);
        txtRecordingStatus = view.findViewById(R.id.txtRecordingStatus);
        btnRecord = view.findViewById(R.id.btnRecord);

        tts = new TextToSpeech(requireContext(), status -> {
            if (status != TextToSpeech.ERROR) tts.setLanguage(Locale.US);
        });

        view.findViewById(R.id.btnPlaySample).setOnClickListener(v -> {
            tts.speak(transcript, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        // Hiển thị câu tiếng Anh lên màn hình
        txtTranscript.setText(transcript);

        // 2. Cài đặt bộ máy nghe lén (SpeechRecognizer)
        setupSpeechRecognizer();

        // 3. Bắt sự kiện bấm nút Mic
        btnRecord.setOnClickListener(v -> {
            txtRecordingStatus.setText("Đang nghe... (Nói xong tự ngắt)");
            txtRecordingStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            // Kích hoạt máy nghe
            speechRecognizer.startListening(speechRecognizerIntent);
        });

        return view;
    }

    private void setupSpeechRecognizer() {
        // Khởi tạo engine
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());

        // Cấu hình Intent: Bắt buộc là tiếng Anh (en-US)
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        // Lắng nghe kết quả trả về
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
                // Người dùng ngừng nói
                txtRecordingStatus.setText("Đang xử lý dữ liệu...");
                txtRecordingStatus.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }

            @Override
            public void onError(int error) {
                txtRecordingStatus.setText("Lỗi nghe (Mã: " + error + "). Nhấn thử lại!");
                txtRecordingStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data != null && !data.isEmpty()) {
                    String spokenText = data.get(0);
                    txtRecordingStatus.setText("Đã thu âm xong.");

                    // XÓA cái Toast đi, GỌI thuật toán chấm điểm vào đây:
                    processPronunciationResult(spokenText);
                }
            }
            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
    }

    // Hàm Cốt Lõi: So sánh và tô màu
    private void processPronunciationResult(String spokenText) {
        // 1. Chuẩn hóa chuỗi (Đưa về chữ thường, xóa sạch dấu câu chấm phẩy để so sánh cho chuẩn)
        String cleanTranscript = transcript.toLowerCase().replaceAll("[^a-z0-9 ]", "");
        String cleanSpoken = spokenText.toLowerCase().replaceAll("[^a-z0-9 ]", "");

        // Cắt thành mảng các từ
        String[] targetWords = cleanTranscript.split("\\s+");
        String[] spokenWords = cleanSpoken.split("\\s+");
        String[] originalWords = transcript.split("\\s+"); // Dùng mảng này để giữ lại dấu phẩy lúc hiển thị

        int correctCount = 0;
        SpannableStringBuilder coloredText = new SpannableStringBuilder();

        // 2. Thuật toán dò từng chữ
        for (int i = 0; i < originalWords.length; i++) {
            String originalWord = originalWords[i];
            String cleanTargetWord = originalWord.toLowerCase().replaceAll("[^a-z0-9]", "");

            boolean isCorrect = false;
            // Dò xem từ này có xuất hiện trong câu người dùng đọc không
            for (String sWord : spokenWords) {
                if (cleanTargetWord.equals(sWord)) {
                    isCorrect = true;
                    break;
                }
            }

            int startPos = coloredText.length();
            coloredText.append(originalWord).append(" ");
            int endPos = coloredText.length() - 1;

            // 3. Quét màu Xanh / Đỏ
            if (isCorrect) {
                coloredText.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                correctCount++;
            } else {
                coloredText.setSpan(new ForegroundColorSpan(Color.parseColor("#F44336")), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        // 4. Tính điểm % và Cập nhật Giao diện
        int score = (int) (((float) correctCount / originalWords.length) * 100);

        if (listener != null) listener.onAnswered(score);

        txtTranscript.setText(coloredText);

        View view = getView();
        if (view != null) {
            LinearLayout layoutResult = view.findViewById(R.id.layoutResult);
            TextView txtScore = view.findViewById(R.id.txtScore);
            TextView txtFeedbackMessage = view.findViewById(R.id.txtFeedbackMessage);

            layoutResult.setVisibility(View.VISIBLE);
            txtScore.setText(score + "%");

            if (score >= 80) {
                txtFeedbackMessage.setText("Tuyệt vời! Phát âm chuẩn như người bản xứ.");
                txtFeedbackMessage.setTextColor(Color.parseColor("#4CAF50"));
            } else if (score >= 50) {
                txtFeedbackMessage.setText("Khá lắm! Cố gắng luyện tập thêm chút nữa nhé.");
                txtFeedbackMessage.setTextColor(Color.parseColor("#FF9800")); // Màu cam
            } else {
                txtFeedbackMessage.setText("Chưa chuẩn lắm. Nghe lại audio và thử lại nha!");
                txtFeedbackMessage.setTextColor(Color.parseColor("#F44336")); // Màu đỏ
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Dân An toàn thông tin là phải nhớ giải phóng tài nguyên, không để rò rỉ bộ nhớ (Memory Leak)
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}