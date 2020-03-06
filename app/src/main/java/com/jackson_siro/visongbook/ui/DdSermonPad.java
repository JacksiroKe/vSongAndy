package com.jackson_siro.visongbook.ui;

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

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.*;

public class DdSermonPad extends AppCompatActivity {

    private boolean showExtra = true;
    private LinearLayout mTitle, mSubtitle, mPlace, mPreacher, mContent;
    private EditText inputTitle, inputSubtitle, inputPlace, inputPreacher, inputContent;
    String noteTitle, noteSubtitle, notePlace, notePreacher, noteContent;
    private SQLiteHelper sqlDB = new SQLiteHelper(this);

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;


    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_sermonpad);
        showExtra = true;
        mTitle = findViewById(R.id.title);
        mSubtitle = findViewById(R.id.subtitle);
        mPreacher = findViewById(R.id.preacher);
        mPlace = findViewById(R.id.place);
        mContent = findViewById(R.id.content);

        inputTitle = findViewById(R.id.input_title);
        inputSubtitle = findViewById(R.id.input_subtitle);
        inputPreacher = findViewById(R.id.input_preacher);
        inputPlace = findViewById(R.id.input_place);
        inputContent = findViewById(R.id.input_content);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        toolbarSet();

        inputTitle.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence newTitle, int start, int before, int count) {
                updateTitles();
            }
        });

        inputSubtitle.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence newTitle, int start, int before, int count) {
                updateTitles();
            }
        });

        inputPreacher.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence newTitle, int start, int before, int count) {
                updateTitles();
            }
        });

        inputPlace.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence newTitle, int start, int before, int count) {
                updateTitles();
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
        actionBar.setTitle("Save a New Sermon");
    }

    private void updateTitles()
    {
        noteTitle = inputTitle.getText().toString().trim();
        noteSubtitle = inputSubtitle.getText().toString().trim();
        notePreacher = inputPreacher.getText().toString().trim();
        notePlace = inputPlace.getText().toString().trim();
        noteContent = inputContent.getText().toString().trim();

        if (noteSubtitle.length() > 3)
        {
            actionBar.setTitle(noteTitle + " (" + noteSubtitle + ")");
        }
        else actionBar.setTitle(noteTitle);

        if (notePreacher.length() > 3 && notePlace.length() < 3)
        {
            actionBar.setSubtitle("By " + notePreacher);
        }
        else if (notePreacher.length() < 3 && notePlace.length() > 3)
        {
            actionBar.setSubtitle("At " + notePlace);
        }
        else if (notePreacher.length() > 3 && notePlace.length() > 3)
        {
            actionBar.setSubtitle("by " + notePreacher + ", " + notePlace);
        }
    }

    private void showExtraInput()
    {
        if (showExtra)
        {
            showExtra = false;
            mTitle.setVisibility(View.GONE);
            mSubtitle.setVisibility(View.GONE);
            mPlace.setVisibility(View.GONE);
            mPreacher.setVisibility(View.GONE);
        }
        else
        {
            showExtra = true;
            mTitle.setVisibility(View.VISIBLE);
            mSubtitle.setVisibility(View.VISIBLE);
            mPlace.setVisibility(View.VISIBLE);
            mPreacher.setVisibility(View.VISIBLE);
        }
    }

    public void SaveNewSermon()
    {
        SermonModel sermon = new SermonModel();
        sermon.categoryid = 0;
        sermon.title = noteTitle;
        sermon.subtitle = noteSubtitle;
        sermon.preacher = notePreacher;
        sermon.place = notePlace;
        sermon.tags = "";
        sermon.content = noteContent;
        sermon.created = sqlDB.GetDate();
        sermon.isfav = 0;
        sqlDB.addSermon(sermon);
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
                updateTitles();
                if (noteTitle.isEmpty())
                    Toast.makeText(this, "Please type something into the Title of the Sermon", Toast.LENGTH_LONG).show();
                else if (noteTitle.length() < 5)
                    Toast.makeText(this, "Please type something into the Title of the Sermon", Toast.LENGTH_LONG).show();
                else if (noteContent.isEmpty())
                    Toast.makeText(this, "Please type something into the Content of the Sermon", Toast.LENGTH_LONG).show();
                else if (noteContent.length() < 10)
                    Toast.makeText(this, "Please type something into the Content of the Sermon", Toast.LENGTH_LONG).show();
                else {
                    SaveNewSermon();
                    finish();
                }
                return true;

            case R.id.action_extra:
                showExtraInput();
                return true;

            /*case R.id.action_cancel:
                if (!noteTitle.isEmpty() && noteTitle.length() < 5) {
                    SaveNewsermon();
                    finish();
                }
                return true;*/

            default:
                return false;
        }
    }

}
