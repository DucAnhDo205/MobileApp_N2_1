package com.duolingo.app.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.duolingo.app.R;
import com.duolingo.app.adapter.PronunciationPagerAdapter;
import com.duolingo.app.models.PronunciationQuestion;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;

import androidx.viewpager2.widget.ViewPager2;
import java.util.List;

public class PronunciationActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private ViewPager2 viewPagerQuestions;
    private PronunciationPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronunciation);

        findViewById(R.id.btnClose).setOnClickListener(v -> finish());
        checkAudioPermission();

        // 1. Ánh xạ ViewPager2
        viewPagerQuestions = findViewById(R.id.viewPagerQuestions);

        // 2. Vô hiệu hóa lướt tay (bắt buộc phải bấm nút Tiếp tục mới cho qua câu)
        viewPagerQuestions.setUserInputEnabled(false);

        // 3. Nhận ID bài học từ màn hình danh sách truyền sang
        // 3. Nhận ID bài học từ màn hình danh sách truyền sang
        String tempLessonId = getIntent().getStringExtra("LESSON_ID");
        if (tempLessonId == null) tempLessonId = "1"; // Mặc định lấy bài 1 nếu bị lỗi

        // Chốt hạ giá trị vào một biến final để truyền vào Lambda
        final String lessonId = tempLessonId;

        // 4. Lôi data từ Database lên (Nhớ là phải gọi ở luồng chạy ngầm)
        VocaVerseDatabase db = VocaVerseDatabase.getDatabase(this);
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {

            // Dùng biến lessonId (đã final) ở đây sẽ không bao giờ lỗi nữa
            List<PronunciationQuestion> questions = db.pronunciationDao().getQuestionsByLessonId(lessonId);

            // Lấy xong thì phải quay lại luồng chính (Main Thread) để cập nhật UI
            runOnUiThread(() -> {
                if (questions != null && !questions.isEmpty()) {
                    adapter = new PronunciationPagerAdapter(this, questions);
                    viewPagerQuestions.setAdapter(adapter);

                    // LẮNG NGHE SỰ KIỆN TỪ FRAGMENT
                    // (Lưu ý: Đoạn này cần can thiệp vào Adapter một chút để set listener cho từng Fragment)
                    // Để nhanh gọn cho đồ án, mình sẽ xử lý trực tiếp trên nút bấm:

                    MaterialButton btnAction = findViewById(R.id.btnAction);
                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setMax(questions.size());

                    btnAction.setOnClickListener(v -> {
                        int currentItem = viewPagerQuestions.getCurrentItem();
                        if (currentItem < questions.size() - 1) {
                            // Nhảy sang câu tiếp theo
                            viewPagerQuestions.setCurrentItem(currentItem + 1);
                            progressBar.setProgress(currentItem + 2);

                            // Reset nút về trạng thái chưa làm
                            btnAction.setText("KIỂM TRA");
                            btnAction.setBackgroundColor(Color.parseColor("#E0E0E0"));
                        } else {
                            // Câu cuối cùng
                            Toast.makeText(this, "Chúc mừng Hương đã hoàn thành bài luyện nói!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
            });
        });
    }

    private void checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Hiện Pop-up xin quyền
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            permissionToRecordAccepted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!permissionToRecordAccepted) {
                Toast.makeText(this, "Em phải cấp quyền Micro thì app mới nghe em đọc được chứ!", Toast.LENGTH_LONG).show();
                finish(); // Đuổi về màn hình trước
            }
        }
    }
}