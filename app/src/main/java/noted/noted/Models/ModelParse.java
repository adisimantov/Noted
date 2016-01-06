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
    public void init(Context context) {
        Parse.initialize(context);
    }
    public void getAllNotes(Model.GetNotesListener listener) {
        NoteParse.getAllNotes(listener);
    }

    public void getNote(String id, Model.GetNoteListener listener){
        NoteParse.getNote(id, listener);
    }

    public void addNote(Note note, Model.AddNoteListener listener){
        NoteParse.addNote(note, listener);
    }

    public void updateNote(Note note, Model.UpdateNoteListener listener){
        NoteParse.updateNote(note, listener);
    }
}
