package com.jackson_siro.visongbook.songbooks;

import com.jackson_siro.visongbook.*;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

@SuppressWarnings("deprecation")
public class SongBookCopy extends ActionBarActivity {
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String FONT_SIZE = "font_size";
	public static final String CURRENT_SONG = "15";
    
	WebView mSongCopy;
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.song_copy);
        
        SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 15);
        String font_size = settings.getString(FONT_SIZE, "15");
        
        mSongCopy = (WebView)findViewById( R.id.copyview );
        Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);
        cursor.moveToFirst();
        int wIndex = cursor.getColumnIndexOrThrow(SongBookDatabase.SONG_TITLE);
        int dIndex = cursor.getColumnIndexOrThrow(SongBookDatabase.SONG_CONTENT);
        setTitle(cursor.getString(wIndex));            
        int fontsize = Integer.parseInt(font_size);
        String ContText = (cursor.getString(dIndex)).replace(System.getProperty("line.separator"), "<br>");
        String SiteHeader = "<!DOCTYPE html><head><title>vSongBook On a Android</title><style>html{font-size:" + fontsize + "px;}</style></head><body><p>";            
        String songtocopy = SiteHeader + ContText.replace("`", "<br>`<br>") + "<br></p></body></html>";            
        mSongCopy.loadData(songtocopy, "text/html", "UTF-8");
    }
     
}
