package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.LearningProgress;
import com.duolingo.app.models.User;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editUsername, editPassword;
    private VocaVerseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = VocaVerseDatabase.getDatabase(this);

        editUsername = findViewById(R.id.edit_text_email); 
        editPassword = findViewById(R.id.edit_text_password);

        MaterialButton loginButton = findViewById(R.id.button_login);
        TextView signUpText = findViewById(R.id.text_sign_up);

        loginButton.setOnClickListener(v -> handleLogin());

        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
            return;
        }

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            User user = database.userDao().login(username, password);
            if (user != null) {
                // Lưu userId vào SharedPreferences để dùng sau này
                getSharedPreferences("VocaVersePrefs", MODE_PRIVATE)
                        .edit()
                        .putInt("current_user_id", user.getId())
                        .apply();

                // Kiểm tra xem user đã có dữ liệu học tập chưa
                LearningProgress progress = database.learningProgressDao().getProgressByUserId(user.getId());
                
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Chào mừng " + user.getFullName(), Toast.LENGTH_SHORT).show();
                    if (progress == null) {
                        // Nếu chưa có dữ liệu học -> Bắt đầu Setup
                        Intent intent = new Intent(LoginActivity.this, LanguageSelectionActivity.class);
                        startActivity(intent);
                    } else {
                        // Nếu đã có -> Vào màn hình chính
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show());
            }
        });
    }
}