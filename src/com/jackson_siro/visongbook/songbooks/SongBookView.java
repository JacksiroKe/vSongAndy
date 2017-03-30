package com.jackson_siro.visongbook.songbooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.adaptor.*;
import com.jackson_siro.visongbook.ownbooks.*;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class SongBookView extends ActionBarActivity {
	
	TableLayout MyToolbar;
	TextView mSongCont;
	ScrollView ScrollScroll;
	Cursor cursor;
    Uri mUri;
    
    SQLiteHelperF db = new SQLiteHelperF(this);
    
    SharedPreferences vSettings;
	SharedPreferences.Editor localEditor;
		
    String VSB_SETTINGS, FONT_SIZE, CURRENT_SONG;
    int CurrSong, FontSize;
    ListView StanzasList;
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.song_view);
        StanzasList =(ListView)findViewById(R.id.stanzalist);
        //ScrollScroll = (ScrollView) findViewById(R.id.ScrollSong);
        //getLayoutInflater().inflate(R.layout.song_view_scroll, this.ScrollScroll);
        //mSongCont = (TextView) findViewById(R.id.songcont);
        
        vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        
		Long rated_me_not = vSettings.getLong("js_vsb_rated_me_not", 0);		
		Long time_used = (System.currentTimeMillis() - rated_me_not) / 1000;
				
		if  (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_rate_me", false)) {
				if (time_used > 18000 ) { 
					rateMePlease();
				}
			} 	
		FontSize = PreferenceManager.getDefaultSharedPreferences(this).getInt("js_vsb_font_size", 15);        
		mUri = getIntent().getData();        
        cursor = managedQuery(mUri, null, null, null, null);
        
        CurrSong = Integer.parseInt(mUri.toString().replaceAll("\\D+", ""));
        localEditor.putInt("js_vsb_curr_song", CurrSong).commit();
        
        //StanzasList.settext setTextSize(FontSize);
        setTextContent(cursor, mUri);

    }

	@SuppressWarnings("unused")
	public void setTextContent(Cursor cursor, Uri uri){
        cursor.moveToFirst();
        if (cursor == null) {
            finish();
        } else {    
            cursor.moveToFirst();
            int wIndex = cursor.getColumnIndexOrThrow(SongBookDatabase.SONG_TITLE);
            int dIndex = cursor.getColumnIndexOrThrow(SongBookDatabase.SONG_CONTENT);
            setTitle(cursor.getString(wIndex));            
            //mSongCont.setText(cursor.getString(dIndex));
        	String[] Stanzas = TextUtils.split(cursor.getString(dIndex), "`");
            CustomSongView adapter = new CustomSongView(SongBookView.this, Stanzas, FontSize);
            StanzasList.setAdapter(adapter);
        }
    }

    public void rateMePlease() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SongBookView.this);
    	builder.setTitle(R.string.just_a_min);
        builder.setMessage(R.string.rate_me_please);
        builder.setNegativeButton(R.string.rate_later, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				localEditor.putBoolean("js_vsb_rate_me", false);
			    localEditor.putLong("js_vsb_rated_me_not", System.currentTimeMillis());
			    localEditor.commit();	
			}
		});
        
        builder.setPositiveButton(R.string.rate_now, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				localEditor.putBoolean("js_vsb_rate_me", true);
			    localEditor.putLong("js_vsb_rated_me_not", System.currentTimeMillis());
			    localEditor.commit();	    
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.jackson_siro.visongbook")));
				Toast.makeText(getApplicationContext(), R.string.blessed, Toast.LENGTH_LONG).show();						
			}
		});
        
        builder.show();
	
	  }
	
	public void Previous (View view){
		int NewSong = CurrSong - 1;
        if (NewSong < 0 || NewSong > 3793) {            
            setTitle(R.string.app_name);
            this.mSongCont.setText(R.string.error_first_song);
            return;
        } else{
        	CurrSong = NewSong;
        	localEditor.putInt("js_vsb_curr_song", CurrSong).commit(); 
        	mUri = Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong));
            //this.ScrollScroll.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_left_in));

            cursor = managedQuery(mUri, null, null, null, null);
            setTextContent(cursor, mUri);
        }           
	}

	public void Next (View view){
		int NewSong = CurrSong + 1;
        if (NewSong < 0 || NewSong > 3793) {            
            setTitle(R.string.app_name);
            this.mSongCont.setText(R.string.error_first_song);
            return;
        } else{
        	CurrSong = NewSong;
        	localEditor.putInt("js_vsb_curr_song", CurrSong).commit(); 
        	mUri = Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong));
            //this.ScrollScroll.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_right_out));

            cursor = managedQuery(mUri, null, null, null, null);
            setTextContent(cursor, mUri);
        }        
	}

	public void Minus (View view){
		FontSize = FontSize - 1;
		if (FontSize > 5) {
			localEditor.putInt("js_vsb_font_size", FontSize).commit();                 
			mUri = Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong));       
			cursor = managedQuery(Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong)), null, null, null, null);
            setTextContent(cursor, mUri);
       }
	}
	
	public void Plus (View view){
		FontSize = FontSize + 1;
		if (FontSize > 5) {
			localEditor.putInt("js_vsb_font_size", FontSize).commit();                 
			mUri = Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong));       
			cursor = managedQuery(Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong)), null, null, null, null);
            setTextContent(cursor, mUri);
       }
	}
    
	public void Share (View view){
		Intent share = new Intent("android.intent.action.SEND");
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        
        Cursor cursor = managedQuery(Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong)), null, null, null, null);
        cursor.moveToFirst();
        
        int wIndex = cursor.getColumnIndexOrThrow(SongBookDatabase.SONG_TITLE);
        int dIndex = cursor.getColumnIndexOrThrow(SongBookDatabase.SONG_CONTENT);
        
        String SongTitle = cursor.getString(wIndex);
        String SongCont = cursor.getString(dIndex);
        
        share.putExtra(Intent.EXTRA_SUBJECT, SongTitle);
        share.putExtra(Intent.EXTRA_TEXT, SongCont + "\n\n Via vSongBook");
        startActivity(Intent.createChooser(share, "Share this Song"));
	}
     @Override
  	public void onBackPressed() {
     	 Intent intent = new Intent(this, CcSongBook.class);
     	 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
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
    	//int qid = Integer.parseInt(FontSize);
        AlertDialog.Builder builder = new AlertDialog.Builder(SongBookView.this);    	
        
        switch (item.getItemId()) {
	        case R.id.search: 
	            onSearchRequested();
	            return true;
            case R.id.favour:
            	if (CurrSong < 0 || CurrSong > 3793) {

                    builder.setTitle(R.string.just_a_min);
                    builder.setMessage(R.string.invalid_song);
                    builder.setNegativeButton(R.string.action_gotit, new DialogInterface.OnClickListener() {					
    					@Override
    					public void onClick(DialogInterface arg0, int arg1) {
    						Toast.makeText(getApplicationContext(), R.string.blessed, Toast.LENGTH_LONG).show();						
    					}
    				});
                    builder.show();
                } else {
                	cursor = managedQuery(Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong)), null, null, null, null);
	                cursor.moveToFirst();
	                
	                int wIndex = cursor.getColumnIndexOrThrow(SongBookDatabase.SONG_TITLE);
	                int dIndex = cursor.getColumnIndexOrThrow(SongBookDatabase.SONG_CONTENT);
	                
	                String SongTitle = cursor.getString(wIndex);
	                String SongCont = (cursor.getString(dIndex)).replace("`", 
	                		System.getProperty("line.separator")+"`"+System.getProperty("line.separator"));
        
	            	db.createFavour(new Favour(SongTitle, SongCont)); 
	        		Toast.makeText(getApplicationContext(), SongTitle + " favourited!", Toast.LENGTH_SHORT).show();
	            }
                return true;
            case R.id.copy:
                if (CurrSong < 0 || CurrSong > 3793) {
                    
                    builder.setTitle(R.string.just_a_min);
                    builder.setMessage(R.string.invalid_song);
                    builder.setNegativeButton(R.string.action_gotit, new DialogInterface.OnClickListener() {					
    					@Override
    					public void onClick(DialogInterface arg0, int arg1) {
    						Toast.makeText(getApplicationContext(), R.string.blessed, Toast.LENGTH_LONG).show();						
    					}
    				});
                    builder.show();
                } else {
                    Intent copyIntent = new Intent(getApplicationContext(), SongBookCopy.class);
                    copyIntent.setData(Uri.withAppendedPath(SongBookProvider.CONTENT_URI, Integer.toString(CurrSong)));
                    startActivity(copyIntent);
                }
                return true;
            
            default:
                return false;
        }
    } 
}
