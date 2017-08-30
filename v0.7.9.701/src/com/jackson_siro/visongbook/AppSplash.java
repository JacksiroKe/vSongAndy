package com.jackson_siro.visongbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.jackson_siro.visongbook.tools.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppSplash extends Activity {
	private long ms=0,splashTime=5000;
	private boolean splashActive = true, paused=false;
	RelativeLayout MySong;
	JSONParser jsonParser = new JSONParser();
	
	private TextView mytext;
	private ImageView myimage;
	SharedPreferences vSettings;
	SharedPreferences.Editor localEditor;
	SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aa_a);
		vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        checkFirstTime();
        checkChangeOver();
        
		createDirIfNotExits("AppSmata/vSongBook/Audio");
		createDirIfNotExits("AppSmata/vSongBook/Text");		
		
		mytext = (TextView) findViewById(R.id.text);
	    myimage = (ImageView) findViewById(R.id.image);
	      
	    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
	    myimage.startAnimation(animation1);
	  
	    Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade1);
	    mytext.startAnimation(animation2);
	    	    
	    Thread mythread = new Thread() {
			public void run() {
				try {
					while (splashActive && ms < splashTime) {
						if(!paused) ms=ms+100;
						sleep(100);
					}
				} catch(Exception e) {}
				finally {
					GetNextActivity();
				}
			}
		};
		mythread.start();			    
	}
	
	public void GetNextActivity(){
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_show_tutorial", false)){
			startActivity(new Intent(AppSplash.this, AppStart.class));
		}	else {
			if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_logged_in_user", false))
			{
				startActivity(new Intent(this, BbLogin.class));
			} else {
				long used_time_l = System.currentTimeMillis() - vSettings.getLong("as_vsb_first_data", 0);
			    int used_time_i = (int)(used_time_l / 1000);
				if ((!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_is_paid", false)) 
						&& (used_time_i > 440000)) {
					 startActivity(new Intent(this, XxxCritical.class));
				} else {
					if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_finished_loading", false))
					{
						startActivity(new Intent(this, CcFirstLoad.class));
					} else {
						startActivity(new Intent(this, CcSongBook.class));
					}
				}
			}
		}	
		finish();
	}

    @SuppressWarnings({ "static-access", "deprecation" })
	public final boolean isInternetOn() {    	
		ConnectivityManager connec =  (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);		
			if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
			     connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
			     connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
			     connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
				return true;			    
			} else if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
				return false;
			}
		  return false;
	}
    
	@SuppressWarnings("unused")
	private boolean isExternalStoragepresent(){
		
		boolean mExternalStorageAvailable= false;
		boolean mExternalStorageWritable= false;
		
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)){
			mExternalStorageAvailable = mExternalStorageWritable = true; 
			
		} else {
			if(!((mExternalStorageAvailable) && (mExternalStorageWritable))){
			} return (mExternalStorageAvailable) && (mExternalStorageWritable);
		}
		return mExternalStorageWritable;
		
	}
	
	public static boolean createDirIfNotExits (String path){		
		boolean ret =true;
		File file = new File(Environment.getExternalStorageDirectory(),path);
		if (!file.exists()){
			if (!file.mkdirs()){
				Log.e("AppSmata::", "Problem Creating AppSmata Folder");
				ret = false;
			}
		}
		return ret;
	}
	
	public void checkFirstTime(){
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_first_use", false)) {
			Toast.makeText(this, "Welcome to vSongBook", Toast.LENGTH_SHORT).show();
			
			sbDB.createOption(new MyOption("userid", "null", "null")); 
			sbDB.createOption(new MyOption("user_name", "null", "null")); 
			sbDB.createOption(new MyOption("user_fname", "null", "null")); 
			sbDB.createOption(new MyOption("user_lname", "null", "null")); 
			sbDB.createOption(new MyOption("user_email", "null", "null")); 
			sbDB.createOption(new MyOption("user_level", "null", "null")); 
			sbDB.createOption(new MyOption("user_mobile", "null", "null")); 
			sbDB.createOption(new MyOption("user_joined", "null", "null"));  
			sbDB.createOption(new MyOption("user_haspaid", "null", "null"));   
			sbDB.createOption(new MyOption("user_amountpaid", "null", "null"));
			sbDB.createOption(new MyOption("user_device", "null", "null")); 
			sbDB.createOption(new MyOption("user_sex", "null", "null")); 
			sbDB.createOption(new MyOption("user_country", "null", "null"));
			sbDB.createOption(new MyOption("user_church", "null", "null")); 
			sbDB.createOption(new MyOption("user_about", "null", "null"));  
			sbDB.createOption(new MyOption("user_scount", "null", "null"));  
			sbDB.createOption(new MyOption("user_avatar", "null", "null"));  
			
			localEditor.putBoolean("as_vsb_first_use", true);
		    localEditor.putBoolean("as_vsb_is_paid", false);
		    localEditor.putLong("as_vsb_first_data", System.currentTimeMillis());
		    localEditor.putLong("as_vsb_expire_data", System.currentTimeMillis() + 440000000);

		    //localEditor.putString("as_vsb_siteurl", "http://192.168.43.214/vsongbook/");			
		    localEditor.putString("as_vsb_siteurl", "http://vsongbook.appsmata.com/");	    
		    localEditor.commit();
		} else {
			if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_is_paid", false)) {
	        	if (isInternetOn())new UserProfileTask().execute();
	  		}
		}
	}
	
	public void boolChangeOver(String oldbool, String newbool){
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(oldbool, false)) {
			localEditor.putBoolean(newbool, false);			
		} else {
			localEditor.putBoolean(newbool, true);
		}
		localEditor.commit();
	}

	public void checkChangeOver(){
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_change_over", false)) {
			localEditor.putBoolean("as_vsb_change_over", false);
		    localEditor.commit();
		} else {
			if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_is_paid", true)) {
				localEditor.putBoolean("as_vsb_is_paid", true);			
			}
			long first_data = vSettings.getLong("js_vsb_first_data", 0);
			long expire_data = vSettings.getLong("js_vsb_first_data", 0); 
			localEditor.putString("as_vsb_mobile_phone", "1234567890");
			localEditor.putLong("as_vsb_first_data", first_data);
		    localEditor.putLong("as_vsb_expire_data", expire_data);
		    localEditor.putBoolean("as_vsb_change_over", true);
		    localEditor.commit();
		}		
	}

	class UserProfileTask extends AsyncTask<String, String, String> {
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(AppSplash.this).edit();
		
		String siteurl = vSettings.getString("as_vsb_siteurl", "NA") + "as_mobile/as_users_profile.php";
		String mobile_phone = vSettings.getString("as_vsb_mobile_phone", "NA");
		
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("haspaid", mobile_phone));
			try {
				JSONObject json = jsonParser.makeHttpRequest(siteurl, "GET", params);
				Log.d("Signing up", json.toString());
				
				if (json.getInt("success") == 1) {
					sbDB.updateOption("user_haspaid", "1");
					localEditor.putBoolean("as_vsb_is_paid", true);
					localEditor.commit();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}