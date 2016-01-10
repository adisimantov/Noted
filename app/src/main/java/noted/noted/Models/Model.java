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
    public Note getLocalNote(long id){
        return local.getNote(id);
    }

    public long addLocalNote(Note note){
        return local.addNote(note);
    }

    public int updateLocalNote(Note note){
        return local.updateNote(note);
    }

    public List<Note> getAllLocalNotes(){
        return local.getAllNotes();
    }


    // Remote database

    public interface GetNotesListener{
        public void onResult(List<Note> notes);
    }

    public void getAllRemoteNotes(GetNotesListener listener){
        remote.getAllNotes(listener);
    }

    public interface GetNoteListener {
        public void onResult(Note note);
    }

    public void getNote(long id,GetNoteListener listener){
        remote.getNote(id,listener);
    }

    public interface AddNoteListener {
        public void onResult(boolean result);
    }

    public void addRemoteNote(Note note, AddNoteListener listener){
        remote.addNote(note, listener);
    }

    public interface UpdateNoteListener {
        public void onResult(boolean result);
    }

    public void updateRemoteNote(Note note, UpdateNoteListener listener){
        remote.updateNote(note, listener);
    }

    // Contacts
    public List<Contact> getAllContacts(){
        return contacts.getAllContacts(context);
    }

    public Contact getContact(String phoneNumber){
        return contacts.getContact(phoneNumber);
    }
}
