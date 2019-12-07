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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jackson_siro.visongbook.adapters.StanzaListAdapter;
import com.jackson_siro.visongbook.components.SwipeTouchListener;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.PostModel;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.StanzaModel;

import java.util.ArrayList;
import java.util.List;

public class EePostView extends AppCompatActivity {

    private static final String SONG_ID = "key.EXTRA_OBJ_ID";
    private static final String EXT_NOTIFICATION_ID = "key.NOTIFICATION.ID";

    private boolean haschorus = false, showMorefabs = false, isScrollable = false;
    private Toolbar toolbar;
    private ActionBar actionBar;

    private int cur_hint = 0, cur_song = 0, cur_stanza = 0, cur_font = 25;

    private MenuItem wishlist, favourites;

    private ImageView hintsView;

    PostModel Song;
    private SQLiteHelper db = new SQLiteHelper(this);
    private RecyclerView recyclerView;
    private RelativeLayout singleView;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private TextView post_content, post_stanzano;
    private String[] songconts, songcontent, stanzanos;
    private String songcontents, stanzanumbers;
    private StanzaListAdapter stanzasAdapter;

    private FloatingActionButton fabmore, fabsmaller, fabbigger, fabview;
    private FrameLayout action1, action2, action3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ee_post_view);
        cur_song = Integer.parseInt(getIntent().getStringExtra(SONG_ID));

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        hintsView = findViewById(R.id.hintView);
        hintsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CheckForHints();
            }
        });

        action1 = findViewById(R.id.frm_action1);
        action2 = findViewById(R.id.frm_action2);
        action3 = findViewById(R.id.frm_action3);

        singleView = findViewById(R.id.single_view);
        singleView.setOnTouchListener(new SwipeTouchListener(EePostView.this){
            public void onSwipeTop() {
                NextStanza();
            }
            public void onSwipeBottom() {
                LastStanza();
            }
            public void onSwipeLeft() {
                NextSong();
            }
            public void onSwipeRight() {
                LastSong();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setOnTouchListener(new SwipeTouchListener(EePostView.this){
            public void onSwipeLeft() {
                NextSong();
            }
            public void onSwipeRight() {
                LastSong();
            }
        });

        post_content = findViewById(R.id.post_content);
        post_stanzano = findViewById(R.id.number);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        toolbarSet();
        CheckForViewType();

        fabmore = findViewById(R.id.fab_action);
        fabmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (showMorefabs)
                {
                    showMorefabs = false;
                    action1.setVisibility(View.GONE);
                    action2.setVisibility(View.GONE);
                    action3.setVisibility(View.GONE);
                    fabmore.setImageResource(R.drawable.ic_add);
                }
                else
                {
                    showMorefabs = true;
                    action1.setVisibility(View.VISIBLE);
                    action2.setVisibility(View.VISIBLE);
                    action3.setVisibility(View.VISIBLE);
                    fabmore.setImageResource(R.drawable.ic_clear);
                }
            }
        });


        fabsmaller = findViewById(R.id.fab_action1);
        fabsmaller.setOnClickListener(new View.OnClickListener() {
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


        fabbigger = findViewById(R.id.fab_action2);
        fabbigger.setOnClickListener(new View.OnClickListener() {
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

        fabview = findViewById(R.id.fab_action3);
        fabview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetSongView();
            }
        });

        Song = db.viewSong(cur_song);
        showSongContent();

        if (!prefget.getBoolean("app_seen_swipe_hints", false)) {
            recyclerView.setVisibility(View.GONE);
            singleView.setVisibility(View.GONE);
            hintsView.setVisibility(View.VISIBLE);
        }
    }

    public void CheckForViewType(){
        if (prefget.getString("app_song_presentation", "") == "scroll")
        {
            isScrollable = false;
            recyclerView.setVisibility(View.VISIBLE);
            singleView.setVisibility(View.GONE);
        }
        else
        {
            isScrollable = true;
            recyclerView.setVisibility(View.GONE);
            singleView.setVisibility(View.VISIBLE);
        }
    }

    public void CheckForHints() {
        switch (cur_hint)
        {
            case 0:
                hintsView.setImageResource(R.drawable.swipe_1);
                break;

            case 1:
                hintsView.setImageResource(R.drawable.swipe_2);
                break;

            case 2:
                hintsView.setImageResource(R.drawable.swipe_3);
                break;

            case 3:
                hintsView.setImageResource(R.drawable.swipe_4);
                break;

            case 4:
                prefedit.putBoolean("app_seen_swipe_hints", true).apply();
                CheckForViewType();
                break;
        }
        cur_hint = cur_hint + 1;
    }

    public void LastStanza() {
        try {
            cur_stanza = cur_stanza - 1;
            setSongContent(cur_stanza);
        }
        catch (Exception e) {
            cur_stanza = cur_stanza + 1;
        }
    };

    public void NextStanza() {
        try {
            cur_stanza = cur_stanza + 1;
            setSongContent(cur_stanza);
        }
        catch (Exception e) {
            cur_stanza = cur_stanza - 1;
        }
    }

    private void LastSong()
    {
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

    private void NextSong()
    {
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

    private void SetSongView()
    {
        if (isScrollable)
        {
            isScrollable = false;
            recyclerView.setVisibility(View.VISIBLE);
            singleView.setVisibility(View.GONE);
            prefedit.putString("app_song_presentation", "slides").apply();
        }
        else
        {
            isScrollable = true;
            recyclerView.setVisibility(View.GONE);
            singleView.setVisibility(View.VISIBLE);
            prefedit.putString("app_song_presentation", "slides").apply();
        }
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
        actionBar.setTitle(Song.title);
        actionBar.setSubtitle(Song.number + "# | " + Song.categoryname);

        List<StanzaModel> stanzaItems = new ArrayList<StanzaModel>();
        songconts = TextUtils.split(Song.content, "\n\n");

        String VerseInfo = "VERSE 1 of " + songconts.length;
        stanzaItems.add( new StanzaModel(VerseInfo, songconts[0]) );

        if (Song.content.contains("CHORUS")) {
            haschorus = true;
            String Chorus = songconts[1].replace("CHORUS\n", "");
            songcontents = songconts[0] + "#" + Chorus;

            stanzanumbers = VerseInfo + "#CHORUS";
            stanzaItems.add( new StanzaModel("CHORUS", Chorus) );
            for (int i = 2; i < songconts.length; i++){
                VerseInfo = "VERSE " + (i + 1) + " of " + songconts.length;
                songcontents = songcontents + "#" + songconts[i] + "#" + Chorus;
                stanzanumbers = stanzanumbers + "#" + VerseInfo + "#CHORUS";

                stanzaItems.add( new StanzaModel(VerseInfo, songconts[i]) );
                stanzaItems.add( new StanzaModel("CHORUS", Chorus) );
            }
        } else {
            haschorus = false;
            try
            {
                stanzanumbers = VerseInfo;
                songcontents = songconts[0];
                for (int i = 1; i < songconts.length; i++){
                    VerseInfo = "VERSE " + (i + 1) + " of " + songconts.length;
                    songcontents = songcontents + "#" + songconts[i];
                    stanzanumbers = stanzanumbers + "#" + VerseInfo;
                    stanzaItems.add( new StanzaModel(VerseInfo, songconts[i]) );
                }
            }
            catch (Exception ex)
            {
                stanzanumbers = "VERSE 1";
                songcontents = songconts[0];
            }
        }

        stanzasAdapter = new StanzaListAdapter(this, stanzaItems, true, cur_font);
        recyclerView.setAdapter(stanzasAdapter);

        songcontent = TextUtils.split(songcontents, "#");
        stanzanos = TextUtils.split(stanzanumbers, "#");

        setSongContent(cur_stanza);
        post_content.setTextSize(cur_font);
    }

    private void setSongContent (int stanzano)
    {
        post_content.setText(songcontent[stanzano]);
        post_stanzano.setText(stanzanos[stanzano]);

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
