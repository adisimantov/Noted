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
            new AlarmReceiver().SetAlarm(context);
        }

        //TODO: go over local notes
    }
}