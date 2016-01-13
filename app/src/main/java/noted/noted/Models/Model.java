package noted.noted.Models;

import android.content.Context;

import java.util.List;

/**
 * Created by Anna on 30-Dec-15.
 */
public class Model {
    private final static Model instance = new Model();
    Context context;

    ModelSql local = new ModelSql();
    ModelParse remote = new ModelParse();
    ModelContact contacts = new ModelContact();

    private Model(){}

    public static Model getInstance(){
        return instance;
    }

    public void init(Context context){
        this.context = context;
        remote.init(context);
        local.init(context);
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

    public interface ResetPasswordListener {
        public void onResult(boolean result);
    }

    public void resetPassword(String email, ResetPasswordListener listener) {
        remote.userResetPassword(email, listener);
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

    public void syncNotesFromServer(final SyncNotesListener listener, String timestamp, String to) {
        remote.getAllNotes(new GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                for (Note note : notes) {
                    local.addNote(note);
                }
                listener.onResult(notes);
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
}
