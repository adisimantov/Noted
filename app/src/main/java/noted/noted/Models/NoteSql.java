package noted.noted.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anna on 30-Dec-15.
 */
public class NoteSql {
    private final static String NOTE_TABLE            = "NOTES";
    private final static String NOTE_ID               = "ID";
    private final static String NOTE_FROM             = "SENDER";
    private final static String NOTE_TO               = "RECEIVER";
    private final static String NOTE_DETAILS          = "DETAILS";
    private final static String NOTE_SENT_TIME        = "SENT_TIME";
    private final static String NOTE_RECEIVED_TIME    = "RECEIVED_TIME";
    private final static String NOTE_SHOWED_TIME      = "SHOWED_TIME";
    private final static String NOTE_TIME_TO_SHOW     = "TIME_TO_SHOW";
    private final static String NOTE_LOCATION_TO_SHOW = "LOCATION_TO_SHOW";
    private final static String NOTE_IS_SHOWN         = "IS_SHOWN";

    public static long addNote(ModelSql.Helper dbHelper, Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_FROM, note.getFrom());
        values.put(NOTE_TO, note.getTo());
        values.put(NOTE_DETAILS, note.getDetails());
        values.put(NOTE_SENT_TIME, note.getSentTime());
        values.put(NOTE_RECEIVED_TIME, note.getReceivedTime());
        values.put(NOTE_SHOWED_TIME, note.getShowedTime());
        values.put(NOTE_TIME_TO_SHOW, note.getTimeToShow());
        values.put(NOTE_LOCATION_TO_SHOW, note.getLocationToShow());
        values.put(NOTE_IS_SHOWN, note.isShown());

        long note_id = db.insert(NOTE_TABLE, NOTE_ID, values);
        return note_id;
    }

    public static int updateNote(ModelSql.Helper dbHelper, Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_FROM, note.getFrom());
        values.put(NOTE_TO, note.getTo());
        values.put(NOTE_DETAILS, note.getDetails());
        values.put(NOTE_SENT_TIME, note.getSentTime());
        values.put(NOTE_RECEIVED_TIME, note.getReceivedTime());
        values.put(NOTE_SHOWED_TIME, note.getShowedTime());
        values.put(NOTE_TIME_TO_SHOW, note.getTimeToShow());
        values.put(NOTE_LOCATION_TO_SHOW, note.getLocationToShow());
        values.put(NOTE_IS_SHOWN, note.isShown());

        int rows_updated = db.update(NOTE_TABLE, values, NOTE_ID + "=" + note.getId(), null);
        return rows_updated;
    }

    public static Note getNote(ModelSql.Helper dbHelper, long note_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(NOTE_TABLE, null, NOTE_ID + "=?", new String[]{String.valueOf(note_id)}, null, null, null);
        Note note = null;

        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(NOTE_ID);
            int from_index = cursor.getColumnIndex(NOTE_FROM);
            int to_index = cursor.getColumnIndex(NOTE_TO);
            int details_index = cursor.getColumnIndex(NOTE_DETAILS);
            int sent_time_index = cursor.getColumnIndex(NOTE_SENT_TIME);
            int received_time_index = cursor.getColumnIndex(NOTE_RECEIVED_TIME);
            int showed_time_index = cursor.getColumnIndex(NOTE_SHOWED_TIME);
            int time_to_show_index = cursor.getColumnIndex(NOTE_TIME_TO_SHOW);
            int location_to_show_index = cursor.getColumnIndex(NOTE_LOCATION_TO_SHOW);
            int is_shown_index = cursor.getColumnIndex(NOTE_IS_SHOWN);

            long id = cursor.getInt(id_index);
            String from = cursor.getString(from_index);
            String to = cursor.getString(to_index);
            String details = cursor.getString(details_index);
            String sentTime = cursor.getString(sent_time_index);
            String receivedTime = cursor.getString(received_time_index);
            String showedTime = cursor.getString(showed_time_index);
            String timeToShow = cursor.getString(time_to_show_index);
            String locationToShow = cursor.getString(location_to_show_index);
            boolean isShown = (cursor.getInt(is_shown_index) == 1);

            note = new Note(id,from,to,details,sentTime,receivedTime,showedTime,timeToShow,locationToShow,isShown);
        }

        return note;
    }

    public static List<Note> getAllNotes(ModelSql.Helper dbHelper) {
        List<Note> data = new LinkedList<Note>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(NOTE_TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(NOTE_ID);
            int from_index = cursor.getColumnIndex(NOTE_FROM);
            int to_index = cursor.getColumnIndex(NOTE_TO);
            int details_index = cursor.getColumnIndex(NOTE_DETAILS);
            int sent_time_index = cursor.getColumnIndex(NOTE_SENT_TIME);
            int received_time_index = cursor.getColumnIndex(NOTE_RECEIVED_TIME);
            int showed_time_index = cursor.getColumnIndex(NOTE_SHOWED_TIME);
            int time_to_show_index = cursor.getColumnIndex(NOTE_TIME_TO_SHOW);
            int location_to_show_index = cursor.getColumnIndex(NOTE_LOCATION_TO_SHOW);
            int is_shown_index = cursor.getColumnIndex(NOTE_IS_SHOWN);

            do {
                long id = cursor.getInt(id_index);
                String from = cursor.getString(from_index);
                String to = cursor.getString(to_index);
                String details = cursor.getString(details_index);
                String sentTime = cursor.getString(sent_time_index);
                String receivedTime = cursor.getString(received_time_index);
                String showedTime = cursor.getString(showed_time_index);
                String timeToShow = cursor.getString(time_to_show_index);
                String locationToShow = cursor.getString(location_to_show_index);
                boolean isShown = (cursor.getInt(is_shown_index) == 1);

                Note st = new Note(id,from,to,details,sentTime,receivedTime,showedTime,timeToShow,locationToShow,isShown);
                data.add(st);
            } while (cursor.moveToNext());
        }

        return data;
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NOTE_TABLE + " (" +
                    NOTE_ID                 + " INTEGER PRIMARY KEY," +
                    NOTE_FROM               + " TEXT NOT NULL," +
                    NOTE_TO                 + " TEXT NOT NULL," +
                    NOTE_DETAILS            + " TEXT NOT NULL," +
                    NOTE_SENT_TIME          + " DATETIME," +
                    NOTE_RECEIVED_TIME      + " DATETIME," +
                    NOTE_SHOWED_TIME        + " DATETIME," +
                    NOTE_TIME_TO_SHOW       + " DATETIME," +
                    NOTE_LOCATION_TO_SHOW   + " TEXT," +
                    NOTE_IS_SHOWN           + " BOOLEAN" + ");");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
    }
}
