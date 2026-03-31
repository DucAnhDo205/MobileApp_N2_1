package com.duolingo.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
import com.duolingo.app.models.User;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {
    private VocaVerseDatabase database;
    private int userId;
    private TextInputEditText edtFullName, edtEmail, edtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        database = VocaVerseDatabase.getDatabase(this);
        userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", -1);

        edtFullName = findViewById(R.id.edt_full_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);

        loadCurrentData();

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_save).setOnClickListener(v -> saveUser());
        findViewById(R.id.btn_change_password).setOnClickListener(v -> showChangePasswordDialog());
    }

    private void loadCurrentData() {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            User user = database.userDao().getUserById(userId);
            if (user != null) {
                runOnUiThread(() -> {
                    edtFullName.setText(user.getFullName());
                    edtEmail.setText(user.getEmail());
                    edtPhone.setText(user.getPhoneNumber());
                });
            }
        });
    }

    private void saveUser() {
        String name = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (name.isEmpty()) {
            edtFullName.setError("Tên không được để trống");
            return;
        }

        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            User user = database.userDao().getUserById(userId);
            if (user != null) {
                user.setFullName(name);
                user.setEmail(email);
                user.setPhoneNumber(phone);
                database.userDao().update(user);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextInputEditText edtCurrent = dialogView.findViewById(R.id.edt_current_password);
        TextInputEditText edtNew = dialogView.findViewById(R.id.edt_new_password);
        TextInputEditText edtConfirm = dialogView.findViewById(R.id.edt_confirm_password);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.btn_confirm_save);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String currentPass = edtCurrent.getText().toString().trim();
            String newPass = edtNew.getText().toString().trim();
            String confirmPass = edtConfirm.getText().toString().trim();

            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                edtConfirm.setError("Mật khẩu xác nhận không khớp");
                return;
            }

            if (newPass.length() < 8) {
                edtNew.setError("Mật khẩu phải từ 8 ký tự trở lên");
                return;
            }

            VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
                User user = database.userDao().getUserById(userId);
                if (user != null) {
                    if (!user.getPassword().equals(currentPass)) {
                        runOnUiThread(() -> edtCurrent.setError("Mật khẩu hiện tại không đúng"));
                        return;
                    }

                    user.setPassword(newPass);
                    database.userDao().update(user);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                }
            });
        });

        dialog.show();
    }
}