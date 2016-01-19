package noted.noted;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import noted.noted.Models.Model;
import noted.noted.Models.Note;

/**
 * Created by adi on 26-Dec-15.
 */
public class TabReceivedNotes extends Fragment {

    GridView receivedGrid;
    GridAdapter adapter;
    View viewTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewTab = inflater.inflate(R.layout.received_notes_tab, container, false);

        receivedGrid = (GridView) viewTab.findViewById(R.id.receivedNotesGrid);
        adapter =  new GridAdapter(viewTab.getContext(),true);
        receivedGrid.setAdapter(adapter);

        receivedGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(R.string.delete_note_message);
                builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note note = adapter.getItem(position);
                        Model.getInstance().deleteLocalNote(note.getId());
                        adapter.lNotes.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });

        receivedGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ViewNoteActivity.class);
                Note note = adapter.getItem(position);
                intent.putExtra("note_id", note.getId());
                startActivity(intent);
            }
        });

        return viewTab;
    }
}
