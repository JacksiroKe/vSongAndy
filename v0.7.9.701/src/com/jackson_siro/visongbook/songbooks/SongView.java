package com.jackson_siro.visongbook.songbooks;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.adaptor.*;
import com.jackson_siro.visongbook.tools.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class SongView extends AppFunctions {
	
	TextView mSongCont;
	Cursor cursor;
	public Uri mUri;
    
    SongBookSQLite db;
    
    SharedPreferences vSettings;
	SharedPreferences.Editor localEditor;
		
    String VSB_SETTINGS, FONT_SIZE, CURRENT_SONG;
    public int CurrSong, FontSize;
    ListView StanzasList;
	SongItem currentSong;
	ImageButton DeleteSong, GoOnline;
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.song_view);
        db = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
    	
        StanzasList =(ListView)findViewById(R.id.stanzalist);
        DeleteSong = (ImageButton) findViewById(R.id.btndelete);
		GoOnline = (ImageButton) findViewById(R.id.btnonline);
		
        DeleteSong.setVisibility(View.GONE);
        GoOnline.setVisibility(View.GONE);
        
        vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        changeStatusBarColor();	
		Long rated_me_not = vSettings.getLong("as_vsb_rated_me_not", 0);		
		Long time_used = (System.currentTimeMillis() - rated_me_not) / 1000;
				
		if  (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_rate_me", false)) {
			if (time_used > 18000 ) RateMePlease();
		}
		
		FontSize = PreferenceManager.getDefaultSharedPreferences(this).getInt("as_vsb_font_size", 15);        
		mUri = getIntent().getData();
        CurrSong = Integer.parseInt(mUri.toString().replaceAll("\\D+", ""));        
        openCurrentSong();
	}

    public void openCurrentSong(){
    	localEditor.putInt("as_vsb_curr_song", CurrSong).commit();
        currentSong = db.readSong(CurrSong);
        setTitle(currentSong.getTitle());           
        String[] Stanzas = TextUtils.split(currentSong.getContent(), "`");
        
        CustomSongView adapter = new CustomSongView(this, Stanzas, FontSize);
        StanzasList.setAdapter(adapter);
    }
    
	public void setTextContent(Cursor cursor, Uri uri){
		cursor.moveToFirst();
        setTitle(currentSong.getTitle());           
    	String[] Stanzas = TextUtils.split(currentSong.getContent(), "`");
        CustomSongView adapter = new CustomSongView(SongView.this, Stanzas, FontSize);
        StanzasList.setAdapter(adapter);
    }

	public void Previous (View view){
		int NewSong = CurrSong - 1;        
		try{ 
			CurrSong = NewSong;
            openCurrentSong();
		} catch(Exception e){
	       	e.printStackTrace();
	       	setTitle(R.string.app_name);
            this.mSongCont.setText(R.string.error_first_song);
	    }
	}

	public void Next (View view){
		int NewSong = CurrSong + 1;        
		try{ 
			CurrSong = NewSong;
            openCurrentSong();
		} catch(Exception e){
	       	e.printStackTrace();
	       	setTitle(R.string.app_name);
            this.mSongCont.setText(R.string.error_last_song);
	    }
	}

	public void Minus (View view){
		FontSize = FontSize - 1;
		if (FontSize > 5) {
			localEditor.putInt("as_vsb_font_size", FontSize).commit();                 
			openCurrentSong();
       }
	}
	
	public void Plus (View view){
		FontSize = FontSize + 1;
		if (FontSize > 5) {
			localEditor.putInt("as_vsb_font_size", FontSize).commit();
	        openCurrentSong();
       }
	}
    
	public void Share (View view){
		Intent share = new Intent("android.intent.action.SEND");
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, currentSong.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, currentSong.getContent() + "\n\n Via vSongBook");
        startActivity(Intent.createChooser(share, "Share this Song"));
	}

	public void Favourite (View view){
		try{ 
        	db.favoriteSong(currentSong);
    		Toast.makeText(getApplicationContext(), currentSong.getTitle() + " favourited!", Toast.LENGTH_SHORT).show();
		} catch(Exception e){
	       	e.printStackTrace();
	       	errorDialog();
	    }
	}

	public void CopySong (View view){
		try{ 
			Intent copyIntent = new Intent(getApplicationContext(), SongCopy.class);
            copyIntent.setData(Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong)));
            startActivity(copyIntent);
		} catch(Exception e){
	       	e.printStackTrace();
	       	errorDialog();
	    }		
	}

	public void EditSong (View view){
		if (isInternetOn()) {
			try{ 
				Intent editIntent = new Intent(getApplicationContext(), SongEdit.class);
				editIntent.setData(Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong)));
	            startActivity(editIntent);
			} catch(Exception e){
		       	e.printStackTrace();
		       	errorDialog();
		    }
		} else InternetNeeded();
	}

	public void DeleteSong (View view){
			
	}
	

	public void UploadSong (View view){
		
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
        inflater.inflate(R.menu.song_view, menu);
        /*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
	        case R.id.search: 
	            onSearchRequested();
	            return true;
            
            default:
                return false;
        }
    } 

}
