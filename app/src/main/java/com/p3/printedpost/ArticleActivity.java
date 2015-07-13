package com.p3.printedpost;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.p3.printedpost.parseObjects.Article;
import com.p3.printedpost.parseObjects.Comment;
import com.p3.printedpost.parseObjects.PrintUser;

import java.util.Vector;

public class ArticleActivity extends AppCompatActivity {
    Article article;
    private RecyclerView mRecyclerView;
    private CommentsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText et_comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Intent intent = getIntent();
        String articleId = intent.getStringExtra("articleId");
        article = PrintedPost.fachada.getArticle(articleId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(article.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);
        RelativeTimeTextView tv_date = (RelativeTimeTextView) findViewById(R.id.tv_article_date);
        tv_date.setReferenceTime(article.getCreatedAt().getTime());
        TextView tv_article_excerpt = (TextView) findViewById(R.id.tv_article_excerpt);
        tv_article_excerpt.setText(article.getExcerpt());
        et_comment = (EditText) findViewById(R.id.et_comment);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recents);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_recents);
        // specify an adapter (see also next example)
        Fachada.OrderCommentsBy orderChoose = Fachada.OrderCommentsBy.OLDERFIRST;
        ;
        mAdapter = new CommentsAdapter(this, swipeRefreshLayout, article, orderChoose);
        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        refresh();

    }

    public void refresh() {
        mAdapter.refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
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
        } else if (id == R.id.order_by) {
            return true;
        } else if (id == R.id.by_coolest) {
            mAdapter.setOrderBy(Fachada.OrderCommentsBy.LIKESFIRST);
        } else if (id == R.id.by_newest) {
            mAdapter.setOrderBy(Fachada.OrderCommentsBy.NEWERFIRST);
        } else if (id == R.id.by_oldest) {
            mAdapter.setOrderBy(Fachada.OrderCommentsBy.OLDERFIRST);
        }

        return super.onOptionsItemSelected(item);
    }

    public void send(View v) {
        if (!et_comment.getText().toString().equals("")) {
            final Comment comment = new Comment(PrintUser.getCurrentUser(), article, et_comment.getText().toString());

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    comment.saveInBackground();
                    try {
                        comment.pin();
                        mAdapter.refresh();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.run();
            et_comment.setText("");

        }
    }

}

class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    Vector<Comment> comments = new Vector<Comment>();
    Article article;
    Activity ctx;
    SwipeRefreshLayout swipeRefreshLayout;
    Fachada.OrderCommentsBy orderBy;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Comment comment;
        public TextView tv_user_name;
        public RelativeTimeTextView tv_date;
        public TextView tv_comment;
        public TextView tv_up_cont;
        public TextView tv_down_cont;
        public Button bt_up;
        public Button bt_down;
        public Button bt_reply;
        public SimpleDraweeView iv_user_photo;
        Activity ctx;
        public View root;


        public ViewHolder(View v) {
            super(v);
            tv_user_name = (TextView) v.findViewById(R.id.tv_user_name);
            tv_date = (RelativeTimeTextView) v.findViewById(R.id.tv_date);
            tv_comment = (TextView) v.findViewById(R.id.tv_comment);
            tv_up_cont = (TextView) v.findViewById(R.id.tv_up_cont);
            tv_down_cont = (TextView) v.findViewById(R.id.tv_down_cont);
            bt_up = (Button) v.findViewById(R.id.bt_up);
            bt_down = (Button) v.findViewById(R.id.bt_down);
            bt_reply = (Button) v.findViewById(R.id.bt_reply);
            iv_user_photo = (SimpleDraweeView) v.findViewById(R.id.iv_user_photo);
            root = v;

        }

        public void update(final Comment comment, final Activity ctx) {
            this.comment = comment;
            this.ctx = ctx;
            //tv_user_name.setText(comment.g);
            tv_date.setReferenceTime(comment.getCreatedAt().getTime());
            tv_comment.setText(comment.getContent());
            tv_up_cont.setText("" + comment.getUps());
            tv_down_cont.setText("" + comment.getDowns());
            bt_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comment.doLike();
                    tv_up_cont.setText("" + comment.getUps());
                }
            });
            bt_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comment.doDislike();
                    tv_down_cont.setText("" + comment.getDowns());
                }
            });
            bt_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, CommentActivity.class);
                    intent.putExtra("commentId", comment.getObjectId());
                    ctx.startActivity(intent);
                }
            });
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, CommentActivity.class);
                    intent.putExtra("commentId", comment.getObjectId());
                    ctx.startActivity(intent);
                }
            });
            tv_user_name.setText(comment.getUserName());
            iv_user_photo.setImageURI(Uri.parse(comment.getUserPhoto()));


        }


    }

    public void refresh() {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });

                PrintedPost.fachada.updateComments(article);
                Log.d("Refresh", "Refreshing started");
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.d("Refresh", "Refreshing Finished");
                comments = PrintedPost.fachada.getComments(article, orderBy);
                notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        asyncTask.execute();


    }

    public void setOrderBy(Fachada.OrderCommentsBy orderBy) {
        this.orderBy = orderBy;
        refresh();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentsAdapter(Activity activity, SwipeRefreshLayout swipeRefreshLayout, final Article article, Fachada.OrderCommentsBy commentsBy) {
        this.article = article;
        this.ctx = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.orderBy = commentsBy;
        comments = PrintedPost.fachada.getComments(article, orderBy);
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintedPost.fachada.updateComments(article);
                ctx.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                                Log.e("ADAPTER", "data changed notified");
                            }
                        }
                );
            }
        }).start();
    }


    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_root, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder holder, int position) {
        holder.update(comments.elementAt(position), ctx);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return comments.size();
    }

}
