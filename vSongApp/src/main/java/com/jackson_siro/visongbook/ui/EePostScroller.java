package com.jackson_siro.visongbook.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.PostModel;
import com.jackson_siro.visongbook.R;

public class EePostScroller extends AppCompatActivity {

    private static final String EXT_OBJ_ID = "key.EXTRA_OBJ_ID";
    private static final String EXT_NOTIFICATION_ID = "key.NOTIFICATION.ID";

    private boolean haschorus = false;
    private Toolbar toolbar;
    private ActionBar actionBar;

    private int cur_song = 0, cur_stanza = 0, cur_font = 25;

    private MenuItem wishlist, favourites;

    private FloatingActionButton fab_smaller, fab_bigger, fab_lastsong, fab_nosong, fab_nextsong;
    private ImageView notice;
    private TextView post_content, post_stanzano;
    private String[] songconts, songcontent, stanzanos;

    PostModel Song;
    private SQLiteHelper db = new SQLiteHelper(this);

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ee_post_scroller);
        cur_song = getIntent().getIntExtra(EXT_OBJ_ID, 0);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        post_content = findViewById(R.id.post_content);
        post_stanzano = findViewById(R.id.number);
        fab_smaller = findViewById(R.id.fab_smaller);
        fab_bigger = findViewById(R.id.fab_bigger);
        fab_lastsong = findViewById(R.id.fab_lastsong);
        fab_nosong = findViewById(R.id.fab_nosong);
        fab_nextsong = findViewById(R.id.fab_nextsong);
        //notice = findViewById(R.id.notice);

        toolbarSet();

        Song = db.viewSong(cur_song);
        showSongContent();

        fab_smaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cur_font = cur_font - 2;
                    post_content.setTextSize(cur_font);
                }
                catch (Exception e) {
                    cur_font = cur_font + 2;
                }
            }
        });

        fab_bigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cur_font = cur_font + 2;
                    post_content.setTextSize(cur_font);
                }
                catch (Exception e) {
                    cur_font = cur_font - 2;
                }
            }
        });

        fab_lastsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cur_song = cur_song - 1;
                    Song = db.viewSong(cur_song);
                    showSongContent();
                    cur_stanza = 0;
                }
                catch (Exception e) {
                    cur_song = cur_song + 1;
                    Toast.makeText(getApplicationContext(), "Invalid action!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        fab_nextsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cur_song = cur_song + 1;
                    Song = db.viewSong(cur_song);
                    showSongContent();
                    cur_stanza = 0;
                }
                catch (Exception e) {
                    cur_song = cur_song - 1;
                    Toast.makeText(getApplicationContext(), "Invalid action!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Song View");
    }

    @SuppressLint("NewApi")
    private void showSongContent() {
        if (Song.songid == "1") fab_lastsong.hide();
        else fab_nosong.hide();

        actionBar.setTitle(Song.title);
        actionBar.setSubtitle(Song.number + "# | " + Song.categoryname);

        songconts = TextUtils.split(Song.content, "\n\n");

    }

    private void setSongContent (int stanzano){

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.ee_post_view, menu);
        favourites = menu.findItem(R.id.action_wish);
        wishlist = menu.findItem(R.id.user_comment);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_wish:

                Toast.makeText(getApplicationContext(), "This feature will be implemented in subsequent updates", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_share:
                try {
                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                    String theTitle = Song.number + "# " + Song.title;
                    String theContent = theTitle + "\n" + Song.categoryname + "\n\n" + Song.content + "\n\nvia vSongBook for Android\n" +
                            "https://play.google.com/store/apps/details?id=com.jackson_siro.visongbook";

                    share.putExtra(Intent.EXTRA_SUBJECT, theTitle);
                    share.putExtra(Intent.EXTRA_TEXT, theContent);
                    startActivity(Intent.createChooser(share, "Share the song: " + theTitle));

                } catch (Exception e) {
                }
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
