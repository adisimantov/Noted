package noted.noted.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Anna on 30-Dec-15.
 */
public class Model {
    private final static Model instance = new Model();
    private Context context = null;
    private SharedPreferences sharedPrefs;
    private final static String PREF_FILE = "PREF_FILE";
    private final static String LAST_SYNC_TIME = "LAST_SYNC_TIME";

    public final static String APP_DEFAULT_DATE_FORMAT = "dd.MM.yyyy hh:mm";
    public final static String APP_DEFAULT_TIMESTAMP_FORMAT = "dd.MM.yyyy hh:mm:ss";

    ModelSql local = new ModelSql();
    ModelParse remote = new ModelParse();
    ModelContact contacts = new ModelContact();

    private Model(){}

    public static Model getInstance(){
        return instance;
    }

    public void init(Context context){
        if (this.context == null) {
            this.context = context;
            sharedPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
            remote.init(context);
            local.init(context);
            contacts.init(context);
        }
    }

    public String getCurrentGMTDate() {
        Calendar cal = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat(remote.DEFAULT_DATE_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone(remote.DEFAULT_TIME_ZONE));
        return formatter.format((cal.getTime()));
    }

    public String getCurrentTimestamp() {
        Calendar cal = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat(APP_DEFAULT_TIMESTAMP_FORMAT);
        return formatter.format((cal.getTime()));
    }

    public long getMilisFromDateString(String date,String dateFormat) {
        try {
            Date parsedDate = new SimpleDateFormat(dateFormat).parse(date);
            return parsedDate.getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    public String getDateStringFromMilis(long miliseconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(miliseconds);
        return formatter.format(calendar.getTime());
    }

    // Shared Preferences
    public String getLastSyncTime() {
        return sharedPrefs.getString(LAST_SYNC_TIME, null);
    }

    public void setLastSyncTime(String timestamp) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(LAST_SYNC_TIME, timestamp);
        editor.commit();
    }

    public interface GetNotesListener{
        public void onResult(List<Note> notes);
    }

    public interface GetNoteListener {
        public void onResult(Note note);
    }

    public interface SimpleSuccessListener {
        public void onResult(boolean result);
    }

    // Local database

    public Note getLocalNote(String id){
        return local.getNote(id);
    }

    public long addLocalNote(Note note){
        return local.addNote(note);
    }
    public int updateLocalNote(Note note){
        return local.updateNote(note);
    }
    public int deleteLocalNote(String noteId){
        return local.deleteNote(noteId);
    }

    public void getLocalNoteAsync(final GetNoteListener listener, final String id){
        class GetNoteAsyncTask extends AsyncTask<String,String,Note> {
            @Override
            protected Note doInBackground(String... params) {
                return local.getNote(id);
            }

            @Override
            protected void onPostExecute(Note note) {
                super.onPostExecute(note);
                listener.onResult(note);
            }
        }

        GetNoteAsyncTask task = new GetNoteAsyncTask();
        task.execute();
    }

    public void addLocalNoteAsync(final SimpleSuccessListener listener, final Note note){
        class AddNoteAsyncTask extends AsyncTask<String,String,Long> {
            @Override
            protected Long doInBackground(String... params) {
                return local.addNote(note);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);
                listener.onResult(id > -1);
            }
        }

        AddNoteAsyncTask task = new AddNoteAsyncTask();
        task.execute();
    }

    public void updateLocalNoteAsync(final SimpleSuccessListener listener, final Note note){
        class UpdateNoteAsyncTask extends AsyncTask<String,String,Integer> {
            @Override
            protected Integer doInBackground(String... params) {
                return local.updateNote(note);
            }

            @Override
            protected void onPostExecute(Integer id) {
                super.onPostExecute(id);
                listener.onResult(id > -1);
            }
        }

        UpdateNoteAsyncTask task = new UpdateNoteAsyncTask();
        task.execute();
    }

    public void deleteLocalNoteAsync(final SimpleSuccessListener listener, final String noteId){
        class DeleteNoteAsyncTask extends AsyncTask<String,String,Integer> {
            @Override
            protected Integer doInBackground(String... params) {
                return local.deleteNote(noteId);
            }

            @Override
            protected void onPostExecute(Integer id) {
                super.onPostExecute(id);
                listener.onResult(id > -1);
            }
        }

        DeleteNoteAsyncTask task = new DeleteNoteAsyncTask();
        task.execute();
    }

