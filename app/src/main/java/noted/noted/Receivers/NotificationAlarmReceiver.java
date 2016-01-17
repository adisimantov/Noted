package noted.noted.Receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import noted.noted.MainActivity;
import noted.noted.Models.Note;
import noted.noted.NotificationController;
import noted.noted.R;

/**
 * Created by adi on 14-Jan-16.
 */
public class NotificationAlarmReceiver extends BroadcastReceiver {

    static  int broadcastCode = 1;
    static  int notificationCode = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("notificationAlarm", "again");
        String id = intent.getStringExtra("noteID");
        String from = intent.getStringExtra("noteFrom");
        String details = intent.getStringExtra("noteDetails");

        NotificationController.getInstance().notify(from,details,id,context);

/*
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(context)
                .setContentTitle(from)
                .setContentText(details)
                .setSmallIcon(R.mipmap.notedicon)
                .setAutoCancel(true).setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationCode++, n);
*/

    }

    public void SetAlarm(Context context, Note note)
    {
        Log.d("ntf", "set alarm notification!!!");
        Intent alarmIntent = new Intent(context, NotificationAlarmReceiver.class);
        alarmIntent.putExtra("noteID", note.getId());
        alarmIntent.putExtra("noteFrom", note.getFrom());
        //TODO remove!
        alarmIntent.putExtra("noteDetails", note.getDetails() + "alarm");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                broadcastCode++, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //TODO: change time to long!
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 30);
        alarmManager.set(AlarmManager.RTC_WAKEUP,  c.getTimeInMillis() /*new Long(note.getTimeToShow())*/, pendingIntent);
    }
}
