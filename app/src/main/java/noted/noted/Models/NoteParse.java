package noted.noted.Models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anna on 30-Dec-15.
 */
public class NoteParse {
    private static final String TAG = "NoteParse";

    private final static String NOTE_TABLE            = "NOTES";
    private final static String NOTE_ID               = "LOCAL_ID";
    private final static String NOTE_FROM             = "SENDER";
    private final static String NOTE_TO               = "RECEIVER";
    private final static String NOTE_DETAILS          = "DETAILS";
    private final static String NOTE_SENT_TIME        = "SENT_TIME";
    private final static String NOTE_RECEIVED_TIME    = "RECEIVED_TIME";
    private final static String NOTE_SHOWED_TIME      = "SHOWED_TIME";
    private final static String NOTE_TIME_TO_SHOW     = "TIME_TO_SHOW";
    private final static String NOTE_LOCATION_TO_SHOW = "LOCATION_TO_SHOW";
    private final static String NOTE_IS_SHOWN         = "IS_SHOWN";

    public static void addNote(Note note, final Model.AddNoteListener listener) {
        ParseObject parseNote = new ParseObject(NOTE_TABLE);
        parseNote.put(NOTE_ID, note.getId());
        parseNote.put(NOTE_FROM, note.getFrom());
        parseNote.put(NOTE_TO, note.getTo());
        parseNote.put(NOTE_DETAILS, note.getDetails());
        parseNote.put(NOTE_SENT_TIME, note.getSentTime());
        parseNote.put(NOTE_RECEIVED_TIME, note.getReceivedTime());
        parseNote.put(NOTE_SHOWED_TIME, note.getShowedTime());
        parseNote.put(NOTE_TIME_TO_SHOW, note.getTimeToShow());
        parseNote.put(NOTE_LOCATION_TO_SHOW, note.getLocationToShow());
        parseNote.put(NOTE_IS_SHOWN, note.isShown());

        parseNote.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    listener.onResult(true);
                } else {
                    listener.onResult(false);
                    Log.d(getClass().getSimpleName(), "Note add error: " + e);
                }
            }
        });
    }

    public static void updateNote(final Note note, final Model.UpdateNoteListener listener) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(NOTE_TABLE);

        // Retrieve the object by id
        query.getInBackground(String.valueOf(note.getId()), new GetCallback<ParseObject>() {
            public void done(ParseObject parseNote, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data.
                    parseNote.put(NOTE_FROM, note.getFrom());
                    parseNote.put(NOTE_TO, note.getTo());
                    parseNote.put(NOTE_DETAILS, note.getDetails());
                    parseNote.put(NOTE_SENT_TIME, note.getSentTime());
                    parseNote.put(NOTE_RECEIVED_TIME, note.getReceivedTime());
                    parseNote.put(NOTE_SHOWED_TIME, note.getShowedTime());
                    parseNote.put(NOTE_TIME_TO_SHOW, note.getTimeToShow());
                    parseNote.put(NOTE_LOCATION_TO_SHOW, note.getLocationToShow());
                    parseNote.put(NOTE_IS_SHOWN, note.isShown());

                    parseNote.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                listener.onResult(true);
                            } else {
                                listener.onResult(false);
                                Log.d(getClass().getSimpleName(), "Note update error: " + e);
                            }
                        }
                    });
                }
            }
        });
    }

    public static void getNote(long id, final Model.GetNoteListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(NOTE_TABLE);
        query.whereEqualTo(NOTE_ID, id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Note note = null;
                if (e == null && parseObjects.size() > 0) {
                    ParseObject po = parseObjects.get(0);
                    long id = po.getInt(NOTE_ID);
                    String from = po.getString(NOTE_FROM);
                    String to = po.getString(NOTE_TO);
                    String details = po.getString(NOTE_DETAILS);
                    String sentTime = po.getString(NOTE_SENT_TIME);
                    String receivedTime = po.getString(NOTE_RECEIVED_TIME);
                    String showedTime = po.getString(NOTE_SHOWED_TIME);
                    String timeToShow = po.getString(NOTE_TIME_TO_SHOW);
                    String locationToShow = po.getString(NOTE_LOCATION_TO_SHOW);
                    boolean isShown = (po.getInt(NOTE_IS_SHOWN) == 1);

                    note = new Note(id, from, to, details, sentTime, receivedTime, showedTime, timeToShow, locationToShow, isShown);
                }
                listener.onResult(note);
            }
        });
    }

    public static void getAllNotes(final Model.GetNotesListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(NOTE_TABLE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                List<Note> notes = new LinkedList<Note>();
                if (e == null) {
                    for (ParseObject po : parseObjects) {
                        long id = po.getInt(NOTE_ID);
                        String from = po.getString(NOTE_FROM);
                        String to = po.getString(NOTE_TO);
                        String details = po.getString(NOTE_DETAILS);
                        String sentTime = po.getString(NOTE_SENT_TIME);
                        String receivedTime = po.getString(NOTE_RECEIVED_TIME);
                        String showedTime = po.getString(NOTE_SHOWED_TIME);
                        String timeToShow = po.getString(NOTE_TIME_TO_SHOW);
                        String locationToShow = po.getString(NOTE_LOCATION_TO_SHOW);
                        boolean isShown = (po.getInt(NOTE_IS_SHOWN) == 1);

                        Note note = new Note(id, from, to, details, sentTime, receivedTime, showedTime, timeToShow, locationToShow, isShown);
                        notes.add(note);
                    }
                }
                listener.onResult(notes);
            }
        });
    }
}
