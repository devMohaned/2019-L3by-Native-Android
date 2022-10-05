package com.gamesexchange.gamesexchange.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.login.MVPLoginActivity;

import static android.content.Context.MODE_PRIVATE;

public class DailyLoginBroadCaster extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREF_DAILY_LOGIN, MODE_PRIVATE);
        String collectedAt = prefs.getString("collected_at", null);

        if (collectedAt != null) {
            long currentTime = System.currentTimeMillis();
            long difference = currentTime - Long.parseLong(collectedAt);
            if (difference > 86400000 && difference < 172800000) {
                RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
                contentView.setImageViewResource(R.id.image, R.drawable.ic_not_coins);
                contentView.setTextViewText(R.id.title, context.getString(R.string.daily_login));
                contentView.setTextViewText(R.id.text, context.getString(R.string.daily_login_small_text));


                Intent intent2 = new Intent(context, MVPLoginActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 98320, intent2,
                        PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Notification.Builder notificationBuilder = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_not_coins)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setContent(contentView);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                try {
                    notificationManager.notify(98320, notificationBuilder.build());
                } catch (NullPointerException e) {
                    Toast.makeText(context, context.getString(R.string.couldnot_get_notifaction), Toast.LENGTH_SHORT).show();
                }
            }
        }




    }
}