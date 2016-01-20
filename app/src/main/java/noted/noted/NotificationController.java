package noted.noted;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import noted.noted.Models.Contact;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

/**
 * Created by adi on 17-Jan-16.
 */
public class NotificationController {

    private static int notificationCode = 1;
    protected static final String TAG = "NotificationController";

    private final static NotificationController instance = new NotificationController();

    private NotificationController() {
    }

    public static NotificationController getInstance() {
        return instance;
    }

    public void notify(Intent intent, Context context) {
        String id = intent.getStringExtra(Utils.NOTE_ID_PARAM);
        String from = intent.getStringExtra(Utils.NOTE_FROM_PARAM);

        // Update this note as shown so it won't be shown again on boot
        Model.getInstance().getLocalNoteAsync(new Model.GetNoteListener() {
            @Override
            public void onResult(Note note) {
                note.setIsShown(true);
                Model.getInstance().updateLocalNoteAsync(new Model.SimpleSuccessListener() {
                    @Override
                    public void onResult(boolean result) {
                    }
                }, note);
            }
        }, id);

        // Get contact display name if exists
        Contact c = Model.getInstance().getContact(from);
        if (c != null) {
            from = c.getName();
        }

        String details = intent.getStringExtra(Utils.NOTE_DETAILS_PARAM);
        Log.d(TAG,"" + id + " " + from + " " + details);
        notify(id, from, details, context);
    }

    /*
        Push notification with note data
     */
    public void notify(String id, String from, String details, Context context) {
        Intent notificationIntent = new Intent(context, ViewNoteActivity.class);
        notificationIntent.putExtra(Utils.NOTE_ID_PARAM, id);

        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, 0);

        Notification n = new Notification.Builder(context)
                .setContentTitle(from)
                .setContentText(details)
                .setSmallIcon(R.mipmap.notedicon)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationCode++, n);
    }
}
