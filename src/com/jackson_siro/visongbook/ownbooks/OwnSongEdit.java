package com.jackson_siro.visongbook.ownbooks;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.adaptor.SimpleGestureFilter;
import com.jackson_siro.visongbook.adaptor.SimpleGestureFilter.SimpleGestureListener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class OwnSongEdit extends ActionBarActivity {
	
	public static final String VSB_SETTINGS = "Vsb_Settings";
	public static final String FONT_SIZE = "font_size";
	
	private String songTitle;
	private String songContent;
	
	EditText title;
	EditText content;
	OwnSong selectedSong;
	SQLiteHelper db;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ob_edit);
		
		title = (EditText) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.content);
				
		Intent intent = getIntent();
		int qid = intent.getIntExtra("song", -1);

		db = new SQLiteHelper(getApplicationContext());
		selectedSong = db.readSong(qid);
		initializeViews();
	}
	
	public void initializeViews() {
		//SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 25);
        //String font_size = settings.getString(FONT_SIZE, "25");

		//title = (EditText) findViewById(R.id.title);
		//content = (EditText) findViewById(R.id.content);
		
		setTitle(selectedSong.getTitle());
		title.setText(selectedSong.getTitle());
		content.setText(selectedSong.getContent());
		
		//int myFonts = Integer.parseInt(font_size);
		//content.setTextSize(myFonts);
        
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.own_save, menu);
        
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.savethis:
            	UpdateSong();
                return true;
                
            case R.id.delete:
            	DeleteThis();				
                return true;
            case R.id.cancel:
            	finish();
            	
            default:
                return false;
        }
	}
	
	public void UpdateSong() {
		selectedSong.setTitle(((EditText) findViewById(R.id.title)).getText().toString());
		selectedSong.setContent(((EditText) findViewById(R.id.content)).getText().toString());
		db.updateSong(selectedSong); 
		Toast.makeText(getApplicationContext(), "The song: " + selectedSong.getTitle() + " has been updated", Toast.LENGTH_SHORT).show();
		
		finish();
	}
	
	private void DeleteThis(){
		AlertDialog.Builder builder = new AlertDialog.Builder(OwnSongEdit.this);
		builder.setTitle("Just a minute...");
		builder.setMessage("Are you sure you want to delete the song: " + selectedSong.getTitle() + "? This action is not reversable.");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Toast.makeText(getApplicationContext(), "God bless you", Toast.LENGTH_LONG).show();
				
			}
		});

		builder.setPositiveButton("Amen", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Toast.makeText(getApplicationContext(), "You have deleted the song: " + selectedSong.getTitle(), Toast.LENGTH_LONG).show();
				db.deleteSong(selectedSong);
        		finish();
			}
		});
		
		builder.show(); //To show the AlertDialog
	}
}
