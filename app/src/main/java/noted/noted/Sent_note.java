package noted.noted;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
    LatLng       locLat;
    TextView     location;
    FrameLayout  timeFL;
    FrameLayout  locationFL;

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
        timeFL = (FrameLayout) findViewById(R.id.timeLayout);
        locationFL = (FrameLayout) findViewById(R.id.locationLayout);

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
                String noteAdress;

                Log.d("noy","Get Item: " + spinner.getSelectedItem().toString());
                //Check contact
                if (contactTo.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter contact", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                //Check location
                noteAdress = location.getText().toString();

                if(spinner.getSelectedItem().equals("Location") && noteAdress.equals("Choose location"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose location", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                
                final Note note = new Note(Model.getInstance().getCurrUser().getPhoneNumber(),contactTo.getText().toString(),
                        details.getText().toString(),Model.getInstance().getCurrentTimestamp(),
                        det.getText().toString() + " " + tet.getText().toString(), null, null);
                Model.getInstance().addLocalAndRemoteNote(note, new Model.AddNoteListener() {
                    @Override
                    public void onResult(boolean result, Note id) {

                        //, String timeToShow, String locationToShow) {

                        Intent intent = new Intent();
                        intent.putExtra(FROM, note.getFrom());
                        intent.putExtra(TO, note.getTo());
                        intent.putExtra(DETAILS, note.getDetails());
                        intent.putExtra(SENT_TIME, note.getSentTime());
                        intent.putExtra(TIME_TO_SHOW, note.getTimeToShow());
                        intent.putExtra(LOCATION_TO_SHOW, "");
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
                String item = spinner.getItemAtPosition(position).toString();
                switch (item) {
                    case ("Time"):
                        timeFL.setVisibility(FrameLayout.VISIBLE);
                        locationFL.setVisibility(FrameLayout.INVISIBLE);
                        break;
                    case ("Location"):
                        timeFL.setVisibility(FrameLayout.INVISIBLE);
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

                try {
                    startActivityForResult(builder.build(Sent_note.this), PLACE_PICKER_RESULT);
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
                    locLat = place.getLatLng();
                }
                    break;
        }
    }
}
