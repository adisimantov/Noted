package noted.noted.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Model.getInstance().syncNotesFromServer(new Model.SyncNotesListener() {
            @Override
            public void onResult(List<Note> data) {
                //TODO: set notifications by time/location
                Log.d("alarm", "syncing data......" );
            }
        });
    }
    public void SetAlarm(Context context)
    {
        Intent downloader = new Intent(context, AlarmReceiver.class);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
                0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR,
                recurringDownload);
    }
}

