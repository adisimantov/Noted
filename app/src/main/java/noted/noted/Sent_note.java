package noted.noted;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import noted.noted.Models.Contact;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class Sent_note extends Activity {
    static final int PICK_CONTACT = 1;
    static final int PLACE_PICKER_RESULT = 2;

    private final static String FROM = "FROM";
    private final static String TO = "TO";
    private final static String DETAILS = "DETAILS";
    private final static String SENT_TIME = "SENT_TIME";
    private final static String TIME_TO_SHOW = "TIME_TO_SHOW";
    private final static String LOCATION_TO_SHOW = "LOCATION_TO_SHOW";

    ImageButton  contactButton;
    TextView     contactTo;
    TextView     details;
    Button       sentBtn;
    Spinner      spinner;
    DateEditText det;
    TimeEditText tet;
    Button       locationBtn;
    TextView     location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_note);

        contactButton = (ImageButton) findViewById(R.id.sentViewContactBtn);
        contactTo = (TextView) findViewById(R.id.sentViewTo);
        sentBtn = (Button) findViewById(R.id.sentBtn);
        details = (TextView) findViewById(R.id.sentViewDetails);
        spinner = (Spinner) findViewById(R.id.typeSpinner);
        det = (DateEditText) findViewById(R.id.add_note_date);
        tet = (TimeEditText) findViewById(R.id.add_note_time);
        location = (TextView) findViewById(R.id.add_note_location);
        locationBtn = (Button) findViewById(R.id.add_note_location_btn);

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);

            }
        });

        sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Note note = new Note(Model.getInstance().getCurrUser().getPhoneNumber(),contactTo.getText().toString(),
                        details.getText().toString(),Model.getInstance().getCurrentGMTDate(),
                        det.getText().toString() + " " + tet.getText().toString(), null);
                Model.getInstance().addLocalAndRemoteNote(note, new Model.AddNoteListener() {
                    @Override
                    public void onResult(boolean result, Note id) {
                        Log.d("noy", "note add");
                        //, String timeToShow, String locationToShow) {

                        Intent intent = new Intent();
                        intent.putExtra(FROM,note.getFrom());
                        intent.putExtra(TO, note.getTo());
                        intent.putExtra(DETAILS,note.getDetails());
                        intent.putExtra(SENT_TIME,note.getSentTime());
                        intent.putExtra(TIME_TO_SHOW,note.getTimeToShow());
                        intent.putExtra(LOCATION_TO_SHOW,"");
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(Sent_note.this), PLACE_PICKER_RESULT);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
*/
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    startManagingCursor(c);
                    if (c.moveToFirst()) {
                        //inr\ id = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        int contactIdIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
                        String idContact = c.getString(contactIdIdx);


                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Contact contact = Model.getInstance().getContactByName(name);

                        if (contact != null) {
                            name = contact.getName();
                            String number = contact.getPhoneNumber();
                            contactTo.setText(number);
                        }
                    }
                }
                break;
            case (PLACE_PICKER_RESULT):
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    location.setText(place.getAddress());
                }
                    break;
        }

    }
}
