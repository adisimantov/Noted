package noted.noted;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import noted.noted.Models.Contact;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class NoteActivity extends Activity {
    TextView from;
    TextView details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        from = (TextView) findViewById(R.id.noteContactDisplay);
        details = (TextView) findViewById(R.id.noteContactDisplay);

        Intent intent = getIntent();

        String id = intent.getStringExtra("note_id");
        Note note = Model.getInstance().getLocalNote(id);
        Contact contact = Model.getInstance().getContact(note.getFrom());
        from.setText(contact.getName());
        details.setText(note.getDetails());
    }
}
