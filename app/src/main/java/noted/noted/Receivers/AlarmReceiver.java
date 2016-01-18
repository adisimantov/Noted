package noted.noted.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import noted.noted.GeofenceController;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        Model.getInstance().init(context);
        Model.getInstance().syncNotesFromServer(new Model.GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                Log.d("alarm", "syncing data....");
                GeofenceController.getInstance().init(context);
                GeofenceController.getInstance().connectToApiClient();
                for (Note note : notes) {
                    if (note.getTimeToShow() != null) {
                        new NotificationAlarmReceiver().setAlarm(context, note);

                    } else if (note.getLocationToShow() != null) {

                        GeofenceController.getInstance().addGeofence(note);
                    }
                }
                GeofenceController.getInstance().disconnectApiClient();
            }
        });
    }


    public void setAlarm(final Context context) {
        Log.d("alarm", "set alarm sync");
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
                //AlarmManager.INTERVAL_HALF_HOUR,
                5 * 60 * 1000,
                recurringDownload);
    }
}

