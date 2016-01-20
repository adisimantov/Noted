package noted.noted;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import noted.noted.Models.Contact;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class ViewNoteActivity extends Activity {
    TextView from;
    TextView to;
    TextView details;
    TextView sentTime;
    TextView timeOrLoc;

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
        String id = intent.getStringExtra(Utils.NOTE_ID_PARAM);

        Model.getInstance().getLocalNoteAsync(new Model.GetNoteListener() {
            @Override
            public void onResult(Note note) {
                if (note != null) {
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

                    to.setText(Utils.LTR_CHAR + toString);
                    from.setText(Utils.LTR_CHAR + fromString);
                    details.setText(Utils.LTR_CHAR + note.getDetails());
                    sentTime.setText(Utils.LTR_CHAR + note.getSentTime());
                    if (note.getTimeToShow() != null) {
                        timeOrLoc.setText(Utils.LTR_CHAR + note.getTimeToShow());
                    } else if (note.getLocationToShowName() != null) {
                        timeOrLoc.setText(Utils.LTR_CHAR + note.getLocationToShowName());
                    }
                } else {
                    finish();
                    Toast toast = Toast.makeText(getApplicationContext(),"Error getting note",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }, id);
    }
}
