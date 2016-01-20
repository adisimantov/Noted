package noted.noted.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import noted.noted.Models.Model;
import noted.noted.Models.Note;
import noted.noted.Utils;

/**
 * Created by Adi on 19-Jan-16.
 * Connects to google location api in order to add or remove geofences of notes the user received
 */
public class GeofenceNoteService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "GeofenceNoteService";

    public static final String NOTE_PARAM_NAME = "GEO_NOTES";
    public static final String REMOVE_NOTE_PARAM_NAME = "GEO_NOTES_TO_REMOVE";
    public static final float GEOFENCE_RADIUS_IN_METERS = 100;

    private Context context;
    private List<String> geoNotes;
    private List<String> geoNotesToRemove;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        if (context == null) {
            context = getApplicationContext();
            Model.getInstance().init(context);
        }
        buildGoogleApiClient();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        this.geoNotes = intent.getStringArrayListExtra(NOTE_PARAM_NAME);
        this.geoNotesToRemove = intent.getStringArrayListExtra(REMOVE_NOTE_PARAM_NAME);
        mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Bye bye");
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (geoNotes != null) {
            for (String noteID : geoNotes) {
                Note currNote = Model.getInstance().getLocalNote(noteID);
                if (currNote.getLocationToShow() != null) {
                    addGeofence(currNote);
                }
            }
        }

        if (geoNotesToRemove != null) {
            removeGeofence(geoNotesToRemove);
        }

        stopSelf();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("GoogleApi", "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("GoogleApi", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    public void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    public void addGeofence(Note note) {
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(note),
                    // This pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent(note)
            );

            Log.d(TAG, "addGeofences " + note.getId() + " " + note.getDetails() + " " + note.getLocationToShow().toString());
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    public void removeGeofence(List<String> listToRemove) {
        try {
            LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, listToRemove);
            Log.i(TAG, "removeGeofence");
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    /**
     * Build geofence for note
     */
    public List<Geofence> createGeofenceList(Note note) {

        List<Geofence> geofenceList = new ArrayList<Geofence>();
        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this geofence.
                .setRequestId(note.getId())
                        // Set the circular region of this geofence.
                .setCircularRegion(
                        note.getLocationToShow().getLatitudeToShow(),
                        note.getLocationToShow().getLongitudeToShow(),
                        GEOFENCE_RADIUS_IN_METERS
                )
                // We will remove the geofence ourself when it is pushed
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                // Set the transition alert at entering
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build());

        return geofenceList;
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest(Note note) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(createGeofenceList(note));

        // Return a GeofencingRequest.
        return builder.build();
    }

    /**
     * Location Services issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent(Note note) {

        Intent intent = new Intent(context, GeofenceTransitionService.class);
        intent.putExtra(Utils.NOTE_ID_PARAM, note.getId());
        intent.putExtra(Utils.NOTE_FROM_PARAM, note.getFrom());
        intent.putExtra(Utils.NOTE_DETAILS_PARAM, note.getDetails());

        return PendingIntent.getService(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e("GoogleApi", "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }
}
