package com.p3.printedpost.parseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Geovane on 11/07/2015.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {
public Comment(){

}
    public Comment(PrintUser author, Article article, String content){
        setAuthor(author);
        setArticle(article);
        setContent(content);
    }
    public Comment(PrintUser author, Article article, String content, Comment nestedTo){
        setAuthor(author);
        setArticle(article);
        setContent(content);
        //setNestedTo(nestedTo);
    }

    public String getContent() {
        return getString("content");
    }
    public PrintUser getAuthor(){
        return (PrintUser) getParseUser("author");
    }
    public Article getArticle(){
        return (Article) getParseObject("article");

    }

    public Comment getNestedTo(){
        return (Comment) getParseObject("nestedTo");
    }

    public void setContent(String content){
        put("content", content);
    }
    public void setAuthor(PrintUser author){
        put("author", author);
    }
    public void setArticle(Article article){
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

    }
    public void doDislike() {
        increment("down");

    }
    public void undoLike() {
        increment("up", -1);

 }

    public void undoDislike() {
        increment("down", -1);

    }




}
