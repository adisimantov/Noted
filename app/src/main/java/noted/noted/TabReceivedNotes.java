package noted.noted;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    GridAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.received_notes_tab, container, false);
        receivedGrid = (GridView) view.findViewById(R.id.receivedNotesGrid);
        adapter =  new GridAdapter(view.getContext(),true);
        receivedGrid.setAdapter(adapter);

        receivedGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(R.string.delete_note_message);
                builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //receivedGrid.removeViewInLayout(view);
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

        return view;
    }
}
