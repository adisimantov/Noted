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
        receivedGrid.setAdapter(new GridAdapter(view.getContext(), true));
        return view;
    }
}
