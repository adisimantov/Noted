package noted.noted.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Anna on 30-Dec-15.
 */
public class ModelSql {
    final static int VERSION = 2;
    private Helper dbHelper;

    public void init(Context context) {
        if (dbHelper == null){
            dbHelper = new Helper(context);
        }
    }

    public List<Note> getAllNotes(){
        return NoteSql.getAllNotes(dbHelper);
    }

    public Note getNote(String id){
        return NoteSql.getNote(dbHelper, id);
    }

    public long addNote(Note note){
        return NoteSql.addNote(dbHelper, note);
    }

    public int updateNote(Note note){
        return NoteSql.updateNote(dbHelper,note);
    }

    class Helper extends SQLiteOpenHelper {
        public Helper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            NoteSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            NoteSql.drop(db);
            onCreate(db);
        }
    }
}
