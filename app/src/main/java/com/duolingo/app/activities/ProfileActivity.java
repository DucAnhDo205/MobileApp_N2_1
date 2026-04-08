package com.duolingo.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.User;
import com.duolingo.app.persistence.VocaVerseDatabase;
import com.duolingo.app.utils.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView textProfileName;
    private ShapeableImageView imageProfileAvatar;
    private VocaVerseDatabase database;
    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        database = VocaVerseDatabase.getDatabase(this);
        NavigationHelper.setup(this, nav, R.id.nav_profile);

        RelativeLayout btnEditProfile = findViewById(R.id.btn_edit_profile);
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            });
        }
    }

    private void initViews() {
        textProfileName = findViewById(R.id.text_profile_name);
        imageProfileAvatar = findViewById(R.id.image_profile_avatar);
        nav = findViewById(R.id.bottom_navigation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nav != null) {
            nav.getMenu().findItem(R.id.nav_profile).setChecked(true);
        }
        loadUserData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        NavigationHelper.setup(this, nav, R.id.nav_profile);
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("VocaVersePrefs", MODE_PRIVATE);
        int userId = prefs.getInt("current_user_id", -1);

        if (userId != -1) {
            VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
                User user = database.userDao().getUserById(userId);
                if (user != null) {
                    runOnUiThread(() -> {
                        String name = (user.getFullName() != null && !user.getFullName().isEmpty())
                                ? user.getFullName() : user.getUsername();
                        textProfileName.setText(name);
                        imageProfileAvatar.setImageResource(R.drawable.ic_avatar_placeholder);
                    });
                }
            });
        }
    }
}