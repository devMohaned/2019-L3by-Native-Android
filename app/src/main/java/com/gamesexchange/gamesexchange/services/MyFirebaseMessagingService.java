package com.gamesexchange.gamesexchange.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.gamesexchange.gamesexchange.activities.login.MVPLoginActivity;
import com.gamesexchange.gamesexchange.activities.messaging.MessagingActivity;
import com.gamesexchange.gamesexchange.activities.messaging.UserListMessagesActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingServ";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
      //  Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
        //    Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
        //    Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        sendNotification(remoteMessage.getData());
    }

    private void sendNotification(Map<String, String> messageBody) {
//        Intent intent = new Intent(this, MessagingActivity.class);
        Intent intent;
        if (MVPHomePageActivity.mAuth != null)
        {
             intent = new Intent(this, UserListMessagesActivity.class);

        }else{
             intent = new Intent(this, MVPLoginActivity.class);

        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final int not_nu = generateRandom();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, not_nu/* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.BigTextStyle bigText = new Notification.BigTextStyle();
        bigText.bigText(messageBody.get("message"));
        bigText.setBigContentTitle(messageBody.get("title"));
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(messageBody.get("title"))
                .setContentText(messageBody.get("message"))
                .setAutoCancel(true)
                .setStyle(bigText)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            notificationManager.notify(not_nu/* ID of notification */, notificationBuilder.build());
            setNewMessageSharedPreference(messageBody.get("title"));
        }catch (NullPointerException e)
        {
            Toast.makeText(this, getString(R.string.couldnot_get_notifaction), Toast.LENGTH_SHORT).show();
        }}


    public int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }


    private void setNewMessageSharedPreference(String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF_MESSAGES, MODE_PRIVATE).edit();
        editor.putString(userName, Constants.SHARED_PREF_MESSAGING_UNREAD);
        editor.apply();
    }
}
