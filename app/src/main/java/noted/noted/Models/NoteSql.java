package noted.noted.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.*;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anna on 30-Dec-15.
 */
public class NoteSql {
    private final static String NOTE_TABLE = "NOTES";
    private final static String NOTE_ID = "ID";
    private final static String NOTE_FROM = "SENDER";
    private final static String NOTE_TO = "RECEIVER";
    private final static String NOTE_DETAILS = "DETAILS";
    private final static String NOTE_SENT_TIME = "SENT_TIME";
    private final static String NOTE_TIME_TO_SHOW = "TIME_TO_SHOW";
    private final static String NOTE_LNG_TO_SHOW = "LNG_TO_SHOW";
    private final static String NOTE_LAT_TO_SHOW = "LAT_TO_SHOW";
    private final static String NOTE_LOCATION_TO_SHOW = "LOCATION_TO_SHOW";
    private final static String NOTE_IS_SHOWN = "IS_SHOWN";

    private static List<Note> getNoteListFromCursor(Cursor cursor) {
        List<Note> data = new LinkedList<Note>();

        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(NOTE_ID);
            int from_index = cursor.getColumnIndex(NOTE_FROM);
            int to_index = cursor.getColumnIndex(NOTE_TO);
            int details_index = cursor.getColumnIndex(NOTE_DETAILS);
            int sent_time_index = cursor.getColumnIndex(NOTE_SENT_TIME);
            int is_shown_index = cursor.getColumnIndex(NOTE_IS_SHOWN);
            int time_to_show_index = cursor.getColumnIndex(NOTE_TIME_TO_SHOW);
            int lng_to_show_index = cursor.getColumnIndex(NOTE_LNG_TO_SHOW);
            int lat_to_show_index = cursor.getColumnIndex(NOTE_LAT_TO_SHOW);
            int location_to_show_index = cursor.getColumnIndex(NOTE_LOCATION_TO_SHOW);

            do {
                String id = cursor.getString(id_index);
                String from = cursor.getString(from_index);
                String to = cursor.getString(to_index);
                String details = cursor.getString(details_index);
                String sentTime = cursor.getString(sent_time_index);
                boolean isShown = (cursor.getInt(is_shown_index) == 1);
                String timeToShow = cursor.getString(time_to_show_index);
                double lngToShow = cursor.getDouble(lng_to_show_index);
                double latToShow = cursor.getDouble(lat_to_show_index);
                String locToShow = cursor.getString(location_to_show_index);

                Note st = new Note(id, from, to, details, sentTime, timeToShow, new Location(lngToShow, latToShow), locToShow, isShown);
                data.add(st);
            } while (cursor.moveToNext());
        }

