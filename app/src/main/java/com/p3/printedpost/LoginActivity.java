package com.p3.printedpost;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.facebook.AccessToken;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.p3.printedpost.parseObjects.PrintUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class LoginActivity extends Activity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    AutoCompleteTextView et_email;
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() != null) {
            openMain();
        } else {
            setContentView(R.layout.activity_login2);
            et_email = (AutoCompleteTextView) findViewById(R.id.et_email);
            et_password = (EditText) findViewById(R.id.et_password);

            Account[] accounts = AccountManager.get(this).getAccounts();
            Set<String> emailSet = new HashSet<String>();
            for (Account account : accounts) {
                if (EMAIL_PATTERN.matcher(account.name).matches()) {
                    emailSet.add(account.name);
                }
            }
            et_email.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet)));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void enterClick(View v) {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        login(email, password);
    }
  /*
    public boolean checkUsername(String username) {
        ParseQuery<ParseUser> queryuserlist = ParseUser.getQuery();
        queryuserlist.whereEqualTo("username", username);
        try {
            //attempt to find a user with the specified credentials.
            return (queryuserlist.count() != 0) ? true : false;
        } catch (ParseException e) {
            return false;
        }
    }
*/

    public void login(String username, String password) {
        final Resources res = getResources();
        final ProgressDialog progressDialog = ProgressDialog.show(this, res.getString(R.string.logging_title), res.getString(R.string.logging_message), true);
        Log.e("Username", username);
        Log.e("Username", password);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                progressDialog.dismiss();
                if (user != null) {
                    Log.e("PARSE", "Login sucess");
                    Intent intent = new Intent(LoginActivity.this, SwipeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Log.e("LOGIN", e.getMessage());
                    Log.e("LOGIN", "" + e.getCode());
//                    Log.e("LOGIN", "" + e.getCause().getMessage());
                    switch (e.getCode()) {
                        case ParseException.VALIDATION_ERROR:
                            Resources res = getResources();
                            GUI.alert(LoginActivity.this, res.getString(R.string.error_wrong_password));
                            Log.e("Parse", "Senha errada");
                            break;
                        case ParseException.EMAIL_NOT_FOUND:
                            Log.e("Parse", "Email not found");
                            break;
                        case ParseException.OBJECT_NOT_FOUND:
                            Log.e("Parse", "Object not found");
                            break;


                    }

                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });

    }
    public void registerClick(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void resetClick(View v) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public void openMain() {

        Intent intent = new Intent(LoginActivity.this, SwipeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void loginFacebook(View v) {
        List<String> permissions = Arrays.asList("email", "public_profile", "user_friends");
        final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.logging_title), getString(R.string.logging_message), true);
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
             //       setEmailAndUserNameToServer();
                    openMain();

                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
              //      setEmailAndUserNameToServer();
                    openMain();
                }
            }
        });
    }



}
