package noted.noted.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import noted.noted.Models.Model;
import noted.noted.Models.Note;
import noted.noted.Services.GeofenceNoteService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Model.getInstance().init(context);
        Model.getInstance().syncNotesFromServer(new Model.GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                Log.d("Alarm", "Syncing Data....");
                List<String> geoNotes = new ArrayList<String>();

                for (Note note : notes) {
                    if (note.getTimeToShow() != null) {
                        new NotificationAlarmReceiver().setAlarm(context, note);

                    } else if (note.getLocationToShow() != null) {
                        geoNotes.add(note.getId());
                    }
                }

                if (geoNotes.size() > 0) {
                    Log.d("Alarm","starting service");
                    Intent intentS = new Intent(context, GeofenceNoteService.class);
                    intentS.putStringArrayListExtra(GeofenceNoteService.NOTE_PARAM_NAME,(ArrayList<String>)geoNotes);
                    context.startService(intentS);
                }
            }
        });
    }

    public void setAlarm(final Context context) {
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

