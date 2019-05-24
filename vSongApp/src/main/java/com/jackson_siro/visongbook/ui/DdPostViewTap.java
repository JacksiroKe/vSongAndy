package com.jackson_siro.visongbook.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.PostModel;

public class DdPostViewTap extends AppCompatActivity {

    private static final String EXT_OBJ_ID = "key.EXTRA_OBJ_ID";
    private static final String EXT_NOTIFICATION_ID = "key.NOTIFICATION.ID";

    private boolean haschorus = false;
    private int postid;
    private Toolbar toolbar;
    private ActionBar actionBar;

    private int cur_stanza = 0, cur_font = 30;

    private MenuItem wishlist, favourites;

    private LinearLayout click_last, click_next, click_left, click_right;
    private ImageView notice;
    private TextView post_content, post_stanzano;
    private String[] songconts, songcontent, stanzanos;
    private String songcontents, stanzanumbers;

    PostModel Song;
    private SQLiteHelper db = new SQLiteHelper(this);

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_post_view_tap);
        postid = getIntent().getIntExtra(EXT_OBJ_ID, 0);
        Song = db.viewSong(postid);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        post_content = findViewById(R.id.post_content);
        post_stanzano = findViewById(R.id.number);
        click_last = findViewById(R.id.click_last);
        click_next = findViewById(R.id.click_next);
        click_left = findViewById(R.id.click_left);
        click_right = findViewById(R.id.click_right);
        notice = findViewById(R.id.notice);

        toolbarSet();
        showSongContent();

        if (!prefget.getBoolean("app_songview_hint", false)) {
            showTutorial();
            click_next.setBackgroundResource(R.color.colorAccent);
            notice.setImageResource(R.drawable.click_next);
            notice.setVisibility(View.VISIBLE);
            post_stanzano.setVisibility(View.GONE);
            post_content.setVisibility(View.GONE);
        }

        click_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (!prefget.getBoolean("app_songview_hint", false)) {
                click_last.setBackgroundResource(R.color.transparent);
                click_right.setBackgroundResource(R.color.colorAccent);
                notice.setImageResource(R.drawable.click_right);
            } else {
                try {
                    cur_stanza = cur_stanza - 1;
                    setSongContent(cur_stanza);
                } catch (Exception e) {
                    cur_stanza = cur_stanza + 1;
                }
            }
            }
        });

        click_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!prefget.getBoolean("app_songview_hint", false)) {
                    click_next.setBackgroundResource(R.color.transparent);
                    click_last.setBackgroundResource(R.color.colorAccent);
                    notice.setImageResource(R.drawable.click_last);
                } else {
                    try {
                        cur_stanza = cur_stanza + 1;
                        setSongContent(cur_stanza);
                    }
                    catch (Exception e) {
                        cur_stanza = cur_stanza - 1;
                    }
                }
            }
        });

        click_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (!prefget.getBoolean("app_songview_hint", false)) {
                click_left.setBackgroundResource(R.color.transparent);
                notice.setVisibility(View.GONE);
                post_stanzano.setVisibility(View.VISIBLE);
                post_content.setVisibility(View.VISIBLE);
                prefedit.putBoolean("app_songview_hint", true).commit();
            } else {
                try {
                    cur_font = cur_font - 2;
                    post_content.setTextSize(cur_font);
                }
                catch (Exception e) {
                    cur_font = cur_font + 2;
                }
            }
            }
        });

        click_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (!prefget.getBoolean("app_songview_hint", false)) {
                click_right.setBackgroundResource(R.color.transparent);
                click_left.setBackgroundResource(R.color.colorAccent);
                notice.setImageResource(R.drawable.click_left);
            } else {
                try {
                    cur_font = cur_font + 2;
                    post_content.setTextSize(cur_font);
                }
                catch (Exception e) {
                    cur_font = cur_font - 2;
                }
            }
            }
        });

    }

    private void showTutorial(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Just a minute!");
        builder.setMessage("Take time to go through our short tutorial on how the new song viewer works.\n\n" +
                "Tap on the pointed section as directed till you are done to view how this works");
        builder.setNegativeButton("Okay Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.show();
    }

    @SuppressLint("NewApi")
    private void showSongContent() {
        actionBar.setTitle(Song.title);
        actionBar.setSubtitle(Song.number + "# " + Song.categoryname);

        songconts = TextUtils.split(Song.content, "\n\n");

        if (Song.content.contains("CHORUS")) {
            haschorus = true;
            songcontents = songconts[0] + "#" + songconts[1].replace("CHORUS\n", "");
            stanzanumbers = "VERSE 1 of " + songconts.length + "#CHORUS";
            for (int i = 2; i < songconts.length; i++){
                songcontents = songcontents + "#" + songconts[i] + "#" + songconts[1].replace("CHORUS\n", "");
                stanzanumbers = stanzanumbers + "#VERSE " + (i + 1) + " of " + songconts.length + "#CHORUS";
            }
        } else {
            haschorus = false;
            stanzanumbers = "VERSE 1 of " + songconts.length;
            songcontents = songconts[0];
            for (int i = 1; i < songconts.length; i++){
                songcontents = songcontents + "#" + songconts[i];
                stanzanumbers = stanzanumbers + "#VERSE " + (i + 1) + " of " + songconts.length;
            }
        }
        songcontent = TextUtils.split(songcontents, "#");
        stanzanos = TextUtils.split(stanzanumbers, "#");

        setSongContent(cur_stanza);
        post_content.setTextSize(cur_font);

    }

    private void setSongContent (int stanzano){
        post_content.setText(songcontent[stanzano]);
        post_stanzano.setText(stanzanos[stanzano]);
    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Song View");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.dd_post_view, menu);
        favourites = menu.findItem(R.id.action_wish);
        wishlist = menu.findItem(R.id.user_comment);


        /*View view = MenuItemCompat.getActionView(wishlist);

        if (db.isDataExist(postid)){
            favourites.setIcon(R.drawable.ic_favorite_black_48dp);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(wishlist);
            }
        });*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_variable = item.getItemId();
        if (item_variable == android.R.id.home){
            onBackPressed();
        }else if (item_variable == R.id.action_wish){
            /*try{
                if (databaseHelpers.isDataExist(postSingle.postid)){
                    databaseHelpers.deleteData(postSingle.postid);
                    favourites.setIcon(R.drawable.ic_favorite_border_black_24dp);
                }else{
                    //databaseHelpers.addData(postSingle.postid, postSingle.title, postSingle.code, postSingle.category, postSingle.image, postSingle.content, postSingle.created);
                    favourites.setIcon(R.drawable.ic_favorite_black_48dp);
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), getString(R.string.please_wait_loading_data), Toast.LENGTH_LONG).show();
            }*/
        }
        
        return super.onOptionsItemSelected(item);
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
