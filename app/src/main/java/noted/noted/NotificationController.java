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

    private  static int notificationCode = 1;

    private final static NotificationController instance = new NotificationController();

    private  NotificationController(){

    }

    public static NotificationController getInstance(){

        return instance;
    }

    public void notify(Intent intent, Context context){
        String id = intent.getStringExtra("noteID");
        String from = intent.getStringExtra("noteFrom");
        Model.getInstance().getLocalNoteAsync(new Model.GetNoteListener() {
            @Override
            public void onResult(Note note) {
                note.setIsShown(true);
                Model.getInstance().updateLocalNoteAsync(new Model.SimpleSuccessListener() {
                    @Override
                    public void onResult(boolean result) {
                        Log.d("UPDATE IS SHOWN","" + result);
                    }
                },note);
            }
        },id);
        Contact c = Model.getInstance().getContact(from);
        if (c != null) {
            from = c.getName();
        }

        String details = intent.getStringExtra("noteDetails");
        notify(from,details,id,context);
    }


    public void notify(String from, String details, String id, Context context){
        Log.d("TIME OF notify", Model.getInstance().getCurrentTimestamp());
        
        Intent notificationIntent = new Intent(context, ViewNoteActivity.class);
        notificationIntent.putExtra("note_id", id);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(context)
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
