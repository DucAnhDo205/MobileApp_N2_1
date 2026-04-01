package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.User;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editName, editUsername, editPhone, editEmail, editPassword;
    private VocaVerseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = VocaVerseDatabase.getDatabase(this);

        editName = findViewById(R.id.edit_text_name);
        editUsername = findViewById(R.id.edit_text_username);
        editEmail = findViewById(R.id.edit_text_email);
        editPhone = findViewById(R.id.edit_text_phone);
        editPassword = findViewById(R.id.edit_text_password);

        MaterialButton registerButton = findViewById(R.id.button_register);
        TextView loginText = findViewById(R.id.text_login);

        registerButton.setOnClickListener(v -> handleRegistration());

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void handleRegistration() {
        String name = editName.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || 
            TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // Kiểm tra trùng lặp
                if (database.userDao().isUserExists(email, username)) {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Email hoặc Username đã tồn tại!", Toast.LENGTH_SHORT).show());
                } else {
                    User newUser = new User(name, username, email, password, phone);
                    long newUserId = database.userDao().insert(newUser);
                    
                    Log.d("DATABASE_LOG", "Đã lưu User mới vào Database với ID: " + newUserId);
                    
                    runOnUiThread(() -> {
                        getSharedPreferences("VocaVersePrefs", MODE_PRIVATE)
                                .edit()
                                .putInt("current_user_id", (int)newUserId)
                                .apply();

                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LanguageSelectionActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
            } catch (Exception e) {
                Log.e("DATABASE_LOG", "Lỗi khi lưu Database: " + e.getMessage());
            }
        });
    }
}