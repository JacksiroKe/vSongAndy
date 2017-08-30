package com.jackson_siro.visongbook;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jackson_siro.visongbook.tools.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class CcLoading extends AppFunctions {

    public SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
	
	TextView Counting, TitleError, TitleLoad;
	String songbook;
	
	ProgressBar mProgressView;
	RelativeLayout AmLoading;
	
	JSONParser jParser = new JSONParser();
	ArrayList<HashMap<String, String>> songbookList;

	private static final String TAG_SUCCESS = "success", TAG_SONGS = "songs", 
		TAG_BOOK = "song_book", TAG_TITLE = "song_title", 
		TAG_CONTENT = "song_content", TAG_KEY = "song_key", 
		TAG_AUTHOR = "song_author", TAG_POSTED = "song_posted";
		
	JSONArray songitems = null;
	ListView list;
	SongBook selSongbook;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.bb_song_load);
        changeStatusBarColor();
		Counting = (TextView) findViewById(R.id.countThis);		
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    localEditor.putBoolean("as_vsb_finished_loading", true);
	    localEditor.commit();
	    
	    SongbookHandler();
	    //if (isInternetOn()) SongbookHandler();
	    //else NotConnected();
	}
    
	private void SongbookHandler() {
		songbookList = new ArrayList<HashMap<String, String>>();
		new LoadAllSongs().execute();
		
	}

    private void setNewTexts(){
    	setTitle("Now Loading SongBooks");
		Counting.setText("Patience pays");
    }
    
	class LoadAllSongs extends AsyncTask<String, String, String> {		
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		String songbook = vSettings.getString("as_vsb_sbnos", "NA");
		String siteurl = vSettings.getString("as_vsb_siteurl", "NA") + "as_mobile/as_songs_list.php";
		String audiourl = vSettings.getString("as_vsb_siteurl", "NA") + "as_media/";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setNewTexts();
		}
		
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("songbook", songbook));
			JSONObject json = jParser.makeHttpRequest(siteurl, "GET", params);
			
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					songitems = json.getJSONArray(TAG_SONGS);
					Log.d("Reading Songs: ", "executed successfully");
					//Log.d("All songs: ", json.toString());
					
					for (int i = 0; i < songitems.length(); i++) {
						JSONObject c = songitems.getJSONObject(i);

						String s_book = c.getString(TAG_BOOK);
						String s_title = c.getString(TAG_TITLE);
						String s_content = c.getString(TAG_CONTENT);
						String s_key = c.getString(TAG_KEY);
						String s_author = c.getString(TAG_AUTHOR);
						String s_posted = c.getString(TAG_POSTED);
						
						sbDB.createSong(new SongItem(s_book, s_title, s_content, 
								s_key, s_author, s_posted, "", "", "", ""));
						//Log.d("Adding Songs: ", s_title + " was added successfully");
					}
					Log.d("Adding Songs: ", "executed successfully");
				} else Log.d("Reading Songs: ", "unsuccessful");
			} catch (JSONException e) {
				e.printStackTrace();
				OpenMainActivity();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			OpenMainActivity();
		}
	}
	
	private void OpenMainActivity(){
		startActivity(new Intent(this, CcSongBook.class));
	}
	
}
