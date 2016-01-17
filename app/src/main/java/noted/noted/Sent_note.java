package noted.noted;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import noted.noted.Models.Contact;
import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class Sent_note extends Activity {
    static final int PICK_CONTACT=1;

    ImageButton contactButton;
    TextView    contactTo;
    TextView    details;
    Button      sentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_note);

        contactButton = (ImageButton) findViewById(R.id.sentViewContactBtn);
        contactTo = (TextView) findViewById(R.id.sentViewTo);
        sentBtn = (Button) findViewById(R.id.sentBtn);
        details = (TextView) findViewById(R.id.sentViewDetails);

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
                //from to details senttime
                Note note = new Note("0544783455", contactTo.getText().toString(), details.getText().toString(),"16/01/2016");
                Model.getInstance().addLocalAndRemoteNote(note, new Model.AddNoteListener() {
                    @Override
                    public void onResult(boolean result, Note id) {
                        Log.d("noy", "note add");
                        finish();
                    }
                });
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
                        /*int phoneIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String number = c.getString(phoneIdx);*/

                        Contact contact = Model.getInstance().getContactByName(name);



                        if (contact != null) {
                            Log.d("noy", contact.getName());
                            name = contact.getName();
                            String number = contact.getPhoneNumber();
                            contactTo.setText(number);
                        }
                    }
                }
                break;
        }

    }
}
