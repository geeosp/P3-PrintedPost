package com.p3.printedpost;

import android.util.Log;

import com.p3.printedpost.parseObjects.Article;
import com.p3.printedpost.parseObjects.Comment;
import com.p3.printedpost.parseObjects.PrintUser;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by Geovane on 28/03/2015.
 */
public class Fachada implements FachadaInterface {
    @Override
    public boolean isLogged() {
        ParseUser user = ParseUser.getCurrentUser();
        return user != null;
    }

    @Override
    public ParseUser getUser() {
        return ParseUser.getCurrentUser();

    }

    public enum OrderCommentsBy {
        NEWERFIRST, OLDERFIRST, LIKESFIRST
    }

    public void resetPassword(String email) {
        ParseUser.requestPasswordResetInBackground(email,
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.e("Parse", "Reset password sent");
                        } else {
                            Log.e("Parse", "Reset password not sent");
                        }
                    }
                });
    }

    public Article getArticle(String articleid) {
        Article article = null;
        ParseQuery<Article> query = ParseQuery.getQuery("Article");
        query.fromLocalDatastore();
        try {
            article = (Article) query.get(articleid);
        } catch (ParseException e) {//não tá no banco local
            e.printStackTrace();
            query = ParseQuery.getQuery("Article");
            try {
                article = (Article) query.get(articleid);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return article;
    }

    public Comment getComment(String commentid) {
        Comment comment = null;
        ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
        query.fromLocalDatastore();
        try {
            comment = (Comment) query.get(commentid);
        } catch (ParseException e) {
            e.printStackTrace();
            query=ParseQuery.getQuery("Comment");
            try{
                comment =(Comment) query.get(commentid);
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
        return comment;
    }

    public Vector<Article> getArticles() {
        final Vector<Article> lu = new Vector<Article>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ParseRelation<Article> articlesRel = PrintUser.getCurrentUser().getRelation("articles");
                    ParseQuery<Article> query = articlesRel.getQuery();
                    query.fromLocalDatastore();
                    query.orderByDescending("createdAt");
                    List<Article> lq = query.find();
                    Iterator<Article> i = lq.iterator();
                    while (i.hasNext()) {
                        lu.add(i.next());
                    }
                } catch (ParseException e) {
                    Log.e("ParseException", e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("InterruptedException", e.getMessage());
        }
        Log.e("TamanhoDaBase", "" + lu.size());
        return lu;
    }

    public void updateArticles() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("FACHADA", "updating articles...");
                ParseRelation<Article> articles = PrintUser.getCurrentUser().getRelation("articles");
                ParseQuery<Article> query = articles.getQuery();
                try {
                    Log.e("Entrou", "Aqui");
                    List<Article> lq = query.find();
                    Iterator<Article> i = lq.iterator();
                    while (i.hasNext()) {
                        i.next().pin();
                    }
                    PrintUser.getCurrentUser().pin();
                    Log.e("FACHADA", "articles updated, size: " + lq.size());
                } catch (ParseException e) {
                    Log.e("ParseException", e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("InterruptedException", e.getMessage());
        }
    }

    public Vector<Comment> getComments(final Article article, final OrderCommentsBy choose) {
        final Vector<Comment> lu = new Vector<Comment>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
                try {
                    query.fromLocalDatastore();
                    query.whereEqualTo("article", article);
                    query.whereEqualTo("level", 0);
                    switch (choose) {
                        case OLDERFIRST:
                            query.orderByAscending("createdAt");
//                            query.orderByDescending("up");
                            break;
                        case NEWERFIRST:
                            query.orderByAscending("down");
                            query.orderByDescending("createdAt,up");
                            break;
                        case LIKESFIRST:
                            query.orderByAscending("down");
                            query.orderByDescending("up,createdAt");

                    }
                    List<Comment> lq = query.find();
                    Iterator<Comment> i = lq.iterator();
                    while (i.hasNext()) {
                        lu.add(i.next());
                    }
                } catch (ParseException e) {
                    Log.e("ParseException", e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("InterruptedException", e.getMessage());
        }
        Log.e("TamanhoDaBase", "" + lu.size());
        return lu;
    }

    public void updateComments(final Article article) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("FACHADA", "updating comments...");
                ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
                query.whereEqualTo("article", article);
                query.whereEqualTo("level", 0);
                try {
                    Log.e("Entrou", "Aqui");
                    List<Comment> lq = query.find();
                    Iterator<Comment> i = lq.iterator();
                    while (i.hasNext()) {
                        i.next().pin();
                    }
                    PrintUser.getCurrentUser().pin();
                    Log.e("FACHADA", "comments updated, size: " + lq.size());
                } catch (ParseException e) {
                    Log.e("ParseException", e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("InterruptedException", e.getMessage());
        }
    }

    public Vector<Comment> getReplies(final Comment comment, final OrderCommentsBy choose) {
        final Vector<Comment> lu = new Vector<Comment>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ParseRelation<Comment> replies = comment.getRelation("replies");
                ParseQuery<Comment> query = replies.getQuery();

                try {
                    query.fromLocalDatastore();
                    switch (choose) {
                        case OLDERFIRST:
                            query.orderByAscending("createdAt");
//                            query.orderByDescending("up");
                            break;
                        case NEWERFIRST:
                            query.orderByAscending("down");
                            query.orderByDescending("createdAt,up");
                            break;
                        case LIKESFIRST:
                            query.orderByAscending("down");
                            query.orderByDescending("up,createdAt");

                    }
                    List<Comment> lq = query.find();
                    Iterator<Comment> i = lq.iterator();
                    while (i.hasNext()) {
                        lu.add(i.next());
                    }
                } catch (ParseException e) {
                    Log.e("ParseException", e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("InterruptedException", e.getMessage());
        }
        Log.e("TamanhoDaBase", "" + lu.size());
        return lu;
    }

    public void updateReplies(final Comment comment) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("FACHADA", "updating comments...");
                ParseRelation<Comment> replies = comment.getRelation("replies");
                ParseQuery<Comment> query = replies.getQuery();
                try {
                    Log.e("Entrou", "Aqui");
                    List<Comment> lq = query.find();
                    Iterator<Comment> i = lq.iterator();
                    while (i.hasNext()) {
                        i.next().pin();
                    }
                    PrintUser.getCurrentUser().pin();
                    Log.e("FACHADA", "comments updated, size: " + lq.size());
                } catch (ParseException e) {
                    Log.e("ParseException", e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("InterruptedException", e.getMessage());
        }
    }


}
