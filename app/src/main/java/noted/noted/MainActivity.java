package noted.noted;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class MainActivity extends Activity {

    // Declaring our tabs and the corresponding fragments.
    ActionBar.Tab receivedTab, sentTab;
    Fragment tabReceivedNotes = new TabReceivedNotes();
    Fragment tabSentNotes = new TabSentNotes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Adding tabs to the ActionBar.
        actionBar.addTab(receivedTab);
        actionBar.addTab(sentTab);
    }
}