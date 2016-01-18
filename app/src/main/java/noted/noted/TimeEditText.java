package noted.noted;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by noy on 17/01/2016.
 */
public class TimeEditText extends EditText implements TimePickerFragment.onTimeSetListener {
    int hour;
    int minute;

    public TimeEditText(Context context) {
        super(context);
        init();
    }

    public TimeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setInputType(0);
        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR);
        minute = cal.get(Calendar.MINUTE);
        setText("" + hour + ":" + minute);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            TimePickerFragment tpf = new TimePickerFragment();
            tpf.setOnTimeSetListener(this);
            tpf.setTime(hour,minute);
            tpf.show(((Activity)getContext()).getFragmentManager(),"TAG");
        }
        return true;
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        setText("" + this.hour + ":" + this.minute);
    }
}
