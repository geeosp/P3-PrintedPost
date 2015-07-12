package com.p3.printedpost.parseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Geovane on 11/07/2015.
 */
@ParseClassName("_User")
public class PrintUser extends ParseUser{


    static public PrintUser getCurrentUser() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.fromLocalDatastore();
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        try {
            return (PrintUser) query.find().iterator().next();
        } catch (Exception e) {
            return null;
        }
    }
}
