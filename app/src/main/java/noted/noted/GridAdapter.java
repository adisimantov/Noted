package noted.noted;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import noted.noted.Models.Note;

public class GridAdapter extends BaseAdapter {

    List<Note> lNotes = new ArrayList<Note>();
    Context context;
    boolean isReceived;

    public GridAdapter(Context context, boolean isReceived) {
        this.context = context;
        this.isReceived = isReceived;
        lNotes.add(new Note(1, 1111, 2222, "blabla", false));
        lNotes.add(new Note(2, 1111, 2222, "blabla", false));
        lNotes.add(new Note(3, 1111, 2222, "blabla", false));
        lNotes.add(new Note(4, 1111, 2222, "blabla", false));
    }

    @Override
    public int getCount() {
        return lNotes.size();
    }

    @Override
    public Note getItem(int position) {
        return lNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lNotes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_note_item, null);
        }

        TextView noteContact = (TextView) convertView.findViewById(R.id.noteContact);
        TextView details = (TextView) convertView.findViewById(R.id.noteDetails);

        Note note = getItem(position);
        if (isReceived) {
            noteContact.setText("FROM " + note.getFrom());
        }
        else{
            noteContact.setText("TO " + note.getTo());
        }
        details.setText(note.getDetails() + "");

        return convertView;
    }
}