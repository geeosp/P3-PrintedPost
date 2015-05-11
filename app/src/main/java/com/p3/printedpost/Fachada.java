package com.p3.printedpost;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/**
 * Created by Geovane on 28/03/2015.
 */
public class Fachada implements FachadaInterface {
    @Override
    public boolean isLogged() {
        ParseUser user = ParseUser.getCurrentUser();
        return user != null;
    }

    @Override
    public ParseUser getUser() {
        return ParseUser.getCurrentUser();

    }


    public void resetPassword(String email) {
        ParseUser.requestPasswordResetInBackground(email,
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.e("Parse", "Reset password sent");
                        } else {
                            Log.e("Parse", "Reset password not sent");
                        }
                    }
                });
    }
}
