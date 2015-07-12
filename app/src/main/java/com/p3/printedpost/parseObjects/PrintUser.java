package com.p3.printedpost.parseObjects;

import com.parse.ParseClassName;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by Geovane on 11/07/2015.
 */
@ParseClassName("_User")
public class PrintUser extends ParseUser {


    static public PrintUser getCurrentUser() {
        return (PrintUser) ParseUser.getCurrentUser();
        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.fromLocalDatastore();
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        try {
            return (PrintUser) query.find().iterator().next();
        } catch (Exception e) {
            return null;
        }*/
    }

    public void addArticle(Article article) {
        ParseRelation<Article> articles = getCurrentUser().getRelation("articles");
        articles.add(article);

    }

    public void removeArticle(Article article) {
        ParseRelation<Article> articles = getCurrentUser().getRelation("articles");
        articles.remove(article);
    }

}
