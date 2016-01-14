package noted.noted.Models;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

/**
 * Created by Anna on 10-Jan-16.
 */
public class UserParse {
    public static void userLogIn(User user, final Model.LogInListener listener) {
        ParseUser.logInInBackground(user.getUsername(), user.getPassword(), new LogInCallback() {
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
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
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
        User user = new User(parseUser.getUsername(),"",parseUser.getEmail());
        return user;
    }

    public static void userResetPassword(String email, final Model.ResetPasswordListener listener) {
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // An email was successfully sent with reset instructions.
                    listener.onResult(true);
                } else {
                    listener.onResult(false);
                }
            }
        });
    }
}
