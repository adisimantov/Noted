package noted.noted;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import noted.noted.Models.Contact;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class ViewNoteActivity extends Activity {
    TextView from;
    TextView to;
    TextView details;
    TextView sentTime;
    TextView timeOrLoc;

    public static final String RTL_CHAR = "\u200E";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        from = (TextView) findViewById(R.id.noteFromDisplay);
        to = (TextView) findViewById(R.id.noteToDisplay);
        details = (TextView) findViewById(R.id.noteDetailsDisplay);
        sentTime = (TextView) findViewById(R.id.noteSentTimeDisplay);
        timeOrLoc = (TextView) findViewById(R.id.noteTimeOrLocDisplay);

        Intent intent = getIntent();

        String id = intent.getStringExtra("note_id");
        Model.getInstance().getLocalNoteAsync(new Model.GetNoteListener() {
            @Override
            public void onResult(Note note) {
                String fromString = note.getFrom();

                Contact contactFrom = Model.getInstance().getContact(fromString);
                if (contactFrom != null) {
                    fromString = contactFrom.getName();
                }


                String toString = note.getTo();
                Contact contactTo = Model.getInstance().getContact(toString);
                if (contactTo != null) {
                    toString = contactTo.getName();
                }


                to.setText(RTL_CHAR + toString);
                from.setText(RTL_CHAR + fromString);
                details.setText(RTL_CHAR + note.getDetails());
                sentTime.setText(RTL_CHAR + note.getSentTime());
                if (note.getTimeToShow() != null) {
                    timeOrLoc.setText(RTL_CHAR + note.getTimeToShow());
                } else if (note.getLocationToShowName() != null) {
                    timeOrLoc.setText(RTL_CHAR + note.getLocationToShowName());
                }
            }
        }, id);
    }
}
