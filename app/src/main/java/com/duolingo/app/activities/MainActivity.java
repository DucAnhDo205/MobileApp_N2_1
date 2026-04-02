package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.adapter.StudyModuleAdapter;
import com.duolingo.app.models.GrammarQuestion;
import com.duolingo.app.models.StudyModule;
import com.duolingo.app.models.User;
import com.duolingo.app.persistence.CSVHelper;
import com.duolingo.app.persistence.VocaVerseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvStudyModules;
    private List<StudyModule> moduleList;

    private TextView textGreeting, textUserName;

    // Khai báo lại cầu nối Database
    private VocaVerseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 1. Ánh xạ View
        rvStudyModules = findViewById(R.id.rvStudyModules);
        textGreeting = findViewById(R.id.text_greeting);
        textUserName = findViewById(R.id.text_user_name);

        // 2. Khởi tạo Database
        database = VocaVerseDatabase.getDatabase(this);

        // 3. Tự động chào theo giờ & Móc tên từ Database lên!
        updateGreeting();
        loadUserData();

        // 4. Khởi tạo dữ liệu lưới
        initData();

        // 5. Thiết lập hiển thị
        setupRecyclerView();

        initializeGrammarData();

        loadGrammarQuestionsFromCSV();
    }

    // Luồng móc nối dữ liệu thực tế từ Database
    private void loadUserData() {
        // Đọc ID người dùng từ phiên đăng nhập (Mặc định là -1 nếu chưa ai đăng nhập)
        int userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", -1);

        if (userId != -1) {
            // Có người đăng nhập -> Xuống Database tìm tên
            VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
                User user = database.userDao().getUserById(userId);
                if (user != null) {
                    // Update UI bắt buộc phải chạy trên Main Thread
                    runOnUiThread(() -> textUserName.setText(user.getFullName() + " 👋"));
                }
            });
        } else {
            // Chưa có ai đăng nhập (Khách vãng lai)
            textUserName.setText("Người dùng 👋");
        }
    }

    private void updateGreeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting;

        if (timeOfDay >= 0 && timeOfDay < 12) greeting = "Chào buổi sáng,";
        else if (timeOfDay >= 12 && timeOfDay < 16) greeting = "Chào buổi trưa,";
        else if (timeOfDay >= 16 && timeOfDay < 21) greeting = "Chào buổi chiều,";
        else greeting = "Chào buổi tối,";

        textGreeting.setText(greeting);
    }

    private void initData() {
        moduleList = new ArrayList<>();
        moduleList.add(new StudyModule("vocab", R.drawable.ic_book, "Từ vựng", "Flashcard", R.color.vocab_main, R.color.vocab_bg));
        moduleList.add(new StudyModule("grammar", R.drawable.ic_pen, "Ngữ pháp", "Điền từ & Sắp xếp", R.color.grammar_main, R.color.grammar_bg));
        moduleList.add(new StudyModule("listening", R.drawable.ic_headphone, "Nghe hiểu", "Trắc nghiệm", R.color.listening_main, R.color.listening_bg));
        moduleList.add(new StudyModule("pronunciation", R.drawable.ic_mic, "Phát âm", "AI đánh giá", R.color.pronunciation_main, R.color.pronunciation_bg));
    }

    private void setupRecyclerView() {
        StudyModuleAdapter adapter = new StudyModuleAdapter(moduleList, module -> {
            // Giả sử id của module ngữ pháp là "grammar"
            if (module.getId().equals("grammar")) {
                Intent intent = new Intent(MainActivity.this, GrammarListActivity.class);
                startActivity(intent);
            }
        });

        rvStudyModules.setLayoutManager(new GridLayoutManager(this, 2));
        rvStudyModules.setAdapter(adapter);
    }

    private void initializeGrammarData() {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            if (database.grammarDao().getCount() == 0) {
                List<GrammarQuestion> questions = CSVHelper.readGrammarFromCSV(this, "grammar_questions.csv");
                database.grammarDao().insertAll(questions);
            }
        });
    }

    private void loadGrammarQuestionsFromCSV() {
        VocaVerseDatabase db = VocaVerseDatabase.getDatabase(this);
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {

            // Dùng getCount() để đếm xem kho có trống không
            if (db.grammarDao().getCount() == 0) {
                try {
                    java.io.InputStream is = getAssets().open("grammar_questions.csv");
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                    String line;
                    reader.readLine(); // Bỏ qua dòng tiêu đề

                    List<GrammarQuestion> bulkInsertList = new ArrayList<>();

                    while ((line = reader.readLine()) != null) {
                        // Regex này giúp tách dấu phẩy chuẩn hơn (phòng trường hợp trong lý thuyết có dấu phẩy)
                        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                        if (parts.length >= 12) {
                            GrammarQuestion q = new GrammarQuestion(
                                    parts[0].replace("\"", "").trim(),
                                    parts[1].replace("\"", "").trim(),
                                    parts[2].replace("\"", "").trim(),
                                    parts[3].replace("\"", "").trim(),
                                    parts[4].replace("\"", "").trim(),
                                    parts[5].replace("\"", "").trim(),
                                    parts[6].replace("\"", "").trim(),
                                    parts[7].replace("\"", "").trim(),
                                    parts[8].replace("\"", "").trim(),
                                    parts[9].replace("\"", "").trim(),
                                    parts[10].replace("\"", "").trim(),
                                    parts[11].replace("\"", "").trim()
                            );
                            bulkInsertList.add(q);
                        }
                    }
                    reader.close();

                    // Nhồi một phát tất cả dữ liệu vào Database
                    db.grammarDao().insertAll(bulkInsertList);

                    // Báo cáo thành công ra màn hình
                    runOnUiThread(() -> android.widget.Toast.makeText(MainActivity.this,
                            "Đã nạp xong " + bulkInsertList.size() + " câu Ngữ pháp!",
                            android.widget.Toast.LENGTH_LONG).show());

                } catch (Exception e) {
                    // Nếu lỗi, in thẳng ra Logcat dòng màu đỏ để bắt bệnh
                    e.printStackTrace();
                    runOnUiThread(() -> android.widget.Toast.makeText(MainActivity.this,
                            "Lỗi đọc file CSV: " + e.getMessage(),
                            android.widget.Toast.LENGTH_LONG).show());
                }
            }
        });
    }
}