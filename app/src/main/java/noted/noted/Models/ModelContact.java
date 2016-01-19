package noted.noted.Models;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.jar.Manifest;


/**
 * Created by noy on 09/01/2016.
 */
public class ModelContact {

    List<Contact> contactsList;
    public static final String DEFAULT_PHONE_PREFIX = "+972";

    public void init(final Context context) {
        class GetContactsAsyncTask extends AsyncTask<String,String,List<Contact>> {
            @Override
            protected List<Contact> doInBackground(String... params) {
                return getContactsFromDevice(context);
            }

            @Override
            protected void onPostExecute(List<Contact> contactList) {
                super.onPostExecute(contactList);
                contactsList = contactList;
            }
        }

        GetContactsAsyncTask task = new GetContactsAsyncTask();
        task.execute();
    }

    private List<Contact> getContactsFromDevice(Context context) {
        List<Contact> list = new LinkedList<Contact>();

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            cursor.moveToFirst();
            do {
                String idContact = cursor.getString(contactIdIdx);
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);

                // In future will pick the user country code somehow
                if (!phoneNumber.startsWith("+")) {
                    phoneNumber = DEFAULT_PHONE_PREFIX + phoneNumber.substring(1);
                }
                Contact contact = new Contact(idContact, name,phoneNumber);
                list.add(contact);
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return list;
    }

    public List<Contact> getAllContactsList(Context context){
        return contactsList;
    }

    public Contact getContact(String phoneNumber){
        for(Contact contact : contactsList){
            if(contact.getPhoneNumber().equals(phoneNumber))
                return contact;
        }
        return null;
    }

    public Contact getContactById(String id){
        for(Contact contact : contactsList) {
                if (contact.getId().equals(id))
                    return contact;
        }
        return null;
    }

    public Contact getContactByName(String name){
        for(Contact contact : contactsList) {
            if (contact.getName().equals(name))
                return contact;
        }
        return null;
    }
}
