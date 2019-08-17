package com.creativeshare.constructionstock.activities_fragments.notifications;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.models.NotificationStatusModel;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {
    Preferences preferences = Preferences.getInstance();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData()!=null)
        {
            final Map<String, String> map = remoteMessage.getData();

            for (String key : map.keySet()) {
                Log.e("key : ", key + " value :" + map.get(key));
            }
            if (getSession().equals(Tags.session_login)) {

                String current_user_id = String.valueOf(getUserData().getId());
                String to_user_id = map.get("to_id");
                if (current_user_id.equals(to_user_id)) {
                    ManageNotification(map);
                }

            }
        }

    }

    private void ManageNotification(Map<String, String> map) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNewVersionNotification(map);

        } else {
            createOldVersionNotification(map);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNewVersionNotification(final Map<String, String> map) {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );

        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_not);
        Intent intent = new Intent(this, Home_Activity.class);
        intent.putExtra("status",1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(map.get("from_user"));
        builder.setContentText(getString(R.string.new_offer));
        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(FireBaseMessaging.this).load(R.drawable.logo).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                builder.setLargeIcon(bitmap);
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotificationStatusModel statusModel = new NotificationStatusModel(1);
                                if (manager!=null)
                                {
                                    manager.createNotificationChannel(channel);
                                    manager.notify(1250,builder.build());
                                    EventBus.getDefault().post(statusModel);
                                }

                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }
                },10);




    }

    private void createOldVersionNotification(final Map<String, String> map) {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_not);
        Intent intent = new Intent(this, Home_Activity.class);
        intent.putExtra("status",1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(map.get("from_user"));
        builder.setContentText(getString(R.string.new_offer));
        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(FireBaseMessaging.this).load(R.drawable.logo).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                builder.setLargeIcon(bitmap);
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotificationStatusModel statusModel = new NotificationStatusModel(1);
                                if (manager!=null)
                                {
                                    manager.notify(1250,builder.build());
                                    EventBus.getDefault().post(statusModel);
                                }

                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }
                },10);





    }


    private UserModel getUserData() {
        return preferences.getUserData(this);
    }

    private String getSession() {
        return preferences.getSession(this);
    }
}
