package com.duolingo.app.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.duolingo.app.R;
import com.duolingo.app.activities.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "study_reminder")
                .setSmallIcon(R.drawable.ic_vocaverse_logo)
                .setContentTitle("VocaVerse: Giờ học đến rồi!")
                .setContentText("Dành ra 5-10 phút để duy trì streak của bạn ngay thôi nào! 🔥")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(101, builder.build());
        } catch (SecurityException e) {
            // Xử lý nếu thiếu quyền POST_NOTIFICATIONS trên Android 13+
        }
    }
}
