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

    protected static final String TAG = "AlarmReceiver";

    private static final AlarmReceiver instance = new AlarmReceiver();
    private static Context context;

    /*
        This class can't have a private constructor but it has static instance
         so when called it won't set the alarm if it is already set
    */
    public static AlarmReceiver getInstance() {
        return instance;
    }

    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            setAlarm();
        }
    }

    /*
        Syncs the local and remote notes and sets condition based notifications for each new note
        By time - sets notification alarm for requested time
        By location - sets geofence for requested location
     */
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Model.getInstance().init(context);
        Model.getInstance().syncNotesFromServer(new Model.GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                Log.d(TAG, "Syncing Data....");
                List<String> geoNotes = new ArrayList<String>();

                for (Note note : notes) {
                    if (note.getTimeToShow() != null) {
                        new NotificationAlarmReceiver().setAlarm(context, note);

                    } else if (note.getLocationToShow() != null) {
                        geoNotes.add(note.getId());
                    }
                }

                // Start a service for connecting to google location api with the location notes
                if (geoNotes.size() > 0) {
                    Intent intentS = new Intent(context, GeofenceNoteService.class);
                    intentS.putStringArrayListExtra(GeofenceNoteService.NOTE_PARAM_NAME, (ArrayList<String>) geoNotes);
                    context.startService(intentS);
                }
            }
        });
    }

    /*
        Start a timer that will sync the local db with notes from the server at every interval
     */
    public void setAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent receivedIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
                                    AlarmManager.INTERVAL_HALF_HOUR, receivedIntent);
    }
}

