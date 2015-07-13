package com.p3.printedpost.parseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Geovane on 11/07/2015.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {
    private String userName = "";
    private String userPhoto = "";
    public Comment() {

    }

    public Comment(PrintUser author, Article article, String content) {
        setAuthor(author);
        setArticle(article);
        setContent(content);
        put("up", 0);
        put("down", 0);
        put("level", 0);
        put("repliescount", 0);
        put("authorname", author.getName());
        put("authorphoto", author.getPhotoUrl());
    }

    public Comment(PrintUser author, Comment comment, String content) {
        setAuthor(author);
        setArticle(comment.getArticle());
        setContent(content);
        put("up", 0);
        put("down", 0);
        put("repliescount", 0);
        put("level", comment.getLevel()+1);
        put("repliescount", 0);
        put("authorname", author.getName());
        put("authorphoto", author.getPhotoUrl());
    }
    public int getLevel(){
        return getInt("level");
    }


    public String getContent() {
        return getString("content");
    }

    public PrintUser getAuthor() {
        return (PrintUser) getParseUser("author");
    }

    public Article getArticle() {
        return (Article) getParseObject("article");

    }
    public String getUserName(){
        return getString("authorname");
    }
    public String getUserPhoto(){
        return getString("authorphoto");
    }

public int getRepliesCount(){
    return getInt("repliescount");
}

    public void setContent(String content) {
        put("content", content);
    }

    public void setAuthor(PrintUser author) {
        put("author", author);
    }

    public void setArticle(Article article) {
        put("article", article);
    }

    /*
    public void setNestedTo(Comment comment){
        put("nestedTo", comment);

    }
*/
    @Override
    public String getObjectId() {
        return super.getObjectId();
    }

    public int getUps() {
        return getInt("up");
    }

    public int getDowns() {
        return getInt("down");
    }

    public void doLike() {
        increment("up");
        increment("rating");
        saveInBackground();
    }

    public void doDislike() {
        increment("down");
        increment("rating", -1);
        saveInBackground();
    }

    public void undoLike() {
        increment("up", -1);
        increment("rating", -1);
        saveInBackground();
    }

    public void undoDislike() {
        increment("down", -1);
        increment("rating");
        saveInBackground();

    }

}
