package noted.noted.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import noted.noted.GeofenceController;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

/**
 * Created by adi on 13-Jan-16.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            new AlarmReceiver().setAlarm(context);

            Model.getInstance().getAllLocalNotesAsync(new Model.GetNotesListener() {
                @Override
                public void onResult(List<Note> notes) {
                    Log.d("boot", "syncing data....");
                    GeofenceController.getInstance().init(context);
                    GeofenceController.getInstance().connectToApiClient();
                    for (Note note : notes) {
                        // If wasn't shown already before boot
                        if (!note.isShown()) {
                            if (note.getTimeToShow() != null) {
                                new NotificationAlarmReceiver().setAlarm(context, note);

                            } else if (note.getLocationToShow() != null) {

                                GeofenceController.getInstance().addGeofence(note);
                            }
                        }
                    }
                    GeofenceController.getInstance().disconnectApiClient();
                }
            });
        }
    }
}