        return data;
    }

    public static long addNote(ModelSql.Helper dbHelper, Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_ID, note.getId());
        values.put(NOTE_FROM, note.getFrom());
        values.put(NOTE_TO, note.getTo());
        values.put(NOTE_DETAILS, note.getDetails());
        values.put(NOTE_SENT_TIME, note.getSentTime());
        values.put(NOTE_IS_SHOWN, note.isShown());
        values.put(NOTE_TIME_TO_SHOW, note.getTimeToShow());
        values.put(NOTE_LNG_TO_SHOW, (note.getLocationToShow() == null) ? null : note.getLocationToShow().getLongitudeToShow());
        values.put(NOTE_LAT_TO_SHOW, (note.getLocationToShow() == null) ? null : note.getLocationToShow().getLatitudeToShow());
        values.put(NOTE_LOCATION_TO_SHOW, note.getLocationToShowName());

        long note_id = db.insert(NOTE_TABLE, NOTE_ID, values);
        return note_id;
    }

    public static int deleteNote(ModelSql.Helper dbHelper, String noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows_deleted = db.delete(NOTE_TABLE, NOTE_ID + "= ? ", new String[]{noteId});
        return rows_deleted;
    }

    public static int updateNote(ModelSql.Helper dbHelper, Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOTE_FROM, note.getFrom());
        values.put(NOTE_TO, note.getTo());
        values.put(NOTE_DETAILS, note.getDetails());
        values.put(NOTE_SENT_TIME, note.getSentTime());
        values.put(NOTE_IS_SHOWN, note.isShown());
        values.put(NOTE_TIME_TO_SHOW, note.getTimeToShow());
        values.put(NOTE_LNG_TO_SHOW, (note.getLocationToShow() == null) ? null : note.getLocationToShow().getLongitudeToShow());
        values.put(NOTE_LAT_TO_SHOW, (note.getLocationToShow() == null) ? null : note.getLocationToShow().getLatitudeToShow());
        values.put(NOTE_LOCATION_TO_SHOW, note.getLocationToShowName());

        int rows_updated = db.update(NOTE_TABLE, values, NOTE_ID + "= ?", new String[]{note.getId()});
        return rows_updated;
    }


    public static List<Note> getReceivedNotes(ModelSql.Helper dbHelper, String receivedPhone, boolean shown) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = NOTE_TO + "=? AND " + NOTE_IS_SHOWN + " = ?";
        Cursor cursor = db.query(NOTE_TABLE, null, query, new String[]{receivedPhone, (shown ? "1" : "0")}, null, null, null);
        return getNoteListFromCursor(cursor);
    }

    public static List<Note> getSentNotes(ModelSql.Helper dbHelper, String sentPhone) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(NOTE_TABLE, null, NOTE_FROM + "=?", new String[]{sentPhone}, null, null, null);
        return getNoteListFromCursor(cursor);
    }

    public static Note getNote(ModelSql.Helper dbHelper, String note_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(NOTE_TABLE, null, NOTE_ID + "=?", new String[]{note_id}, null, null, null);
        Note note = null;

        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(NOTE_ID);
            int from_index = cursor.getColumnIndex(NOTE_FROM);
            int to_index = cursor.getColumnIndex(NOTE_TO);
            int details_index = cursor.getColumnIndex(NOTE_DETAILS);
            int sent_time_index = cursor.getColumnIndex(NOTE_SENT_TIME);
            int is_shown_index = cursor.getColumnIndex(NOTE_IS_SHOWN);
            int time_to_show_index = cursor.getColumnIndex(NOTE_TIME_TO_SHOW);
            int lng_to_show_index = cursor.getColumnIndex(NOTE_LNG_TO_SHOW);
            int lat_to_show_index = cursor.getColumnIndex(NOTE_LAT_TO_SHOW);
            int location_to_show_index = cursor.getColumnIndex(NOTE_LOCATION_TO_SHOW);

            String id = cursor.getString(id_index);
            String from = cursor.getString(from_index);
            String to = cursor.getString(to_index);
            String details = cursor.getString(details_index);
            String sentTime = cursor.getString(sent_time_index);
            boolean isShown = (cursor.getInt(is_shown_index) == 1);
            String timeToShow = cursor.getString(time_to_show_index);
            double lngToShow = cursor.getDouble(lng_to_show_index);
            double latToShow = cursor.getDouble(lat_to_show_index);
            String locToShow = cursor.getString(location_to_show_index);

            note = new Note(id, from, to, details, sentTime, timeToShow, new Location(lngToShow, latToShow), locToShow, isShown);
        }

        return note;
    }

    public static List<Note> getAllNotes(ModelSql.Helper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(NOTE_TABLE, null, null, null, null, null, null);
        return getNoteListFromCursor(cursor);
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NOTE_TABLE + " (" +
                NOTE_ID + " TEXT PRIMARY KEY," +
                NOTE_FROM + " TEXT NOT NULL," +
                NOTE_TO + " TEXT NOT NULL," +
                NOTE_DETAILS + " TEXT NOT NULL," +
                NOTE_SENT_TIME + " DATETIME NOT NULL," +
                NOTE_IS_SHOWN + " BOOLEAN," +
                NOTE_TIME_TO_SHOW + " DATETIME," +
                NOTE_LNG_TO_SHOW + " REAL," +
                NOTE_LAT_TO_SHOW + " REAL," +
                NOTE_LOCATION_TO_SHOW + " TEXT" + ");");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
    }
}
