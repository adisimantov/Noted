package noted.noted.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import noted.noted.NotificationController;

/**
 * Listener for geofence transition changes.
 * <p/>
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a notification
 * as the output.
 */
public class GeofenceTransitionService extends IntentService {

    protected static final String TAG = "GeofenceTransitionSrv";
    static int notificationCode = 1000;

    public GeofenceTransitionService() {
        super(TAG);
    }

    /**
     * Handles incoming intents.
     *
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, "error " + geofencingEvent.getErrorCode() + " " + errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Create notification for this location
            NotificationController.getInstance().notify(intent, this);

            // Remove used geofences
            List<String> geofencesToRemove = new ArrayList<>();
            for (Geofence geofence : triggeringGeofences) {
                geofencesToRemove.add(geofence.getRequestId());
            }

            if (geofencesToRemove.size() > 0) {
                Context context = getApplicationContext();
                Intent intentS = new Intent(context, GeofenceNoteService.class);
                intentS.putStringArrayListExtra(GeofenceNoteService.REMOVE_NOTE_PARAM_NAME, (ArrayList<String>) geofencesToRemove);
                context.startService(intentS);
            }
        } else {
            // Log the error.
            Log.e(TAG, "errorHandleIntent");
        }
    }
}