package noted.noted;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import noted.noted.Models.Contact;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class SendNoteActivity extends Activity {
    static final int PICK_CONTACT = 1;
    static final int PLACE_PICKER_RESULT = 2;

    private final static String RTL_CHAR = "\u200E";

    private final static String ID = "ID";
    private final static String FROM = "FROM";
    private final static String TO = "TO";
    private final static String DETAILS = "DETAILS";
    private final static String SENT_TIME = "SENT_TIME";
    private final static String TIME_TO_SHOW = "TIME_TO_SHOW";
    private final static String LOCATION_TO_SHOW = "LOCATION_TO_SHOW";

    ImageButton contactButton;
    TextView contactTo;
    TextView details;
    Button sendBtn;
    Spinner spinner;
    DateEditText det;
    TimeEditText tet;
    LatLng locLat;
    TextView location;
    FrameLayout timeFL;
    FrameLayout locationFL;
    ProgressBar acIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_note);

        contactButton = (ImageButton) findViewById(R.id.sentViewContactBtn);
        contactTo = (TextView) findViewById(R.id.sentViewTo);
        sendBtn = (Button) findViewById(R.id.sentBtn);
        details = (TextView) findViewById(R.id.sentViewDetails);
        spinner = (Spinner) findViewById(R.id.typeSpinner);
        det = (DateEditText) findViewById(R.id.add_note_date);
        tet = (TimeEditText) findViewById(R.id.add_note_time);
        location = (TextView) findViewById(R.id.add_note_location);
        timeFL = (FrameLayout) findViewById(R.id.timeLayout);
        locationFL = (FrameLayout) findViewById(R.id.locationLayout);
        acIndicator = (ProgressBar) findViewById(R.id.activity_indicator);
        acIndicator.setVisibility(View.GONE);

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteAdress;
                final Note note;
                acIndicator.setVisibility(View.VISIBLE);

                //Check contact
                if (contactTo.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.enter_contact_message), Toast.LENGTH_SHORT);
                    toast.show();
                    acIndicator.setVisibility(View.GONE);
                    return;
                }

                if (spinner.getSelectedItem().equals(getResources().getString(R.string.time))) {
                    note = new Note(Model.getInstance().getCurrUser().getPhoneNumber(),
                            Model.getInstance().getPhoneNumberWithCountry(contactTo.getText().toString()),
                            details.getText().toString(), Model.getInstance().getCurrentTimestamp(),
                            det.getText().toString() + " " + tet.getText().toString(), null, null);
                } else {
                    //Check location
                    noteAdress = location.getText().toString();

                    if (spinner.getSelectedItem().equals(getResources().getString(R.string.location)) &&
                            noteAdress.equals(getResources().getString(R.string.choose_location))) {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.choose_location_message), Toast.LENGTH_SHORT);
                        toast.show();
                        acIndicator.setVisibility(View.GONE);
                        return;
                    }

                    note = new Note(Model.getInstance().getCurrUser().getPhoneNumber(),
                            Model.getInstance().getPhoneNumberWithCountry(contactTo.getText().toString()),
                            details.getText().toString(), Model.getInstance().getCurrentTimestamp(),
                            null, new noted.noted.Models.Location(locLat.latitude, locLat.longitude), noteAdress);
                }

                Model.getInstance().addLocalAndRemoteNote(note, new Model.AddNoteListener() {
                    @Override
                    public void onResult(boolean result, Note id) {
                        if (result) {
                            Intent intent = new Intent();
                            intent.putExtra(ID, note.getId());
                            intent.putExtra(FROM, note.getFrom());
                            intent.putExtra(TO, note.getTo());
                            intent.putExtra(DETAILS, note.getDetails());
                            intent.putExtra(SENT_TIME, note.getSentTime());
                            intent.putExtra(TIME_TO_SHOW, note.getTimeToShow());
                            intent.putExtra(LOCATION_TO_SHOW, "");
                            setResult(RESULT_OK, intent);
                            acIndicator.setVisibility(View.GONE);
                            finish();
                        } else {
                            acIndicator.setVisibility(View.GONE);
                            Toast toast = Toast.makeText(getApplicationContext(), "Check your network connection", Toast.LENGTH_SHORT);
                            toast.show();
                        }
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
                String item = spinner.getItemAtPosition(position).toString();
                switch (item) {
                    case (Utils.TIME):
                        timeFL.setVisibility(FrameLayout.VISIBLE);
                        locationFL.setVisibility(FrameLayout.GONE);
                        break;
                    case (Utils.LOCATION):
                        timeFL.setVisibility(FrameLayout.GONE);
                        locationFL.setVisibility(FrameLayout.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                acIndicator.setVisibility(View.VISIBLE);

                try {
                    startActivityForResult(builder.build(SendNoteActivity.this), PLACE_PICKER_RESULT);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
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
                    location.setText(RTL_CHAR + place.getName());
                    locLat = place.getLatLng();
                    acIndicator.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}
