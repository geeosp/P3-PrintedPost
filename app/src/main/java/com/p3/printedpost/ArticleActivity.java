package com.p3.printedpost;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.p3.printedpost.parseObjects.Article;
import com.p3.printedpost.parseObjects.Comment;

import java.util.Vector;

public class ArticleActivity extends AppCompatActivity {
    Article article;
    private RecyclerView mRecyclerView;
    private CommentsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recents);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.srl_recents);
        // specify an adapter (see also next example)
        mAdapter = new CommentsAdapter(this, swipeRefreshLayout, article);
        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


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
        }

        return super.onOptionsItemSelected(item);
    }
}

class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    Vector<Comment> comments = new Vector<Comment>();
    Article article;
    Activity ctx;
    SwipeRefreshLayout swipeRefreshLayout;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Comment comment;
        public TextView tv_title;
        public RelativeTimeTextView tv_date;
        public TextView tv_excerpt;
        Activity ctx;

        public ViewHolder(View v) {
            super(v);
            tv_title = (TextView) v.findViewById(R.id.tv_article_title);
            tv_date = (RelativeTimeTextView) v.findViewById(R.id.tv_article_date);
            tv_excerpt = (TextView) v.findViewById(R.id.tv_article_exerpt);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, CommentActivity.class);
                    intent.putExtra("articleId", comment.getObjectId());
                    ctx.startActivity(intent);
                }
            });
        }

        public void update(Comment comment, Activity ctx) {
            this.comment = comment;
            this.ctx = ctx;
            /*
            tv_title.setText(article.getTitle());
            tv_excerpt.setText(article.getExcerpt());
            tv_date.setReferenceTime(article.getCreatedAt().getTime());
*/
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
                comments = PrintedPost.fachada.getComments(article);
                notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        asyncTask.execute();


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentsAdapter(Activity activity, SwipeRefreshLayout swipeRefreshLayout, final Article article) {
        this.article = article;
        this.ctx = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;

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
        return null;
    }

    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder holder, int position) {

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return comments.size();
    }
}
