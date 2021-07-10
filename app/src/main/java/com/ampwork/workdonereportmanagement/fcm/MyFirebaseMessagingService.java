package com.ampwork.workdonereportmanagement.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.basic.SplashActivity;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Collection;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private PreferencesManager preferencesManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        preferencesManager = new PreferencesManager(MyFirebaseMessagingService.this);
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null) {
            if (remoteMessage.getData().size() > 0) {
                try {
                    Collection<String> data = remoteMessage.getData().values();
                    ArrayList<String> stringArrayList = new ArrayList<>();
                    for (String s : data) {
                        stringArrayList.add(s);
                    }
                    handleDataMessage(stringArrayList);
                } catch (Exception e) {
                    Log.e("FirebaseMessag", "Exception: " + e.getMessage());
                }

            }
        }

    }

    private void handleDataMessage(ArrayList<String> data) {

        String reportId = data.get(0);
        String title = data.get(1);
        String message = data.get(2);

        if (!AppUtility.isAppIsInBackground(getApplicationContext())) {

            sendNotification(title, message, reportId);

        } else {
            // If the app is in background, firebase itself handles the notification
            sendNotification(title, message, reportId);
        }
    }

    private void sendNotification(String title, String messageBody, String reportId) {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("is_from_notification", true);
        intent.putExtra("reportId", reportId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setColor(getResources().getColor(R.color.purple_500))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, /* ID of notification */notificationBuilder.build());
    }


    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), path);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
