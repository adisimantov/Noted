package noted.noted;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import noted.noted.Models.Note;

/**
 * Created by adi on 26-Dec-15.
 */
public class TabReceivedNotes extends Fragment {

    GridView receivedGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.received_notes_tab, container, false);
        receivedGrid = (GridView) view.findViewById(R.id.receivedNotesGrid);
        receivedGrid.setAdapter(new GridAdapter(view.getContext()));
        return view;
    }

    private class GridAdapter extends BaseAdapter {

        List<Note> lNotes = new ArrayList<Note>();
        Context context;

        public GridAdapter(Context context) {
            this.context = context;
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

            TextView from = (TextView) convertView.findViewById(R.id.noteFrom);
            TextView to = (TextView) convertView.findViewById(R.id.noteTo);
            TextView details = (TextView) convertView.findViewById(R.id.noteDetails);

            Note note = getItem(position);
            from.setText(note.getFrom()+"");
            to.setText(note.getTo()+"");
            details.setText(note.getDetails()+"");

            return convertView;
        }
    }
}
