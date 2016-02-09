package com.jackson_siro.vsongbook;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings_II extends ActionBarActivity{
	private TextView App_Mode;
	private TextView Full_Name;
	private TextView Phone_No; 
	private TextView Church_Name;
	private TextView Installed_on;
	private TextView Expired_on;
	private TextView Paid_on;
	private Button Protest_this;
	private Button Confirm_this;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super .onCreate(savedInstanceState);
		
		setContentView(R.layout.settings_ii);
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String strUserName = vSettings.getString("users_name", "NA");
		String strChurch = vSettings.getString("church_name", "NA");
		String strNumber = vSettings.getString("phone_number", "NA");
		String you_Installed = "a long time ago";
		String Expiration_Date = "a long time ago";
		
		Long first_use = vSettings.getLong("vsb_first_data", 0);
	    Long expiry_data = vSettings.getLong("vsb_expire_data", 0);
	    
	    Long secs_Used = (System.currentTimeMillis() - first_use) / 1000;	    
	    int my_secs = (int)(long)secs_Used;
	    
	    if (my_secs < 60) you_Installed = my_secs +" seconds ago";
	    else if (my_secs < 3600) {
	    	you_Installed = my_secs / 60 +" minutes ago";
	    }
	    else if (my_secs < 86400) {
	    	you_Installed = my_secs / 3600 +" hours ago";
	    }
	    else if (my_secs < 604800) {
	    	you_Installed = my_secs / 86400 +" days ago";
	    }
	    else if (my_secs < 2419200) {
	    	you_Installed = my_secs / 604800 +" weeks ago";
	    }
	    else if (my_secs < 29030400) {
	    	you_Installed = my_secs / 2419200 +" months ago";
	    }
	    else if (my_secs < 145152000) {
	    	you_Installed = my_secs / 29030400 +" years ago";
	    }
	    
	    Long secs_Rem = (expiry_data - System.currentTimeMillis()) / 1000;	    
	    int my_remsecs = (int)(long)secs_Rem;
	    int my_rem_sec = (int)(long)secs_Rem;
	    
	    if (my_remsecs < 0) {
	    	Expiration_Date = "This Trial Mode of vSongBook expires is expired!";
	    }
	    if (my_remsecs < 60) {
	    	Expiration_Date = "This Trial Mode of vSongBook expires " + 
	    			my_remsecs +" seconds from now!";
	    }
	    if (my_remsecs < 3601) {
	    	Expiration_Date = "This Trial Mode of vSongBook expires " + 
	    			my_remsecs /60 +" minutes from now!";
	    }
	    if (my_secs < 86401) {
	    	Expiration_Date = "This Trial Mode of vSongBook expires " + 
	    			my_remsecs /3600 +" hours from now!";
	    }
	    if (my_remsecs < 604801) {
	    	Expiration_Date = "This Trial Mode of vSongBook expires " + 
	    			my_remsecs /86400 +" days from now!";
	    }
	     
	    App_Mode = (TextView) findViewById(R.id.app_mode);
	    Full_Name = (TextView) findViewById(R.id.fullname);
	    Phone_No = (TextView) findViewById(R.id.phonenumber);
	    Church_Name = (TextView) findViewById(R.id.church);
	    Installed_on = (TextView) findViewById(R.id.installed_on);
	    Expired_on = (TextView) findViewById(R.id.expired_on);
	    Paid_on = (TextView) findViewById(R.id.paid_on);
	    Protest_this = (Button) findViewById(R.id.btnProtest);
		Confirm_this = (Button) findViewById(R.id.btnConfrim);
		
		Full_Name.setText(strUserName);
		Phone_No.setText("Phone: " + strNumber);
		Church_Name.setText("Church: " + strChurch);
		Installed_on.setText("Installed: " + you_Installed);
		
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("vsb_is_paid", true))
		{
			setTitle("App Mode :: Premium Mode");
			App_Mode.setText("PREMIUM MODE");		
			Expired_on.setVisibility(View.GONE);
			Protest_this.setVisibility(View.GONE);
			Confirm_this.setVisibility(View.GONE);
			Paid_on.setVisibility(View.GONE);	
		}
		else { 
			setTitle("App Mode :: Trial Mode");
			App_Mode.setText("TRIAL MODE");		 		
			Expired_on.setText(Expiration_Date);
			Protest_this.setVisibility(View.GONE);
			Confirm_this.setVisibility(View.GONE);
			Paid_on.setVisibility(View.GONE);
		}
		}
	
}
