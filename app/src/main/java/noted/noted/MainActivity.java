package noted.noted;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.AvoidXfermode;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsAuthConfig;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.digits.sdk.android.DigitsUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;
import java.util.Calendar;
import java.util.List;

import noted.noted.Models.Contact;
import noted.noted.Models.Model;
import noted.noted.Models.Note;
import noted.noted.Models.User;

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
        //Digits.getInstance().getSessionManager().clearActiveSession();

/*
        Model.getInstance().logOut(new Model.LogOutListener() {
            @Override
            public void onResult(boolean result) {
                Log.d("Log out","" + result);
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
                            Model.getInstance().getCurrentGMTDate(),true);

                    Model.getInstance().signOrLogin(newUser, new Model.SimpleSuccessListener() {
                        @Override
                        public void onResult(boolean result) {
                            Log.d("signOrLogin", " " + result);

                            if (result) {
                                finish();
                                startActivity(getIntent());
                            } else {
                                // TODO : show failure message
                            }
                        }
                    });
                }

                @Override
                public void failure(DigitsException exception) {
                    Log.d("Digits", "Sign in with Digits failure", exception);
                }
            });
        } else {
            // Log in with current digit user
            Log.d("DIGITS",Digits.getInstance().getSessionManager().getActiveSession().getAuthToken().toString());
            // Log in with current digit user
            if (Model.getInstance().getCurrUser() == null) {
                String phone = Digits.getInstance().getSessionManager().getActiveSession().getPhoneNumber();
                String auth = Digits.getInstance().getSessionManager().getActiveSession().getAuthToken().toString();
                User currUser = new User(phone, auth, "", true);
                Model.getInstance().logIn(currUser, new Model.SimpleSuccessListener() {
                    @Override
                    public void onResult(boolean result) {
                        Log.d("LOG IN", " " + result);
                        Model.getInstance().syncNotesFromServer(new Model.SyncNotesListener() {
                            @Override
                            public void onResult(List<Note> data) {
                                Log.d(" number ", "" + data.size());
                            }
                        });
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
/*
        Note test = new Note("anna","anna","bla", "05/01/16");
        final String[] delete_id = new String[1];

        Model.getInstance().addLocalAndRemoteNote(test, new Model.AddNoteListener() {
            @Override
            public void onResult(boolean result, Note id) {
                delete_id[0] = id.getId();
                Log.d("a", "" + result + " " + id.getId());
            }
        });
        Model.getInstance().getAllRemoteNotes(new Model.GetNotesListener() {
            @Override
            public void onResult(List<Note> notes) {
                delete_id[0] = notes.get(0).getId();
                for (Note note : notes) {
                    Log.d("a", note.getId());
                }
            }
        });*/

/*        List<Note> before = Model.getInstance().getAllLocalNotes();
        Log.d("before", "" + before.size());
        for (Note note : before) {
            Log.d("a", note.getId());
        }
        delete_id[0] = before.get(0).getId();
        Log.d("get", "" + (Model.getInstance().getLocalNote(delete_id[0]) == null));
        Log.d("delete", "" + Model.getInstance().deleteLocalNote(delete_id[0]));
        List<Note> after = Model.getInstance().getAllLocalNotes();
        Log.d("after", "" + after.size());
        for (Note note : after) {
            Log.d("a", note.getId());
        }*/
        //List<Contact> contactList = Model.getInstance().getAllContacts();
        //Contact contact = Model.getInstance().getContact("000-1255");
    }
}