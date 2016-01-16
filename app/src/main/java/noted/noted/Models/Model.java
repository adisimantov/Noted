package noted.noted.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import noted.noted.PreferenceManager;

/**
 * Created by Anna on 30-Dec-15.
 */
public class Model {
    private final static Model instance = new Model();
    private Context context = null;
    private SharedPreferences sharedPrefs;
    private final static String PREF_FILE = "PREF_FILE";
    private final static String LAST_SYNC_TIME = "LAST_SYNC_TIME";

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
        }
    }

    public String getLastSyncTime() {
        return sharedPrefs.getString(LAST_SYNC_TIME, null);
    }

    public void setLastSyncTime(String timestamp) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(LAST_SYNC_TIME, timestamp);
        editor.commit();
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

    public List<Note> getAllLocalNotes(){
        return local.getAllNotes();
    }

    public List<Note> getReceivedLocalNotes(String sentPhone){
        return local.getReceivedNotes(sentPhone);
    }

    public List<Note> getSentLocalNotes(String receivedPhone){
        return local.getSentNotes(receivedPhone);
    }

    // Remote database
    public interface LogInListener {
        public void onResult(boolean result);
    }

    public void logIn(User user, LogInListener listener) {
        remote.userLogIn(user, listener);
    }

    public interface SignUpListener {
        public void onResult(boolean result);
    }

    public void signIn(User user, SignUpListener listener) {
        remote.userSignUp(user, listener);
    }

    public interface LogOutListener {
        public void onResult(boolean result);
    }

    public void logOut(LogOutListener listener) {
        remote.userLogOut(listener);
    }


    public interface GetNotesListener{
        public void onResult(List<Note> notes);
    }

    public interface GetNoteListener {
        public void onResult(Note note);
    }

    public void getNote(String id, GetNoteListener listener) {
        remote.getNote(id, listener);
    }

    public interface AddNoteListener {
        public void onResult(boolean result, Note id);
    }

    public void addRemoteNote(Note note, AddNoteListener listener) {
        remote.addNote(note, listener);
    }

    public interface UpdateNoteListener {
        public void onResult(boolean result);
    }

    public void updateRemoteNote(Note note, UpdateNoteListener listener) {
        remote.updateNote(note, listener);
    }

    public void addLocalAndRemoteNote(final Note note, final AddNoteListener listener) {
        addRemoteNote(note, new AddNoteListener() {
            @Override
            public void onResult(boolean result, Note note) {
                if (result) {
                    if (addLocalNote(note) > -1) {
                        listener.onResult(true, note);
                    } else {
                        listener.onResult(false, note);
                    }
                } else {
                    listener.onResult(false, note);
                }
            }
        });
    }

    public interface SyncNotesListener {
        public void onResult(List<Note> data);
    }

    public void syncNotesFromServer(final SyncNotesListener listener){
        String timestamp = getLastSyncTime();
        Log.d("TIMESTAMP", "" + timestamp);
        String to = "anna" ;//getCurrUser().getPhoneNumber();
        //Log.d("TO", "" + to);
        remote.getAllNotes(new GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                for (Note note : notes) {
                    local.addNote(note);
                }
                listener.onResult(notes);

                Calendar cal = Calendar.getInstance();
                DateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss.SSS'Z'");
                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                setLastSyncTime(formatter.format((cal.getTime())));
            }
        }, timestamp, to);
    }

    public User getCurrUser() {
        return remote.getCurrUser();
    }
    // Contacts
    public List<Contact> getAllContacts(){
        return contacts.getAllContacts(context);
    }

    public Contact getContact(String phoneNumber){
        return contacts.getContact(phoneNumber);
    }
}
