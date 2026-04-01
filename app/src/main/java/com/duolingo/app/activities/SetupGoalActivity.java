package com.duolingo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SetupGoalActivity extends AppCompatActivity {

    private TextView textStepQuestion;
    private LinearLayout layoutOptions;
    private MaterialButton buttonNext;
    
    private int currentStep = 1;
    private String selectedTime = "";
    private String selectedLevel = "";
    private String selectedReason = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_goal);

        textStepQuestion = findViewById(R.id.text_step_question);
        layoutOptions = findViewById(R.id.layout_options);
        buttonNext = findViewById(R.id.button_next_step);

        loadStep(1);

        buttonNext.setOnClickListener(v -> {
            if (currentStep < 3) {
                currentStep++;
                loadStep(currentStep);
            } else {
                finishSetup();
            }
        });
    }

    private void loadStep(int step) {
        layoutOptions.removeAllViews();
        List<String> options = new ArrayList<>();

        if (step == 1) {
            textStepQuestion.setText("Tại sao bạn lại muốn học?"); // Changed order
            options.add("Du Lịch ✈️");
            options.add("Công việc 💼");
            options.add("Trường học 🎓");
            options.add("Mục đích khác");
            buttonNext.setText("Tiếp theo");
        } else if (step == 2) {
            textStepQuestion.setText("Bạn muốn dành bao nhiêu thời gian để học mỗi ngày?"); // Changed order
            options.add("🔥 30 phút / ngày");
            options.add("🔥 1 tiếng / ngày");
            options.add("🔥 Nhiều hơn");
            buttonNext.setText("Tiếp theo");
        } else if (step == 3) {
            textStepQuestion.setText("Trình độ Tiếng Anh của bạn đang ở mức nào rồi?"); // Changed order
            options.add("Người mới");
            options.add("Biết chút ít");
            options.add("Biết nhiều chút");
            buttonNext.setText("Bắt đầu ngay!");
        }

        for (String opt : options) {
            addOptionView(opt);
        }
    }

    private void addOptionView(String title) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_goal_option, layoutOptions, false);
        MaterialCardView card = view.findViewById(R.id.card_option);
        TextView textTitle = view.findViewById(R.id.text_option_title);
        textTitle.setText(title);

        card.setOnClickListener(v -> {
            // Reset tất cả các card khác về trạng thái bình thường
            for (int i = 0; i < layoutOptions.getChildCount(); i++) {
                MaterialCardView otherCard = layoutOptions.getChildAt(i).findViewById(R.id.card_option);
                otherCard.setStrokeWidth(0);
                otherCard.setSelected(false);
            }
            // Làm nổi bật card được chọn
            card.setStrokeWidth(4);
            card.setSelected(true);
            
            if (currentStep == 1) selectedReason = title; // Changed assignment
            else if (currentStep == 2) selectedTime = title; // Changed assignment
            else if (currentStep == 3) selectedLevel = title; // Changed assignment
        });

        layoutOptions.addView(view);
    }

    private void finishSetup() {
        if (selectedTime.isEmpty() || selectedLevel.isEmpty() || selectedReason.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn mục tiêu của bạn!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Chuyển sang màn hình chính
        Intent intent = new Intent(SetupGoalActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}