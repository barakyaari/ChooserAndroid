package net.cloudapp.chooser.chooser.notificationServices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import net.cloudapp.chooser.chooser.R;

/**
 * Created by Ben on 14/11/2016.
 */

public abstract class Notifier {
    private static final int uniqueID = 20214001;

    public static void sendNotification(String notificationBody, Context context) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Notification Received!");
        notification.setContentText(notificationBody);
        attachIntent(notification, context);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }

    private static void attachIntent(NotificationCompat.Builder notification, Context context) {
        Intent i = new Intent("android.intent.action.FeedView");
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pi);
    }
}
