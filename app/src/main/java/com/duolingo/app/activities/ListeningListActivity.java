package com.duolingo.app.activities; //

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
import com.google.android.material.card.MaterialCardView;

public class ListeningListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_list);

        // Nút back
        findViewById(R.id.btn_back_listening_list).setOnClickListener(v -> finish());

        // Ánh xạ 3 cái thẻ Level
        MaterialCardView cardEasy = findViewById(R.id.card_level_easy);
        MaterialCardView cardMedium = findViewById(R.id.card_level_medium);
        MaterialCardView cardHard = findViewById(R.id.card_level_hard);

        // Bắt sự kiện Click và truyền chữ "Dễ", "Trung bình", "Khó" sang phòng nghe
        cardEasy.setOnClickListener(v -> openListeningRoom("A1"));
        cardMedium.setOnClickListener(v -> openListeningRoom("B1"));
        cardHard.setOnClickListener(v -> openListeningRoom("C1"));
    }

    private void openListeningRoom(String level) {
        Intent intent = new Intent(this, ListeningActivity.class);
        intent.putExtra("LEVEL_KEY", level); // Gắn chìa khóa chứa Level
        startActivity(intent);
    }
}