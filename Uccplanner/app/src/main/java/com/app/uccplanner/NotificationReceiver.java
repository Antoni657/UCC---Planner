package com.app.uccplanner;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String eventTitle = intent.getStringExtra("title");




        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "event_channel")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Event Reminder")
                .setContentText("You have an upcoming event: " + eventTitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setAutoCancel(true);
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        builder.setSound(soundUri);
//
//
//        long[] vibrationPattern = {0, 1000, 500, 1000, 500, 1000};
//        builder.setVibrate(vibrationPattern);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
        }


    }

