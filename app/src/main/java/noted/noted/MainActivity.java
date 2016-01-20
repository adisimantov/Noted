package noted.noted;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.maps.model.LatLng;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.Calendar;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import noted.noted.Models.Model;
import noted.noted.Models.Note;
import noted.noted.Models.User;
import noted.noted.Receivers.AlarmReceiver;

public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Fdmw3315pp4jkT5XzrJaGrZCf";
    private static final String TWITTER_SECRET = "4GqParZLqrr7cggWANygsAP732WEiVi1cEE3ZxV8NG4OiVjG74";


    // Declaring our tabs and the corresponding fragments.
    ActionBar.Tab receivedTab, sentTab;
    Fragment tabReceivedNotes = new TabReceivedNotes();
    Fragment tabSentNotes = new TabSentNotes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        // Init databse model with context
        Model.getInstance().init(this);
/*        Model.getInstance().getAllLocalNotesAsync(new Model.GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                for (Note note : notes) {
                    Log.d("DEL","" + note.getId());
                    Model.getInstance().deleteLocalNote(note.getId());
                }
            }
        });
       Model.getInstance().setLastSyncTime(null);*/
/*        Model.getInstance().syncNotesFromServer(new Model.GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                for (Note note : notes) {
                    Log.d("aa", note.getId() + " " + note.isShown() + " " + note.getTimeToShow());
                }
            }
        });*/

        // User is not signup to digits
        if (Digits.getInstance().getSessionManager().getActiveSession() == null) {
            setContentView(R.layout.activity_sign_in);

            final DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
            digitsButton.setCallback(new AuthCallback() {
                @Override
                public void success(DigitsSession session, String phoneNumber) {

                    User newUser = new User(phoneNumber, session.getAuthToken().toString(),
                            Model.getInstance().getCurrentTimestamp(),true);

                    Model.getInstance().signOrLogin(newUser, new Model.SimpleSuccessListener() {
                        @Override
                        public void onResult(boolean result) {
                            Log.d("signOrLogin", " " + result);

                            if (result) {
                                finish();
                                startActivity(getIntent());
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Failed to log in", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }

                @Override
                public void failure(DigitsException exception) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Failed to sign in", Toast.LENGTH_SHORT);
                    Log.d("Digits", "Sign in with Digits failure", exception);
                }
            });
        } else {
            // Start sync notes alarm
            AlarmReceiver.getInstance().init(this);

            // Log in with current digit user
            if (Model.getInstance().getCurrUser() == null) {
                String phone = Digits.getInstance().getSessionManager().getActiveSession().getPhoneNumber();
                String auth = Digits.getInstance().getSessionManager().getActiveSession().getAuthToken().toString();
                User currUser = new User(phone, auth, "", true);
                Model.getInstance().logIn(currUser, new Model.SimpleSuccessListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (!result) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Failed to log in", Toast.LENGTH_SHORT);
                        }
                    }
                });
            }

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
            receivedTab = actionBar.newTab().setText("Received Notes");
            sentTab = actionBar.newTab().setText("Sent Notes");

            // Setting tab listeners.
            receivedTab.setTabListener(new TabListener(tabReceivedNotes));
            sentTab.setTabListener(new TabListener(tabSentNotes));

            // Adding tabs to the ActionBar.
            actionBar.addTab(receivedTab);
            actionBar.addTab(sentTab);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}