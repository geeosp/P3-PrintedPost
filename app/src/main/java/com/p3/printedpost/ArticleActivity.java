package com.p3.printedpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.p3.printedpost.parseObjects.Article;

public class ArticleActivity extends AppCompatActivity {
    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Intent intent = getIntent();
        String articleId = intent.getStringExtra("articleId");
        article = PrintedPost.fachada.getArticle(articleId);
        Log.d("Article", "title: " + article.getTitle());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(article.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);
        RelativeTimeTextView tv_date =(RelativeTimeTextView) findViewById(R.id.tv_article_date);
        tv_date.setReferenceTime(article.getCreatedAt().getTime());
        TextView tv_article_excerpt = (TextView) findViewById(R.id.tv_article_excerpt);
        tv_article_excerpt.setText(article.getExcerpt());


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
