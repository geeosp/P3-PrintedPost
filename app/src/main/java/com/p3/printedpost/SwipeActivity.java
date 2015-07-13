package com.p3.printedpost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.p3.printedpost.parseObjects.Article;
import com.p3.printedpost.parseObjects.PrintUser;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Locale;


public class SwipeActivity extends AppCompatActivity {
    final int transitionTime = 350;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final Toolbar toolbar = (Toolbar) findViewById(R.id.as_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TransitionDrawable transition = (TransitionDrawable) toolbar.getBackground();
                if (position == 0) {
                    ScanFragment fscan = (ScanFragment) mSectionsPagerAdapter.getItem(0);
                    transition.reverseTransition(transitionTime);
                    if (fscan != null)
                        fscan.resume();
                } else {
                    ScanFragment fscan = (ScanFragment) mSectionsPagerAdapter.getItem(0);

                    transition.startTransition(transitionTime);
                    if (fscan != null)
                        fscan.pause();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // PrintedPost.fachada.updateArticles();
        setEmailAndUserNameToServer();
    }

    public void openRecents(View v) {
        ScanFragment fscan = (ScanFragment) mSectionsPagerAdapter.getItem(0);
        if (fscan != null)
            fscan.pause();
        mViewPager.setCurrentItem(1);
        Log.d("Geeo", "openrecents clicked");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_swipe, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    // Camera fragment activity
                    return new ScanFragment();
                case 1:
                    return new RecentsFragment();
                default:
                    return PlaceholderFragment.newInstance(position + 1);

            }


        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);

            }
            return null;
        }
    }

    public void setEmailAndUserNameToServer() {

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Log.e("oi", object.toString());
                        PrintUser user = PrintUser.getCurrentUser();
                        String name = "", email = "";
                        try {
                            name = object.getString("name");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            email = object.getString("email");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        user.put("name", name);
                        try {
                            user.setEmail(email);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        user.saveInBackground();
                        try {
                            String userid = object.getString("id");
                            saveFacebookPhoto(userid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, email");
        request.setParameters(parameters);
        request.executeAsync();


    }

    void saveFacebookPhoto(String userid) {
        final String id = userid;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    URL imageUrl = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                    bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    final ParseFile fotofile = new ParseFile("photo.png", byteArray);
                    Log.e("Facebook", "Salvando foto..");
                    fotofile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            PrintUser current = PrintUser.getCurrentUser();
                            current.put("photo", fotofile);
                            current.saveEventually(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Log.e("Facebook", "Foto salva");
                                }
                            });

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("ok", "ok");

            }
        });
        t.start();
    }

    public void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void seek(final String articleId) {
        View vv = findViewById(R.id.rl_warnings);
        vv.setVisibility(View.VISIBLE);
        final View lookingForArticle = vv.findViewById(R.id.ll_looking_for_article);
        final View articleNotFound = vv.findViewById(R.id.ll_article_not_found);
        final View articlefound = vv.findViewById(R.id.ll_article_found);
        final Article article = PrintedPost.fachada.getArticle(articleId);
        if(article==null){
            lookingForArticle.setVisibility(View.GONE);
            articlefound.setVisibility(View.GONE);
            articleNotFound.setVisibility(View.VISIBLE);
        }else{
            TextView tv_article_title = (TextView) articlefound.findViewById(R.id.tv_article_title);
            tv_article_title.setText(article.getTitle());
            TextView tv_article_excerpt = (TextView) articlefound.findViewById(R.id.tv_article_excerpt);
            tv_article_excerpt.setText(article.getExcerpt());
            RelativeTimeTextView tv_date = (RelativeTimeTextView) articlefound.findViewById(R.id.tv_article_date);
            tv_date.setReferenceTime(article.getCreatedAt().getTime());
            Button bt_show_article = (Button) articlefound.findViewById(R.id.bt_show_article);
            bt_show_article.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        article.pin();
                        PrintUser.getCurrentUser().getRelation("articles").add(article);
                        PrintUser.getCurrentUser().saveInBackground();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                    intent.putExtra("articleId", articleId);
                    startActivity(intent);
                }
            });
        }
    }

    public void dismiss(View v) {
        View vv = findViewById(R.id.rl_warnings);
        vv.setVisibility(View.GONE);
    }

}
