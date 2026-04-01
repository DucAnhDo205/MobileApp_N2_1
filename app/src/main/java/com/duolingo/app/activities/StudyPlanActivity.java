package com.duolingo.app.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.LearningProgress;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.duolingo.app.services.NotificationReceiver;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class StudyPlanActivity extends AppCompatActivity {

    private Button buttonPickTime;
    private int selectedHour = 19;
    private int selectedMinute = 0;
    private VocaVerseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_plan);

        database = VocaVerseDatabase.getDatabase(this);
        buttonPickTime = findViewById(R.id.button_pick_time);
        MaterialButton buttonStart = findViewById(R.id.button_start);

        buttonPickTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        buttonPickTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }, selectedHour, selectedMinute, true);
            timePickerDialog.show();
        });

        buttonStart.setOnClickListener(v -> handleStartJourney());
        
        createNotificationChannel();
    }

    private void handleStartJourney() {
        int userId = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE).getInt("current_user_id", -1);
        
        if (userId != -1) {
            VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
                // Tạo một bản ghi tiến trình mới cho User
                // Giả định languageId là 1 (mặc định cho lần đầu, hoặc lấy từ LanguageSelection)
                LearningProgress progress = new LearningProgress(userId, 1, 0, 0, 1);
                database.learningProgressDao().insert(progress);
                
                // Lưu thời gian thông báo
                getSharedPreferences("VocaVersePrefs", MODE_PRIVATE)
                        .edit()
                        .putInt("notif_hour", selectedHour)
                        .putInt("notif_minute", selectedMinute)
                        .apply();

                scheduleNotification();

                runOnUiThread(() -> {
                    Toast.makeText(StudyPlanActivity.this, "Hành trình bắt đầu! Chúc bạn học tốt.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudyPlanActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            });
        }
    }

    private void scheduleNotification() {
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "StudyReminderChannel";
            String description = "Channel for Study Reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("study_reminder", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}