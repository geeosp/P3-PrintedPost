package com.p3.printedpost.parseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Geovane on 11/07/2015.
 */
@ParseClassName("Article")
public class Article extends ParseObject {
    public Article() {
    }

      public Article(String title, String excerpt) {
        setTitle(title);
        setExcerpt(excerpt);
    }

    public String getTitle() {
        return getString("title");
    }

    public String getExcerpt() {
        return getString("excerpt");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public void setExcerpt(String excerpt) {
        put("excerpt", excerpt);
    }

    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    //another commit
    @Override
    public String getObjectId() {
        return super.getObjectId();
    }
}
