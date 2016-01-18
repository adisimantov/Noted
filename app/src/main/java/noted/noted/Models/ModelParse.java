package noted.noted.Models;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Anna on 30-Dec-15.
 */
public class ModelParse {
    public final static String DEFAULT_DATE_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss.SSS'Z'";
    public final static String DEFAULT_TIME_ZONE = "GMT";

    public void init(Context context) {
        Parse.initialize(context);
    }

    // Notes
    public void getAllNotes(Model.GetNotesListener listener, String timestamp, String to) {
        NoteParse.getAllNotesTo(listener, timestamp, to);
    }
    public void getNote(String id, Model.GetNoteListener listener){
        NoteParse.getNote(id, listener);
    }

    public void addNote(Note note, Model.AddNoteListener listener){
        NoteParse.addNote(note, listener);
    }

    public void updateNote(Note note, Model.SimpleSuccessListener listener){
        NoteParse.updateNote(note, listener);
    }

    // User
    public void userLogIn(User user, Model.SimpleSuccessListener listener) {
        UserParse.userLogIn(user, listener);
    }

    public void userSignUp(User user, Model.SimpleSuccessListener listener) {
        UserParse.userSignUp(user, listener);
    }

    public void userLogOut(Model.SimpleSuccessListener listener) {

        UserParse.userLogOut(listener);
    }

    public User getCurrUser() {
        return UserParse.getCurrUser();
    }
}
