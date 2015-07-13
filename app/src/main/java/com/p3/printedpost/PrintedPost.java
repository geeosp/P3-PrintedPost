package com.p3.printedpost;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.p3.printedpost.parseObjects.Article;
import com.p3.printedpost.parseObjects.Comment;
import com.p3.printedpost.parseObjects.PrintUser;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Geovane on 01/03/2015.
 */
public class PrintedPost extends Application {
    public static Fachada fachada;// = new Fachada();

    public void onCreate() {
        fachada = new Fachada();
        Parse.enableLocalDatastore(this);
        Fresco.initialize(this);
        ParseObject.registerSubclass(PrintUser.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Article.class);
        Parse.initialize(this, "hGNzYptoQo0eLE4NNjYrom3xoRvr6zeNPkhUtSbI", "SP1UR3W9A41NPH1plxDOSzXKgJVezcavMu7zeUtB");
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);

  /*      if(PrintUser.getCurrentUser()!=null){
            ParseQuery<Article> query = ParseQuery.getQuery("Article");
            query.findInBackground(new FindCallback<Article>() {
                @Override
                public void done(List<Article> list, ParseException e) {
                    ParseRelation<Article> articleParseRelation = PrintUser.getCurrentUser().getRelation("articles");
                    Iterator<Article> it= list.iterator();
                    while(it.hasNext()){
                        articleParseRelation.add(it.next());
                    }
                    PrintUser.getCurrentUser().saveInBackground();

                }
            });
        }5
*/


/*
        if (PrintUser.getCurrentUser() != null) {
            Article[] articles = new Article[20];
            ParseRelation<Article> articleParseRelation = PrintUser.getCurrentUser().getRelation("articles");
            for (int i = 0; i < 20; i++) {
                try {
                    Log.e("INIT", "creating article" + i);
                    articles[i] = new Article("Title " + i, i + " resumindo isso...");
                    Log.e("INIT", "saving article" + i);
                    articles[i].save();
                    Log.e("INIT", "adding in relationship " + i);
                    if (i % 2 == 0)
                        articleParseRelation.add(articles[i]);
                    Log.e("INIT", "added in relationship " + i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
*/

        //   ParseUser.logOut();

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
