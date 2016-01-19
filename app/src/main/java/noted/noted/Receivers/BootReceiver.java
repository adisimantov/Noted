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

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            new AlarmReceiver().setAlarm(context);

            Model.getInstance().getReceivedLocalNotesAsync(new Model.GetNotesListener() {
                @Override
                public void onResult(List<Note> notes) {
                    Log.d("boot", "syncing data....");
                    List<String> geoNotes = new ArrayList<String>();

                    for (Note note : notes) {
                        if (note.getTimeToShow() != null) {
                            new NotificationAlarmReceiver().setAlarm(context, note);

                        } else if (note.getLocationToShow() != null) {
                            geoNotes.add(note.getId());
                        }
                    }

                    if (geoNotes.size() > 0) {
                        Log.d("Alarm", "starting service");
                        Intent intentS = new Intent(context, GeofenceNoteService.class);
                        intentS.putStringArrayListExtra(GeofenceNoteService.NOTE_PARAM_NAME, (ArrayList<String>) geoNotes);
                        context.startService(intentS);
                    }
                }
            }, Model.getInstance().getCurrUser().getPhoneNumber());
        }
    }
}