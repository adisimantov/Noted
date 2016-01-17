package noted.noted.Models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anna on 30-Dec-15.
 */
public class NoteParse {
    private static final String TAG = "NoteParse";

    private final static String NOTE_TABLE            = "NOTES";
    private final static String NOTE_FROM             = "SENDER";
    private final static String NOTE_TO               = "RECEIVER";
    private final static String NOTE_DETAILS          = "DETAILS";
    private final static String NOTE_SENT_TIME        = "SENT_TIME";
    private final static String NOTE_TIME_TO_SHOW     = "TIME_TO_SHOW";
    private final static String NOTE_LOCATION_TO_SHOW = "LOCATION_TO_SHOW";
    private final static String NOTE_CREATED_AT       = "createdAt";

    private static Note createNoteFromParse(ParseObject po) {
        String id = po.getObjectId();
        String from = po.getString(NOTE_FROM);
        String to = po.getString(NOTE_TO);
        String details = po.getString(NOTE_DETAILS);
        String sentTime = po.getString(NOTE_SENT_TIME);
        String timeToShow = po.getString(NOTE_TIME_TO_SHOW);
        ParseGeoPoint locationToShow = po.getParseGeoPoint(NOTE_LOCATION_TO_SHOW);
        Location location = new Location(locationToShow.getLongitude(),locationToShow.getLatitude());

        return (new Note(id, from, to, details, sentTime, timeToShow, location));
    }

    private static ParseObject createParseFromNote(Note note) {
        ParseObject parseNote = new ParseObject(NOTE_TABLE);
        parseNote.put(NOTE_FROM, note.getFrom());
        parseNote.put(NOTE_TO, note.getTo());
        parseNote.put(NOTE_DETAILS, note.getDetails());
        parseNote.put(NOTE_SENT_TIME, (note.getSentTime() == null) ? JSONObject.NULL : note.getSentTime());
        parseNote.put(NOTE_TIME_TO_SHOW, (note.getTimeToShow() == null) ? JSONObject.NULL : note.getTimeToShow());

        ParseGeoPoint location = null;
        if (note.getLocationToShow() != null) {
            location = new ParseGeoPoint(note.getLocationToShow().getLongitudeToShow(),
                    note.getLocationToShow().getLatitudeToShow());
        }

        parseNote.put(NOTE_LOCATION_TO_SHOW, (location == null) ? JSONObject.NULL : location);

        return parseNote;
    }

    public static void addNote(final Note note, final Model.AddNoteListener listener) {

        final ParseObject parseNote = createParseFromNote(note);
        parseNote.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    note.setId(parseNote.getObjectId());
                    listener.onResult(true, note);
                } else {
                    listener.onResult(false, note);
                    Log.d(getClass().getSimpleName(), "Note add error: " + e);
                }
            }
        });
    }

    public static void updateNote(final Note note, final Model.UpdateNoteListener listener) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(NOTE_TABLE);

        // Retrieve the object by id
        query.getInBackground(note.getId(), new GetCallback<ParseObject>() {
            public void done(ParseObject parseNote, ParseException e) {
                if (e == null) {
                    parseNote = createParseFromNote(note);

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

    public static void getNote(String id, final Model.GetNoteListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(NOTE_TABLE);
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject po, ParseException e) {
                Note note = null;
                if (e == null) {
                    note = createNoteFromParse(po);
                }
                listener.onResult(note);
            }
        });
    }

    public static void getAllNotesTo(final Model.GetNotesListener listener,String timestamp,String to) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(NOTE_TABLE);
        query.whereEqualTo(NOTE_TO, to);

        // If timestamp is null get all user notes
        if (timestamp != null) {
            query.whereGreaterThanOrEqualTo(NOTE_CREATED_AT, timestamp);
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                List<Note> notes = new LinkedList<Note>();
                if (e == null) {
                    for (ParseObject po : parseObjects) {
                        Note note = createNoteFromParse(po);
                        notes.add(note);
                    }
                }
                listener.onResult(notes);
            }
        });
    }
}
