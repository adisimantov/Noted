package noted.noted;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by adi on 26-Dec-15.
 */
public class TabSentNotes extends Fragment{
    GridView sentGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sent_notes_tab, container, false);
        sentGrid = (GridView) view.findViewById(R.id.sentNotesGrid);
        sentGrid.setAdapter(new GridAdapter(view.getContext(), false));
        return view;
    }
}
