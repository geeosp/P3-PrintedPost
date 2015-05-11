package com.p3.printedpost;

import android.app.Application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/**
 * Created by Geovane on 01/03/2015.
 */
public class PrintedPost extends Application {
    public static Fachada fachada;// = new Fachada();

    public void onCreate() {
        fachada = new Fachada();
        Parse.initialize(this, "hGNzYptoQo0eLE4NNjYrom3xoRvr6zeNPkhUtSbI", "SP1UR3W9A41NPH1plxDOSzXKgJVezcavMu7zeUtB");
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);
        ParseUser.logOut();

        try {

            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (NameNotFoundException e) {
            Log.e("name not found", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        }

    }
}
