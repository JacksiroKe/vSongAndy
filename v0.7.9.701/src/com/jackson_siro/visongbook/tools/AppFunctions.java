package com.jackson_siro.visongbook.tools;

import com.jackson_siro.visongbook.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class AppFunctions extends ActionBarActivity {

	JSONParser jsonParser = new JSONParser();
	SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
	public static final String TAG_SUCCESS = "success", TAG_USER = "user", TAG_USERID = "userid", TAG_USERNAME = "user_name", TAG_FNAME = "user_fname", TAG_LNAME = "user_lname", TAG_EMAIL = "user_email", TAG_LEVEL = "user_level", TAG_MOBILE = "user_mobile", TAG_JOINED = "user_joined", TAG_HASPAID = "user_haspaid", TAG_AMOUNT = "user_amountpaid", TAG_DEVICE = "user_device", TAG_SEX = "user_sex", TAG_COUNTRY = "user_country", TAG_CHURCH = "user_church", TAG_CITY = "user_city", TAG_ABOUT = "user_about", TAG_SCOUNT = "user_scount", TAG_AVATAR = "user_avatar";
	
	public void checkUserStatus(String mobile){
		if (isInternetOn()) {
	        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_is_paid", false)) {
	        	UserProfileTask myTask = new UserProfileTask();
	            myTask.execute("haspaid", mobile);
	  		}
        }        
	}

    @SuppressWarnings("static-access")
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
    
    @SuppressLint("NewApi")
	public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.orange));
        }
    }
    
    public void setLoggedIn(){
    	SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    	localEditor.putBoolean("as_vsb_logged_in_user", true);
	    localEditor.commit();
	}
    
    public void errorDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
        builder.setTitle(R.string.just_a_min);
        builder.setMessage(R.string.invalid_song);
        builder.setNegativeButton(R.string.action_gotit, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Toast.makeText(getApplicationContext(), R.string.blessed, Toast.LENGTH_LONG).show();						
			}
		});
        builder.show();
    }

    @SuppressWarnings("unused")
	public void HaveYouPaidMe() {
    	SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
    	long used_time_l, rem_time_l, expires_l;
        int used_time_i, rem_time_i, expired_i;
        String expiry_txt = "from now";

        used_time_l = System.currentTimeMillis() - vSettings.getLong("as_vsb_first_data", 0);
        rem_time_l = vSettings.getLong("as_vsb_expire_data", 0) - System.currentTimeMillis();
        expires_l = System.currentTimeMillis() - vSettings.getLong("as_vsb_expire_data", 0);

        used_time_i = (int)(used_time_l / 1000);
        rem_time_i = (int)(rem_time_l / 1000);
        expired_i = (int)(expires_l / 1000);

        if (rem_time_i < 60) expiry_txt = rem_time_i + " seconds from Now";
        else if (rem_time_i < 3600) expiry_txt = rem_time_i / 60 + " minutes from now";
        else if (rem_time_i < 86400) expiry_txt = rem_time_i / 3600 + " hours from now";
        else if (rem_time_i < 440000) expiry_txt = rem_time_i / 86400 + " days from now";
        YouMustJustPay(expiry_txt);
    }

    public void InternetNeeded() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.just_a_min);        
        builder.setMessage(R.string.cant_use_now);
    	builder.setPositiveButton(R.string.okay_got_it, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//YouRatedMeYes();
			}
		});
        
        builder.show();
	
	  }

    public void YouMustJustPay(String rem_time) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.just_a_min);        
        builder.setMessage(getString(R.string.expiry_warning, rem_time));
    	builder.setPositiveButton(R.string.okay_got_it, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//YouRatedMeYes();
				
			}
		});
        
        builder.show();
	
	  }
    
    @SuppressWarnings("unused")
	public void RateMePlease() {
    	SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	long used_time_l, rate_time_l;
        int used_time_i, rate_time_i;

        used_time_l = System.currentTimeMillis() - vSettings.getLong("as_vsb_first_data", 0);
        rate_time_l = vSettings.getLong("as_vsb_rated_me_not", 0) - System.currentTimeMillis();
         
        used_time_i = (int)(used_time_l / 1000);
        rate_time_i = (int)(rate_time_l / 1000);
        if (used_time_i > 18000)
        {            
        	 builder.setTitle(R.string.just_a_min);
        	 builder.setMessage(R.string.rate_me_please);
		     
        	 builder.setNegativeButton(R.string.rate_later, new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					YouRatedMeNot();	
				}
			});
		        
	        builder.setPositiveButton(R.string.rate_now, new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					YouRatedMeYes();
					Toast.makeText(getApplicationContext(), R.string.blessed, Toast.LENGTH_LONG).show();						
				}
			});
	        
	        builder.show();
         }
	  }
	
	public void YouRatedMeNot(){
		SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		localEditor.putBoolean("as_vsb_rate_me", false);
	    localEditor.putLong("as_vsb_rated_me_not", System.currentTimeMillis());
	    localEditor.commit();
     }
	
	public void YouRatedMeYes(){
		SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		localEditor.putBoolean("as_vsb_rate_me", true);
	    localEditor.putLong("as_vsb_rated_me_not", System.currentTimeMillis());
	    localEditor.commit();	    
	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.jackson_siro.visongbook")));
     }
	
    public void HowToPayMessage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.upgrade_to_premium);
        builder.setMessage(R.string.upgrade_explanation);
        builder.setPositiveButton(R.string.okay_got_it, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		});
        
        builder.show();
	
	  }
    
    public void QuickActivate(String vsbcode){
    	if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_is_paid", false))
        {            
    		Calendar rightNow = Calendar.getInstance();
    	    int CurHour = rightNow.get(Calendar.HOUR_OF_DAY);    	    
    		if (vsbcode.contains("JCSR")) {
                String[] CodeHr = vsbcode.split("JCSR");
                int CdHour = Integer.parseInt(CodeHr[1]);
                if ((CurHour - CdHour) < 3) ApprovalTask(true);
            } else if (vsbcode.contains("jcsr")) {
                String[] CodeHr = vsbcode.split("jcsr");
                int CdHour = Integer.parseInt(CodeHr[1]);
                if ((CurHour - CdHour) < 3) ApprovalTask(true);
            }
        }
    }
    
	public void ActivateVsongbook(String vsbcode) {
		Calendar rightNow = Calendar.getInstance();
	    int CurHour = rightNow.get(Calendar.HOUR_OF_DAY);	    
        if (vsbcode.contains("JCSR")) {
            String[] CodeHr = vsbcode.split("JCSR");
            int CdHour = Integer.parseInt(CodeHr[1]);
            
            if ((CurHour - CdHour) < 3) {
            	ApprovalTask(true);
            	PaymentValidation(1);
            } else PaymentValidation(2);
        } else if (vsbcode.contains("jcsr")) {
            String[] CodeHr = vsbcode.split("jcsr");
            int CdHour = Integer.parseInt(CodeHr[1]);
            
            if ((CurHour - CdHour) < 3) {
            	ApprovalTask(true);
            	PaymentValidation(1);
            } else PaymentValidation(2);
        } else PaymentValidation(3);
    }

    public void ApprovalTask(boolean enableapp){
    	SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		if (enableapp) {
			localEditor.putBoolean("as_vsb_is_paid", true);
	    	localEditor.putBoolean("as_vsb_update_online", true);
		}
    	else {
    		localEditor.putBoolean("as_vsb_is_paid", false);
	    	localEditor.putBoolean("as_vsb_update_online", false);
	    }
		localEditor.commit();
    }
  
    public void PaymentValidation(int showthis) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	if (showthis==1) {
    		builder.setTitle(R.string.app_unlocked);
    		builder.setMessage(R.string.app_unlocked_explanation);
    		builder.setPositiveButton(R.string.okay_got_it, new DialogInterface.OnClickListener() {					
    			@Override
    			public void onClick(DialogInterface arg0, int arg1) {
    					
    			}
    		});
    	}
    	else if (showthis==2) {
    		builder.setTitle(R.string.expired_code);
    		builder.setMessage(R.string.expired_code_explanation);
    		builder.setPositiveButton(R.string.okay_got_it, new DialogInterface.OnClickListener() {					
    			@Override
    			public void onClick(DialogInterface arg0, int arg1) {
    								
    			}
    		});
    	}
    	else if (showthis==3) {
    		builder.setTitle(R.string.invalid_code);
    		builder.setMessage(R.string.invalid_code_explanation);
    		builder.setPositiveButton(R.string.okay_got_it, new DialogInterface.OnClickListener() {					
    			@Override
    			public void onClick(DialogInterface arg0, int arg1) {
    								
    			}
    		});
    	}            	
        builder.show();	
	 }

    public void ResetThisApp() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.reset_vsongbook);
		builder.setMessage(R.string.reset_vsongbook_explanation);
		builder.setPositiveButton(R.string.reset_everything, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ResetAccount();
				ResetAppData();
				finish();		
				startActivity(new Intent(getApplicationContext(), AppStart.class));
			}
		});
		
		builder.setNegativeButton(R.string.reset_userdata, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ResetAccount();
				finish();		
				startActivity(new Intent(getApplicationContext(), AppStart.class));
			}
		});
		
		builder.setNeutralButton(R.string.action_cancel, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
					
			}
		});
        builder.show();	
	 }
    
    public void ResetAccount(){
    	SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
    	sbDB.updateOption("userid", ""); 
		sbDB.updateOption("user_name", "");
		sbDB.updateOption("user_fname", ""); 
		sbDB.updateOption("user_lname", ""); 
		sbDB.updateOption("user_email", ""); 
		sbDB.updateOption("user_level", ""); 
		sbDB.updateOption("user_mobile", ""); 
		sbDB.updateOption("user_church", ""); 
		sbDB.updateOption("user_country", ""); 
		sbDB.updateOption("user_haspaid", ""); 
		sbDB.updateOption("user_joined", "");
		sbDB.updateOption("user_haspaid", "");
		sbDB.updateOption("user_amountpaid", "");
		sbDB.updateOption("user_device", ""); 
		sbDB.updateOption("user_sex", ""); 
		sbDB.updateOption("user_about", "");  
		sbDB.updateOption("user_scount", "");  
		sbDB.updateOption("user_avatar", "");
		
		SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    	localEditor.putBoolean("as_vsb_logged_in_user", false);
    	localEditor.putBoolean("as_vsb_is_paid", false);
		localEditor.putString("as_vsb_mobile_phone", "1234567890");
	    localEditor.commit();
    }
    
    public void ResetAppData(){
    	SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
    	sbDB.deleteSongbooks();
    	sbDB.deleteMainSongs();
    	sbDB.deleteMySongs();
    	
    	SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    	localEditor.putBoolean("as_vsb_logged_in_user", false);
    	localEditor.putBoolean("as_vsb_first_use", false);
    	localEditor.putBoolean("as_vsb_show_tutorial", false);
    	localEditor.putBoolean("as_vsb_logged_in_user", false);
    	localEditor.putBoolean("as_vsb_finished_loading", false);
	    localEditor.putLong("as_vsb_first_data", 0);
	    localEditor.putLong("as_vsb_expire_data", 0);

    	
	    localEditor.commit();
    }

	public class UserProfileDownload extends AsyncTask<String, Void, Integer> {
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
    	String siteurl = vSettings.getString("as_vsb_siteurl", "NA") + "as_mobile/as_users_profile.php";
    	String userid = vSettings.getString("as_vsb_userid", "NA");
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		protected Integer doInBackground(String ...args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("download", userid));
			
			JSONObject json = jsonParser.makeHttpRequest(siteurl, "GET", params);
			Log.d("Download, Download", json.toString());
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
					sbDB.updateOption("user_about", user.getString(TAG_ABOUT));  
					sbDB.updateOption("user_scount", user.getString(TAG_SCOUNT));  
					sbDB.updateOption("user_avatar", user.getString(TAG_AVATAR));
					
					int paidstats = user.getInt(TAG_HASPAID);
					if (paidstats == 1) ApprovalTask(true);
					else ApprovalTask(false);
					Log.d("Downloading profile", "Successful");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public class UserProfileUpload extends AsyncTask<String, Void, Integer> {
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
    	
		String siteurl = vSettings.getString("as_vsb_siteurl", "NA") + "as_mobile/as_users_profile.php";
    	String userid = vSettings.getString("as_vsb_userid", "NA");
		
		String firstname 	= sbDB.getOption("user_fname");
		String lastname 	= sbDB.getOption("user_lname");
		String country 		= sbDB.getOption("user_country");
		String city 		= sbDB.getOption("user_city");
		String church 		= sbDB.getOption("user_church");
		String mobile 		= sbDB.getOption("user_mobile");
		String haspaid 		= sbDB.getOption("user_haspaid");
		String sex 			= sbDB.getOption("user_sex"); 
		String about 		= sbDB.getOption("user_about");
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		protected Integer doInBackground(String ...args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uploadload", userid));
			params.add(new BasicNameValuePair("mobile", mobile));
			params.add(new BasicNameValuePair("firstname", firstname));
			params.add(new BasicNameValuePair("lastname", lastname));
			params.add(new BasicNameValuePair("country", country));
			params.add(new BasicNameValuePair("city", city));
			params.add(new BasicNameValuePair("church", church));
			params.add(new BasicNameValuePair("haspaid", haspaid));
			params.add(new BasicNameValuePair("sex", sex));
			
			JSONObject json = jsonParser.makeHttpRequest(siteurl, "GET", params);
			Log.d("Upload, Upload", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					localEditor.putBoolean("as_vsb_update_online", false);
				    localEditor.commit();
					Log.d("Uploading profile", "Successful");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	class UserProfileTask extends AsyncTask<String, String, String> {
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		String siteurl = vSettings.getString("as_vsb_siteurl", "NA") + "as_mobile/as_users_profile.php";
		
		protected String doInBackground(String... args) {
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(args[0], args[1]));

			JSONObject json = jsonParser.makeHttpRequest(siteurl, "GET", params);
			Log.d("Action="+args[0], "for "+args[1]);
			Log.d("Checking online: ", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					if (args[0] == "haspaid") ApprovalTask(true);
					else if (args[0] == "disable") ApprovalTask(false);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
}
