package com.jackson_siro.visongbook;
import com.jackson_siro.visongbook.tools.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class XxxCritical extends AppFunctions {
	RelativeLayout MySong;
	EditText ActivaText;
	SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
	SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.critical);		
		setTitle("vSongBook has Terminated");
		
		checkUserStatus(vSettings.getString("as_vsb_mobile_phone", "NA"));
		ActivaText = (EditText) findViewById(R.id.activation_code);
        changeStatusBarColor();	
	}

	public void onExit(View v) {
		finish();
	}	

	public void ActivateApp(View view) { 
		ActivateVsongbook(ActivaText.toString());
		ActivaText.setText("");
	}
    
}