package noted.noted;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import noted.noted.Models.Model;
import noted.noted.Models.Note;

/**
 * Created by adi on 26-Dec-15.
 */
public class TabSentNotes extends Fragment{
    static final int SENT_NOTE=1;
    private final static String ID = "ID";
    private final static String FROM = "FROM";
    private final static String TO = "TO";
    private final static String DETAILS = "DETAILS";
    private final static String SENT_TIME = "SENT_TIME";
    private final static String TIME_TO_SHOW = "TIME_TO_SHOW";
    private final static String LOCATION_TO_SHOW = "LOCATION_TO_SHOW";

    GridView sentGrid;
    ImageButton addButton;
    GridAdapter adapter;
    View viewTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewTab = inflater.inflate(R.layout.sent_notes_tab, container, false);
        sentGrid = (GridView) viewTab.findViewById(R.id.sentNotesGrid);
        adapter =  new GridAdapter(viewTab.getContext(),false);
        sentGrid.setAdapter(adapter);

        addButton = (ImageButton) viewTab.findViewById(R.id.add_new_note_fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),SendNoteActivity.class);
                startActivityForResult(intent, SENT_NOTE);
            }
        });

        sentGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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

        sentGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(),ViewNoteActivity.class);
                Note note = adapter.getItem(position);
                intent.putExtra("note_id", note.getId());
                startActivity(intent);
            }
        });

        return viewTab;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

            if (resultCode == Activity.RESULT_OK && requestCode == SENT_NOTE) {
                Bundle bundle = data.getExtras();
                Note note = new Note(bundle.getString(ID), bundle.getString(FROM), bundle.getString(TO), bundle.getString(DETAILS),
                        bundle.getString(SENT_TIME), bundle.getString(TIME_TO_SHOW), null, null);
                adapter.lNotes.add(note);
                adapter.notifyDataSetChanged();
            }

    }
}
