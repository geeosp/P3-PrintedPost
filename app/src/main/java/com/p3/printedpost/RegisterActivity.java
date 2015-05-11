package com.p3.printedpost;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity //extends ActionBarActivity
        extends Activity {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    public static final int REGISTER = 0;
    EditText et_name;
    //EditText et_username;
    AutoCompleteTextView et_email;
    EditText et_password;
    EditText et_passwordConfirm;

    Fachada fachada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_name = (EditText) findViewById(R.id.et_name);
      //  et_username = (EditText) findViewById(R.id.et_username);
        et_email = (AutoCompleteTextView) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_passwordConfirm = (EditText) findViewById(R.id.et_password_check);
        fachada = PrintedPost.fachada;


        Account[] accounts = AccountManager.get(this).getAccounts();
        Set<String> emailSet = new HashSet<String>();
        for (Account account : accounts) {
            if (EMAIL_PATTERN.matcher(account.name).matches()) {
                emailSet.add(account.name);
            }
        }
        et_email.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet)));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void registerClick(View v) {
        if (et_name.getText().toString().length() < 3) {
           GUI.alert(this,getResources().getString(R.string.name_too_short));
        //} else if (et_username.getText().toString().contains(" ")) {
//            GUI.alert(this, getResources().getString(R.string.invalid_username));
        } else if (!validateEmail(et_email.getText().toString()))//verifica  email
            GUI.alert(this, getResources().getString(R.string.invalid_email));
        else if (!passwordVallid(et_password.getText().toString()))
            GUI.alert(this, getResources().getString(R.string.short_password));
        else if (!et_password.getText().toString().equals(et_passwordConfirm.getText().toString()))//verifica as senhas
            GUI.alert(this,getResources().getString(R.string.passwordDontMatch));
        else {
            String name = et_name.getText().toString();
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();
          //  String username = et_username.getText().toString();
            register(email,  password, name);
        }


    }

    public boolean passwordVallid(String password) {
        return password.length() >= 6;
    }

    public boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void register(String email, String password, String name) {
        ParseUser user = new ParseUser();
        user.put("name", name);
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
    Log.e("Registering" ,email+" "+password);
        Log.e("Parse", "Connecting");

        final Resources res = getResources();
        final String emailAux = email;
        final ProgressDialog progressDialog = ProgressDialog.show(this, res.getString(R.string.registering_title), res.getString(R.string.registering_message), true);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    // Hooray! Let them use the app now.


                    Log.e("PARSE", "registered");
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {

                    Log.e("PARSE", e.getMessage());
                    Log.e("Register", "Nao foi possivel registrar");
                    int code = e.getCode();
                    Log.e("Parse", "" + code);
                    switch (code) {
                        case ParseException.INVALID_EMAIL_ADDRESS:
                            Log.e("Register", "Email invalido: " + emailAux);
                            GUI.alert(RegisterActivity.this, res.getString(R.string.error_invalid_email));
                            break;
                        case ParseException.USERNAME_TAKEN:
                            Log.e("Register", "Conta ja existe: " + emailAux);
                            GUI.alert(RegisterActivity.this, res.getString(R.string.error_account_exists));
                            break;
                        case ParseException.INVALID_SESSION_TOKEN:
                            Log.e("Register", "invalid_session_token: ");
                            GUI.alert(RegisterActivity.this, res.getString(R.string.error_account_exists));
                            break;

                    }
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }


}
