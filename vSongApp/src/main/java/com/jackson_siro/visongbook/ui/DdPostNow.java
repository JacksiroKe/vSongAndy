package com.jackson_siro.visongbook.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.PostModel;

public class DdPostNow extends AppCompatActivity {

    private EditText inputTitle, inputContent;
    String noteTitle, noteContent;
    private SQLiteHelper sqlDB = new SQLiteHelper(this);

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_post_now);
        inputTitle = findViewById(R.id.input_title);
        inputContent = findViewById(R.id.input_content);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        toolbarSet();

        FloatingActionButton fab = findViewById(R.id.fab_action);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteTitle = inputTitle.getText().toString().trim();
                noteContent = inputContent.getText().toString().trim();

                if (noteTitle.isEmpty())
                    Snackbar.make(view, "Please type something into the Title", Snackbar.LENGTH_SHORT).show();
                else if (noteTitle.length() < 5)
                    Snackbar.make(view, "Please type something into the Title", Snackbar.LENGTH_SHORT).show();
                else if (noteContent.isEmpty())
                    Snackbar.make(view, "Please type something into the Content", Snackbar.LENGTH_SHORT).show();
                else if (noteContent.length() < 10)
                    Snackbar.make(view, "Please type something into the Content", Snackbar.LENGTH_SHORT).show();
                else SaveNewSong();
            }
        });
    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Save a New Note");
        actionBar.setSubtitle("You may publish it later as a Song");
    }

    public void SaveNewSong()
    {
        PostModel song = new PostModel();
        song.bookid = 0;
        song.categoryid = 0;
        song.basetype = "S";
        song.number = prefget.getInt("app_last_mysong_no", 0) + 1;
        song.alias = "";
        song.title = noteTitle;
        song.tags = "";
        song.content = noteContent;
        song.created = sqlDB.GetDate();
        song.what = "";
        song.when = "";
        song.where = "";
        song.netthumbs = 0;
        song.views = 0;
        song.acount = 0;
        song.userid = prefget.getInt("user_userid", 0);
        song.isfav = 0;
        sqlDB.addSong(song);

        Toast.makeText(getApplicationContext(), "New Song " + noteTitle + " saved!", Toast.LENGTH_LONG).show();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.bb_proceed, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_proceed:
                noteTitle = inputTitle.getText().toString().trim();
                noteContent = inputContent.getText().toString().trim();

                if (noteTitle.isEmpty())
                    Toast.makeText(this, "Please type something into the Title", Toast.LENGTH_LONG).show();
                else if (noteTitle.length() < 5)
                    Toast.makeText(this, "Please type something into the Title", Toast.LENGTH_LONG).show();
                else if (noteContent.isEmpty())
                    Toast.makeText(this, "Please type something into the Content", Toast.LENGTH_LONG).show();
                else if (noteContent.length() < 10)
                    Toast.makeText(this, "Please type something into the Content", Toast.LENGTH_LONG).show();
                else SaveNewSong();
                return true;

            default:
                return false;
        }
    }

}
