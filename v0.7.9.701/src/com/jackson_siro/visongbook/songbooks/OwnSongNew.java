package com.jackson_siro.visongbook.songbooks;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.tools.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class OwnSongNew extends AppFunctions {
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String FONT_SIZE = "font_size";
	
	String song_posted, song_title, song_key, song_content;
	SongMine selectedSong;
	EditText title, key, content;
	public SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_editor);

        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'yyyy");  
        song_posted = formatter.format(curDateTime);        
        
        changeStatusBarColor();
        title = (EditText) findViewById(R.id.title);
		key = (EditText) findViewById(R.id.key);
		content = (EditText) findViewById(R.id.content);
		
	}

	public void AddNewSong() {		
		song_title = title.getText().toString();
		song_key = key.getText().toString();
		song_content = content.getText().toString();		
		sbDB.createMySong(new SongMine(song_title, song_content, song_key, song_posted, "", "0", 0));
		Toast.makeText(getApplicationContext(), "New song: " + song_title + " added.", Toast.LENGTH_SHORT).show();
		finish();
	}
	
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.proceed, menu);
         return true;
     }
     

 	@Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case R.id.donehere:
            	 AddNewSong();
                 return true;
                 
             default:
                 return false;
         }
     }
}