    public void getAllLocalNotesAsync(final GetNotesListener listener) {
        class GetNotesAsyncTask extends AsyncTask<String,String,List<Note>> {
            @Override
            protected List<Note> doInBackground(String... params) {
                return local.getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                listener.onResult(notes);
            }
        }

        GetNotesAsyncTask task = new GetNotesAsyncTask();
        task.execute();
    }

    public void getReceivedLocalNotesAsync(final GetNotesListener listener, final String receivedPhone){
        class GetNotesAsyncTask extends AsyncTask<String,String,List<Note>> {
            @Override
            protected List<Note> doInBackground(String... params) {
                return local.getReceivedNotes(receivedPhone);
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                listener.onResult(notes);
            }
        }

        GetNotesAsyncTask task = new GetNotesAsyncTask();
        task.execute();
    }

    public void getSentLocalNotesAsync(final GetNotesListener listener, final String sentPhone){
        class GetNotesAsyncTask extends AsyncTask<String,String,List<Note>> {
            @Override
            protected List<Note> doInBackground(String... params) {
                return local.getSentNotes(sentPhone);
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                listener.onResult(notes);
            }
        }

        GetNotesAsyncTask task = new GetNotesAsyncTask();
        task.execute();
    }

    // Remote database
    // Users
    public void logIn(User user, SimpleSuccessListener listener) {
        remote.userLogIn(user, listener);
    }

    public void signIn(User user, SimpleSuccessListener listener) {
        remote.userSignUp(user, listener);
    }

    public void signOrLogin(final User user , final SimpleSuccessListener listener) {
        remote.userLogIn(user, new SimpleSuccessListener() {
            @Override
            public void onResult(boolean result) {
                if (result) {
                    listener.onResult(true);
                } else {
                    remote.userSignUp(user, new SimpleSuccessListener() {
                        @Override
                        public void onResult(boolean result) {
                            listener.onResult(result);
                        }
                    });
                }
            }
        });
    }

    public void logOut(SimpleSuccessListener listener) {
        remote.userLogOut(listener);
    }

    public User getCurrUser() {
        return remote.getCurrUser();
    }

    // Notes
    public void getNote(String id, GetNoteListener listener) {
        remote.getNote(id, listener);
    }

    public void addRemoteNote(Note note, AddNoteListener listener) {
        remote.addNote(note, listener);
    }

    public void updateRemoteNote(Note note, SimpleSuccessListener listener) {
        remote.updateNote(note, listener);
    }

    public interface AddNoteListener {
        public void onResult(boolean result, Note id);
    }

    public void addLocalAndRemoteNote(final Note note, final AddNoteListener listener) {
        addRemoteNote(note, new AddNoteListener() {
            @Override
            public void onResult(boolean result, final Note noteRemote) {
                if (result) {
                    addLocalNoteAsync(new SimpleSuccessListener() {
                        @Override
                        public void onResult(boolean result) {
                            listener.onResult(result, noteRemote);
                        }
                    },note);
                } else {
                    listener.onResult(false, noteRemote);
                }
            }
        });
    }

    public interface SyncNotesListener {
        public void onResult(List<Note> data);
    }

    public void syncNotesFromServer(final SyncNotesListener listener){
        String timestamp = getLastSyncTime();
        String to = getCurrUser().getPhoneNumber();
        remote.getAllNotes(new GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                for (Note note : notes) {
                    local.addNote(note);
                }
                listener.onResult(notes);
                setLastSyncTime(getCurrentGMTDate());
            }
        }, timestamp, to);
    }

    // Contacts
    public List<Contact> getAllContacts(){
        return contacts.getAllContacts(context);
    }

    public Contact getContact(String phoneNumber){
        return contacts.getContact(phoneNumber);
    }

    public Contact getContactById(String id){
        return contacts.getContactById(id);
    }

    public Contact getContactByName(String name){
        return contacts.getContactByName(name);
    }

}
