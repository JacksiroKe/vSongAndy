package com.jackson_siro.visongbook.songbooks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.tools.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SongEdit extends AppFunctions {
	
	TextView mSongCont;
	Cursor cursor;
	public Uri mUri;
    SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
    
    SharedPreferences vSettings;
	SharedPreferences.Editor localEditor;
		
    String VSB_SETTINGS, FONT_SIZE, CURRENT_SONG;
    public int CurrSong, FontSize;
	SongItem currentSong;
	EditText title, key, content;
	ImageButton DeleteSong, GoOnline;
	String song_posted, song_title, song_key, song_content;
	
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
		currentSong = sbDB.readSong(CurrSong);
        
		setTitle("Editting: " + currentSong.getTitle() + " on vSongBook");
        title.setText(currentSong.getTitle());
        key.setText(currentSong.getKey());
        content.setText(currentSong.getContent().replace("`", "\n\n"));
        
	}

	public void UpdateMainSong() {		
		song_title = title.getText().toString();
		song_key = key.getText().toString();
		song_content = content.getText().toString();		
		sbDB.createMySong(new SongMine(song_title, song_content, song_key, song_posted, "", "M", 2));
		Toast.makeText(getApplicationContext(), "Song: " + song_title + " update pending.", Toast.LENGTH_SHORT).show();
		finish();
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
        inflater.inflate(R.menu.proceed, menu);
        
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.donehere:
            	//UpdateMainSong();
            	Toast.makeText(this, "Feature not available currently!", Toast.LENGTH_SHORT).show();
    			
            	finish();
                return true;
             
            default:
                return false;
        }
	}
/*
	class UserSignupTask extends AsyncTask<String, Void, Integer>  {
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		String siteurl = vSettings.getString("siteurl", "NA") + "as_mobile/as_users_signup.php";
		
		String firstname = mFirstnameView.getText().toString();
		String lastname = mLastnameView.getText().toString();
		String country = mCountryView.getText().toString();
		String city = mCityView.getText().toString();
		String church = mChurchView.getText().toString();
		String mobile = mMobileView.getText().toString();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		protected Integer doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mobile", mobile));
			params.add(new BasicNameValuePair("firstname", firstname));
			params.add(new BasicNameValuePair("lastname", lastname));
			params.add(new BasicNameValuePair("country", country));
			params.add(new BasicNameValuePair("city", city));
			params.add(new BasicNameValuePair("church", church));
			params.add(new BasicNameValuePair("haspaid", "0"));
			params.add(new BasicNameValuePair("sex", "1"));
			
			JSONObject json = jsonParser.makeHttpRequest(siteurl, "POST", params);

			Log.d("Signing in", json.toString());
			int mRegisterSuccess = 0;
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					JSONArray userObj = json.getJSONArray(TAG_USER); 
					JSONObject user = userObj.getJSONObject(0);
										
					sbDB.updateOption("userid", user.getString(TAG_USERID)); 
					sbDB.updateOption("user_name", user.getString(TAG_USERNAME));
					sbDB.updateOption("user_fname", user.getString(TAG_FNAME)); 
					sbDB.updateOption("user_lname", user.getString(TAG_LNAME)); 
					sbDB.updateOption("user_email", user.getString(TAG_EMAIL)); 
					sbDB.updateOption("user_level", user.getString(TAG_LEVEL)); 
					sbDB.updateOption("user_mobile", user.getString(TAG_MOBILE)); 
					sbDB.updateOption("user_church", user.getString(TAG_CHURCH)); 
					sbDB.updateOption("user_country", user.getString(TAG_COUNTRY)); 
					sbDB.updateOption("user_haspaid", user.getString(TAG_HASPAID)); 
					sbDB.updateOption("user_joined", user.getString(TAG_JOINED));
					sbDB.updateOption("user_haspaid", user.getString(TAG_HASPAID));
					sbDB.updateOption("user_amountpaid", user.getString(TAG_AMOUNT));
					sbDB.updateOption("user_device", user.getString(TAG_DEVICE)); 
					sbDB.updateOption("user_sex", user.getString(TAG_SEX)); 
					sbDB.updateOption("user_country", user.getString(TAG_COUNTRY));
					sbDB.updateOption("user_church", user.getString(TAG_CHURCH)); 
					sbDB.updateOption("user_about", user.getString(TAG_ABOUT));  
					sbDB.updateOption("user_scount", user.getString(TAG_SCOUNT));  
					sbDB.updateOption("user_avatar", user.getString(TAG_AVATAR)); 
					setLoggedIn();
					mRegisterSuccess = 2;
				} else mRegisterSuccess = 1;
			} catch (JSONException e) {
				e.printStackTrace();
				mRegisterSuccess = 0;
			}
			return mRegisterSuccess;
		}

		protected void onPostExecute(Integer mLoginSuccess) {
			showSignUpProgress(false);
			if (mLoginSuccess == 0) ErrorShowNow();
			else if (mLoginSuccess == 1) couldNotValidate();
			else if (mLoginSuccess == 2) nextActivity(mobile);
		}
	}*/
}
