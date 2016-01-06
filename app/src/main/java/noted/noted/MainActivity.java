package noted.noted;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import noted.noted.Models.Model;
import noted.noted.Models.Note;

public class MainActivity extends Activity {

    // Declaring our tabs and the corresponding fragments.
    ActionBar.Tab receivedTab, sentTab;
    Fragment tabReceivedNotes = new TabReceivedNotes();
    Fragment tabSentNotes = new TabSentNotes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init databse model with context
        Model.getInstance().init(this);

        // Asking for the default ActionBar element that our platform supports.
        ActionBar actionBar = getActionBar();

        // Screen handling while hiding ActionBar icon.
        actionBar.setDisplayShowHomeEnabled(false);

        // Screen handling while hiding Actionbar title.
        actionBar.setDisplayShowTitleEnabled(false);

        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Setting custom tab icons.
        receivedTab = actionBar.newTab().setText("Receibed Notes");
        sentTab = actionBar.newTab().setText("Sent Notes");

        // Setting tab listeners.
        receivedTab.setTabListener(new TabListener(tabReceivedNotes));
        sentTab.setTabListener(new TabListener(tabSentNotes));

        Note test = new Note("anna","anna","bla", "05/01/16");
/*        Model.getInstance().addRemoteNote(test, new Model.AddNoteListener() {
            @Override
            public void onResult(boolean result) {
                Log.d("a","DONE");
                Model.getInstance().getAllRemoteNotes(new Model.GetNotesListener() {
                    @Override
                    public void onResult(List<Note> notes) {
                        Log.d("a", notes.get(0).getId());
                        Model.getInstance().addLocalNote(notes.get(0));
                        Log.d("a", Model.getInstance().getAllLocalNotes().get(0).getId());
                    }
                });
            }
        });*/

        Model.getInstance().getAllRemoteNotes(new Model.GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                Log.d("a", notes.get(0).getId());
            }
        });

        // Adding tabs to the ActionBar.
        actionBar.addTab(receivedTab);
        actionBar.addTab(sentTab);
    }
}