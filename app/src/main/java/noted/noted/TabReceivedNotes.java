package noted.noted;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import noted.noted.Models.Note;

/**
 * Created by adi on 26-Dec-15.
 */
public class TabReceivedNotes extends Fragment{

    GridView receivedGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.received_notes_tab, container, false);
        receivedGrid = (GridView) view.findViewById(R.id.receivedNotesGrid);
        receivedGrid.setAdapter(new MyAdapter(view.getContext()));
        return inflater.inflate(R.layout.received_notes_tab, container, false);
    }

    private class MyAdapter extends BaseAdapter {

        List<Note> lNotes = new ArrayList<Note>();

        public MyAdapter(Context context){

            lNotes.add(new Note(1,1111,2222,"blabla",false));
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
            View v = convertView;
            TextView from;
            TextView to;
            TextView details;

            from = (TextView) v.getTag(R.id.noteFrom);
            to = (TextView) v.getTag(R.id.noteTo);
            details = (TextView) v.getTag(R.id.noteDetails);

            Note note = getItem(position);
            from.setText(note.getFrom());
            to.setText(note.getTo());
            details.setText(note.getDetails());

            return v;
        }
    }
}
