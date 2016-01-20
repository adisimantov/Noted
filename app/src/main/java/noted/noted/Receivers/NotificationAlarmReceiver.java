package noted.noted.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import noted.noted.Models.Model;
import noted.noted.Models.Note;
import noted.noted.NotificationController;
import noted.noted.Utils;

/**
 * Created by adi on 14-Jan-16.
 */
public class NotificationAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";
    private static int broadcastCode = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Notify user with received alarm
        NotificationController.getInstance().notify(intent, context);
    }

    /*
        Sets notification alarm for the requested time in the note
     */
    public static void setAlarm(Context context, Note note) {
        Intent alarmIntent = new Intent(context, NotificationAlarmReceiver.class);
        alarmIntent.putExtra(Utils.NOTE_ID_PARAM, note.getId());
        alarmIntent.putExtra(Utils.NOTE_FROM_PARAM, note.getFrom());
        alarmIntent.putExtra(Utils.NOTE_DETAILS_PARAM, note.getDetails());
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, broadcastCode++, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Get wanted time in miliseconds and set an alarm for this note
        long miliseconds = Model.getInstance().getMilisFromDateString(note.getTimeToShow(), Model.APP_DEFAULT_DATE_FORMAT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, miliseconds, pendingIntent);
    }
}
