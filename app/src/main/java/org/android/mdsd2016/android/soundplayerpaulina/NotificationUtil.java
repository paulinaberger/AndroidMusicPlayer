package org.android.mdsd2016.android.soundplayerpaulina;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by paulinaberger on 2017-04-01.
 */

public class NotificationUtil extends AppCompatActivity {

    public static void showNotification(Context context, Song song) {

        Intent resultIntent = new Intent(context, SongInfoActivity.class);
        resultIntent.putExtra(SongInfoActivity.KEY, song);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                                android.R.drawable.ic_media_play : R.drawable.ic_launcher)
                        .setContentTitle(song.getTitle())
                        .setContentText(song.getComment())
                        .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(0, notification);
    }
}
