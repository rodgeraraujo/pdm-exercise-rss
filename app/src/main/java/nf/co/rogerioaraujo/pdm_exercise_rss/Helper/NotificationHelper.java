package nf.co.rogerioaraujo.pdm_exercise_rss.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import nf.co.rogerioaraujo.pdm_exercise_rss.R;

public class NotificationHelper extends ContextWrapper {

    // notification info
    private static final String CHANNEL_ID = "nf.co.rogerioaraujo.pdm_exercise_rss";
    private static final String CHANNEL_NAME = "RSS App";
    private final int NOTIFICATION_ID = 001;
    private NotificationManager manager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {

        NotificationChannel rssChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        rssChannel.enableLights(true);
        rssChannel.enableVibration(true);
        rssChannel.setLightColor(Color.GREEN);
        rssChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(rssChannel);
    }

    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getRSSChannelNotification(String nTitle, String nBody) {
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentText(nBody)
                .setContentTitle(nTitle)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true);
    }
}
