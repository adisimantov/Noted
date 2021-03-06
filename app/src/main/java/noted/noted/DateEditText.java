package noted.noted;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by noy on 17/01/2016.
 */
public class DateEditText extends EditText implements DatePickerFragment.onDateSetListener {
    int year;
    int month;
    int day;

    public DateEditText(Context context) {
        super(context);
        init();
    }

    public DateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setInputType(0);
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        setText();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            DatePickerFragment dpf = new DatePickerFragment();
            dpf.setOnDateSetListener(this);
            dpf.setDate(year, month, day);
            dpf.show(((Activity) getContext()).getFragmentManager(), "TAG");
        }
        return true;
    }

    public void setText() {
        Log.d("DateEditText",(((month + 1) < 10) ? "0" : ""));
        Log.d("DateEditText",month + "");
        setText("" + ((day < 10) ? "0" : "") + day + "/" + (((month + 1) < 10) ? "0" : "") + (month + 1) + "/" + year);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;

        setText();
    }
}
