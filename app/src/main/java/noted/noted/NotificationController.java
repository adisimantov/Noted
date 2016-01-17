package noted.noted;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by adi on 17-Jan-16.
 */
public class NotificationController {

    private  static int notificationCode = 1;

    private final static NotificationController instance = new NotificationController();

    private  NotificationController(){

    }

    public static NotificationController getInstance(){

        return instance;
    }


    public void notify(String from, String details, String id, Context context){
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(context)
                .setContentTitle(from)
                .setContentText(details)
                .setSmallIcon(R.mipmap.notedicon)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationCode++, n);

    }
}
