package com.jackson_siro.visongbook.songbooks;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.tools.*;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OwnSongEdit extends AppFunctions {
	
	TextView mSongCont;
	Cursor cursor;
	public Uri mUri;
    SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
    
    SharedPreferences vSettings;
	SharedPreferences.Editor localEditor;
		
    String VSB_SETTINGS, FONT_SIZE, CURRENT_SONG;
    public int CurrSong, FontSize;
	SongMine currentSong;
	EditText title, key, content;
	String song_updated, song_title, song_key, song_content;
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.song_editor);
    	vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        changeStatusBarColor();	
		FontSize = PreferenceManager.getDefaultSharedPreferences(this).getInt("as_vsb_font_size", 15);        
		mUri = getIntent().getData();
        CurrSong = Integer.parseInt(mUri.toString().replaceAll("\\D+", ""));        
        
        title = (EditText) findViewById(R.id.title);
        key = (EditText) findViewById(R.id.key);
		content = (EditText) findViewById(R.id.content);
		currentSong = sbDB.readMySong(CurrSong);
		
		long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'yyyy");  
        song_updated = formatter.format(curDateTime);        
        
        title.setText(currentSong.getTitle());
        key.setText(currentSong.getKey());
        content.setText(currentSong.getTitle().replace("\n\n", "`"));
	}

     @Override
  	public void onBackPressed() {
     	 //Intent intent = new Intent(this, CcSongBook.class);
     	 //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         //startActivity(intent);
         finish();
  	}
     
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.proceed, menu);
        return true;
    }

	public void UpdateSong() {
		currentSong.setTitle(title.getText().toString());
		currentSong.setKey(key.getText().toString());
		currentSong.setContent(content.getText().toString());
		currentSong.setUpdated(song_updated);
		sbDB.updateMySong(currentSong); 
		Toast.makeText(getApplicationContext(), "The song: " + currentSong.getTitle() + " updated", Toast.LENGTH_SHORT).show();		
		finish();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.donehere:
            	UpdateSong();
            	finish();
                return true;
             
            default:
                return false;
        }
	}
	
}
