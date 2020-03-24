package com.jackson_siro.visongbook.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class DdSongPad extends AppCompatActivity {

    private boolean showExtra = true;
    private EditText inputTitle, inputContent;
    String noteTitle, noteContent;
    private SQLiteHelper sqlDB = new SQLiteHelper(this);

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private LinearLayout mTitle, mContent;

    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_songpad);
        showExtra = true;
        mTitle = findViewById(R.id.title);
        mContent = findViewById(R.id.content);

        inputTitle = findViewById(R.id.input_title);
        inputContent = findViewById(R.id.input_content);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        toolbarSet();

        inputTitle.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence newTitle, int start, int before, int count) {
                actionBar.setTitle(newTitle);
            }
        });

        inputContent.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence newTitle, int start, int before, int count) {
                showExtraInput();
            }
        });

    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Save a New Song");
    }

    private void showExtraInput()
    {
        if (showExtra)
        {
            showExtra = false;
            mTitle.setVisibility(View.GONE);
        }
        else
        {
            showExtra = true;
            mTitle.setVisibility(View.VISIBLE);
        }
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

        prefedit.putInt("app_last_mysong_no", song.number).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.ee_editor, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_proceed:
                noteTitle = inputTitle.getText().toString().trim();
                noteContent = inputContent.getText().toString().trim();

                if (noteTitle.isEmpty())
                    Toast.makeText(this, "Please type something into the Title of the Song", Toast.LENGTH_LONG).show();
                else if (noteTitle.length() < 5)
                    Toast.makeText(this, "Please type something into the Title of the Song", Toast.LENGTH_LONG).show();
                else if (noteContent.isEmpty())
                    Toast.makeText(this, "Please type something into the Content of the Song", Toast.LENGTH_LONG).show();
                else if (noteContent.length() < 10)
                    Toast.makeText(this, "Please type something into the Content of the Song", Toast.LENGTH_LONG).show();
                else {
                    SaveNewSong();
                    finish();
                }
                return true;

            case R.id.action_extra:
                showExtraInput();
                return true;

            /*case R.id.action_cancel:
                if (!noteTitle.isEmpty() && noteTitle.length() < 5) {
                    SaveNewSong();
                    finish();
                }
                return true;*/

            default:
                return false;
        }
    }

}
