package com.jackson_siro.vsongbook.notepad;

import com.jackson_siro.vsongbook.*;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class OwnSongNew extends ActionBarActivity {
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String FONT_SIZE = "font_size";
	
	String songTitle;
	String songContent;
	//OwnSong selectedSong;
	//SQLiteHelper db;
	SQLiteHelper db = new SQLiteHelper(this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notepad_new);
		
	}
	
	public void AddNewSong() {
		
		EditText title = (EditText) findViewById(R.id.title);
		EditText content = (EditText) findViewById(R.id.content);
		
		songTitle = title.getText().toString();
		songContent = content.getText().toString();
		
		//db.onUpgrade(db.getWritableDatabase(), 1, 2);

		db.createSong(new OwnSong(songTitle, songContent)); 
		Toast.makeText(getApplicationContext(), "A new song has been added.", Toast.LENGTH_SHORT).show();
		
		finish();
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
            	 AddNewSong();
                 return true;
                 
             case R.id.cancel:
             	finish();
                 return true;

                 
             default:
                 return false;
         }
     } 
}
