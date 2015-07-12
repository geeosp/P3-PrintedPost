package com.p3.printedpost.parseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Geovane on 11/07/2015.
 */
@ParseClassName("Article")
public class Article extends ParseObject {
public Article(){}
    public String getTitle() {
        return getString("title");
    }

    public String getExcerpt() {
        return getString("excerpt");
    }

    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    @Override
    public String getObjectId() {
        return super.getObjectId();
    }
}
