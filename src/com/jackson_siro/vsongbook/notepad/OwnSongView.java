package com.jackson_siro.vsongbook.notepad;

import com.jackson_siro.vsongbook.*;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class OwnSongView extends ActionBarActivity {
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String FONT_SIZE = "font_size";
	
	TextView songTitle;
	TextView contentName;
	OwnSong selectedSong;
	SQLiteHelper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.notepad_song);
		
		//songTitle = (TextView) findViewById(R.id.title);
		contentName = (TextView) findViewById(R.id.content);

		Intent intent = getIntent();
		int id = intent.getIntExtra("song", -1);

		// open the database of the application context
		db = new SQLiteHelper(getApplicationContext());

		// read the song with "id" from the database
		selectedSong = db.readSong(id);

		initializeViews();
	}

	public void initializeViews() {
		SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 25);
        String font_size = settings.getString(FONT_SIZE, "25");
        
		//songTitle.setText(selectedSong.getTitle());
		setTitle(selectedSong.getTitle());
		contentName.setText(selectedSong.getContent());
		contentName.setMovementMethod(new ScrollingMovementMethod());
		
		int myFonts = Integer.parseInt(font_size);
		contentName.setTextSize(myFonts);
        
	}
	
	/*
	public void update(View v) {
		Toast.makeText(getApplicationContext(), "This song is u-pdated.", Toast.LENGTH_SHORT).show();
		selectedSong.setTitle(((EditText) findViewById(R.id.titleEdit)).getText().toString());
		selectedSong.setContent(((EditText) findViewById(R.id.contentEdit)).getText().toString());

		// update song with changes
		db.updateSong(selectedSong);
		finish();
	}

	public void delete(View v) {
		Toast.makeText(getApplicationContext(), "This song is deleted.", Toast.LENGTH_SHORT).show();

		// delete selected song
		db.deleteSong(selectedSong);
		finish();
	}*/
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.own_song, menu);
        
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newsong:
            	Intent intent = new Intent(OwnSongView.this, OwnSongNew.class);
				startActivity(intent);
                return true;
                
            case R.id.delete:
            	AlertDialog.Builder builder = new AlertDialog.Builder(OwnSongView.this);
				builder.setTitle("Just a minute...");
				builder.setMessage("Are you sure you want to delete this song? This action is not reversable.");
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Toast.makeText(getApplicationContext(), "God bless you", Toast.LENGTH_LONG).show();
						
					}
				});

				builder.setPositiveButton("Amen", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Toast.makeText(getApplicationContext(), "You have deleted the song", Toast.LENGTH_LONG).show();
						db.deleteSong(selectedSong);
		        		finish();
					}
				});
				
				builder.show(); //To show the AlertDialog
				
                return true;

                
            default:
                return false;
        }
    }
	
}
