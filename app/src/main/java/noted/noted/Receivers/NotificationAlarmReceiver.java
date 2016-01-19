package noted.noted.Receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import java.util.Calendar;
import java.util.logging.Handler;

import noted.noted.MainActivity;
import noted.noted.Models.Model;
import noted.noted.Models.Note;
import noted.noted.NotificationController;
import noted.noted.R;

/**
 * Created by adi on 14-Jan-16.
 */
public class NotificationAlarmReceiver extends BroadcastReceiver {

    static  int broadcastCode = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("NotificationAlarmRecei", "again");
        Log.d("NotificationAlarmRecei", Model.getInstance().getCurrentTimestamp());
    /*    String id = intent.getStringExtra("noteID");
        String from = intent.getStringExtra("noteFrom");
        String details = intent.getStringExtra("noteDetails");

        NotificationController.getInstance().notify(from,details,id,context);
    */
        NotificationController.getInstance().notify(intent, context);
    }

    public void setAlarm(Context context, Note note)
    {
        Log.d("NotificationAlarmRecei", "set alarm notification!");

        Intent alarmIntent = new Intent(context, NotificationAlarmReceiver.class);
        alarmIntent.putExtra("noteID", note.getId());
        alarmIntent.putExtra("noteFrom", note.getFrom());
        alarmIntent.putExtra("noteDetails", note.getDetails());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                broadcastCode++, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Log.d("NotificationAlarmRecei","TIME NOTE " +note.getId()+ " " + note.getTimeToShow());
        long miliseconds = Model.getInstance().getMilisFromDateString(note.getTimeToShow(),
                Model.APP_DEFAULT_DATE_FORMAT);
        Log.d("NotificationAlarmRecei","MILISECONDS NOTE " + note.getId()+ " " + miliseconds);
        Log.d("NotificationAlarmRecei","DATE FROM MILLIS NOTE " + note.getId() + " " + Model.getInstance().getDateStringFromMilis(miliseconds,Model.APP_DEFAULT_DATE_FORMAT));
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,miliseconds,pendingIntent);
    }
}
