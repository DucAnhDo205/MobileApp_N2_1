package com.duolingo.app.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import com.duolingo.app.R;
import com.duolingo.app.activities.ExamSelectionActivity;
import com.duolingo.app.activities.GameMenuActivity;
import com.duolingo.app.activities.MainActivity;
import com.duolingo.app.activities.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {

    public static void setup(Activity activity, BottomNavigationView nav, int currentId) {
        if (nav == null) return;

        // BƯỚC 1: Gỡ listener cũ để việc set trạng thái không gây nhảy trang
        nav.setOnItemSelectedListener(null);

        // BƯỚC 2: Chỉ tô màu icon hiện tại (setChecked KHÔNG kích hoạt chuyển trang)
        MenuItem currentItem = nav.getMenu().findItem(currentId);
        if (currentItem != null) {
            currentItem.setChecked(true);
        }

        // BƯỚC 3: Cài đặt Listener cho các thao tác bấm thật
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // Nếu bấm vào đúng trang đang đứng -> Đứng yên
            if (id == currentId) return true;

            Class<?> target = null;
            if (id == R.id.nav_study) target = MainActivity.class;
            else if (id == R.id.nav_community) target = GameMenuActivity.class;
            else if (id == R.id.nav_test) target = ExamSelectionActivity.class;
            else if (id == R.id.nav_profile) target = ProfileActivity.class;

            if (target != null) {
                Intent intent = new Intent(activity, target);

                // TỐI ƯU RAM: Lôi trang cũ lên, không tạo trang mới
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0); // Chuyển trang tức thì
                return true;
            }
            return false;
        });
    }
}