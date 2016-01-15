package noted.noted.Models;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

/**
 * Created by Anna on 10-Jan-16.
 */
public class UserParse {
    private final static String USER_JOIN_DATE = "JOIN_DATE";
    private final static String USER_IS_ACTIVE = "IS_ACTIVE";

    public static void userLogIn(User user, final Model.LogInListener listener) {
        ParseUser.logInInBackground(user.getPhoneNumber(), user.getAuthCode(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    listener.onResult(true);
                }
                else {
                    listener.onResult(false);
                }
            }
        });
    }

    public static void userSignUp(User user, final Model.SignUpListener listener) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(user.getPhoneNumber());
        newUser.setPassword(user.getAuthCode());
        newUser.put(USER_JOIN_DATE,user.getJoinDate());
        newUser.put(USER_IS_ACTIVE,user.getIsActive());
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    listener.onResult(true);
                } else {
                    listener.onResult(false);
                }
            }
        });
    }

    public static User getCurrUser() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        User user = null;
        if (parseUser != null) {
            user = new User(parseUser.getUsername(), "", parseUser.getString(USER_JOIN_DATE), parseUser.getBoolean(USER_IS_ACTIVE));
        }

        return user;
    }
}
