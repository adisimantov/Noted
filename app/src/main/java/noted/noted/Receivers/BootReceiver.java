package noted.noted.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import noted.noted.Models.Model;
import noted.noted.Models.Note;
import noted.noted.Services.GeofenceNoteService;

/**
 * Created by adi on 13-Jan-16.
 */
public class BootReceiver extends BroadcastReceiver {

    protected static final String TAG = "BootReceiver";

    /*
    Syncs the local and remote notes and sets condition based notifications
    for each note in the local db that wasn't shown yet
    By time - sets notification alarm for requested time
    By location - sets geofence for requested location
 */
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Start sync notes alarm
            AlarmReceiver.getInstance().init(context);

            Model.getInstance().getReceivedLocalNotesAsync(new Model.GetNotesListener() {
                @Override
                public void onResult(List<Note> notes) {
                    Log.d(TAG, "syncing data....");
                    List<String> geoNotes = new ArrayList<String>();

                    for (Note note : notes) {
                        if (note.getTimeToShow() != null) {
                            NotificationAlarmReceiver.setAlarm(context, note);

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
            }, Model.getInstance().getCurrUser().getPhoneNumber(), false);
        }
    }
}