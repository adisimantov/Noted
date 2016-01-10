package noted.noted.Models;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.LinkedList;
import java.util.List;
import java.util.jar.Manifest;


/**
 * Created by noy on 09/01/2016.
 */
public class ModelContact {
    List<Contact> contactsList;

    public List<Contact> getAllContacts(Context context){
        contactsList = new LinkedList<Contact>();

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
            cursor.moveToFirst();
            do {
                String idContact = cursor.getString(contactIdIdx);
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);
                Contact contact = new Contact(name,phoneNumber);
                contactsList.add(contact);
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return contactsList;
    }

    public Contact getContact(String phoneNumber){
        for(Contact contact : contactsList){
            if(contact.getPhoneNumber().equals(phoneNumber))
                return contact;
        }
        return null;
    }
}
