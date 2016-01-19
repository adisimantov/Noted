package noted.noted;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

/**
 * Created by noy on 17/01/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    int hour;
    int min;

    public void setTime(int h, int m) {
        hour = h;
        min = m;
    }

    interface onTimeSetListener {
        public void onTimeSet(int hourOfDay, int minute);
    }

    private onTimeSetListener listener;

    public void setOnTimeSetListener(onTimeSetListener ls) {
        listener = ls;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, hour, min, true);
        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.onTimeSet(hourOfDay, minute);
    }
}
