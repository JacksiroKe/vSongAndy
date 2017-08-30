package com.jackson_siro.visongbook;

import com.jackson_siro.visongbook.tools.*;
//import com.jackson_siro.visongbook.tools.AppTimeAgo.timeAgo;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Settings extends AppFunctions {

	SongBookSQLite db = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
	public static final String VSB_SETTINGS = "vSONG_BOOKs", FONT_SIZE = "font_size", SET_THEME = "set_theme";
	
	TextView mFullName, mMobile, mUserSince, mChurch, mCountry, mPreview, mAppMode, mInstalled;
	ImageView mUserIcon;
	Bitmap bitmap;
	private SeekBar mSeekBar;
	//private final String IMG_PATH = "/AppSmata/vSongBook/media/";

    SharedPreferences vSettings;
	SharedPreferences.Editor localEditor;
	int FontSize;
	LinearLayout mEvaluation;
	EditText mActivaText;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();

	    FontSize = PreferenceManager.getDefaultSharedPreferences(this).getInt("as_vsb_font_size", 15);        
		
		mSeekBar = (SeekBar) findViewById(R.id.seekBar1);
		mPreview = (TextView) findViewById(R.id.fontsize);	
		mFullName = (TextView) findViewById(R.id.full_name);
		mMobile = (TextView) findViewById(R.id.mobile_phone);
		mUserSince = (TextView) findViewById(R.id.user_since);
		mChurch = (TextView) findViewById(R.id.church);
		mCountry = (TextView) findViewById(R.id.country);
		mInstalled = (TextView) findViewById(R.id.installedon);
		
		mEvaluation = (LinearLayout) findViewById(R.id.evaluationStuff);
		mAppMode = (TextView) findViewById(R.id.application_mode);
        mActivaText = (EditText) findViewById(R.id.activaText);
        
        changeStatusBarColor();
		LoadUserDetails();
		InstalledTime();
		
		mPreview.setText( "Font Size: " + FontSize);
		mSeekBar.setProgress(FontSize);
		
		mSeekBar.setOnSeekBarChangeListener(new	OnSeekBarChangeListener() {	
			int progress = FontSize;
			@Override public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser){ progress = progresValue; }
			@Override public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override	
			public void onStopTrackingTouch(SeekBar	seekBar) {	
				mPreview.setText( "Font Size: " + progress);
				String font_size = Integer.toString(progress);
				SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 25);
			    settings.edit().putString(FONT_SIZE, font_size).commit();
			    localEditor.putInt("as_vsb_font_size", FontSize).commit();   
			}
		});
		
	}

	public void LoadUserDetails(){
		mFullName.setText(db.getOption("user_fname") + " " + db.getOption("user_lname"));
		mMobile.setText("+" + db.getOption("user_mobile"));
		String userSince = db.getOption("user_joined");
		//mUserSince.setText(timeAgo(userSince));
		mUserSince.setText(db.getOption("user_joined"));
		mChurch.setText(db.getOption("user_church")+ " Church");
		mCountry.setText(db.getOption("user_country"));
	}
	
	public void InstalledTime(){

        long used_time_l, rem_time_l, expires_l;
        int used_time_i, rem_time_i, expired_i;

        used_time_l = System.currentTimeMillis() - vSettings.getLong("as_vsb_first_data", 0);
        rem_time_l = vSettings.getLong("as_vsb_expire_data", 0) - System.currentTimeMillis();
        expires_l = System.currentTimeMillis() - vSettings.getLong("as_vsb_expire_data", 0);

        used_time_i = (int)(used_time_l / 1000);
        rem_time_i = (int)(rem_time_l / 1000);
        expired_i = (int)(expires_l / 1000);
        
        if (used_time_i < 60) mInstalled.setText("Installed: " + used_time_i + " seconds ago");
        else if (used_time_i < 3600) mInstalled.setText("Installed: " + used_time_i / 60 + " minutes ago");
        else if (used_time_i < 86400) mInstalled.setText("Installed: " + used_time_i / 3600 + " hours ago");
        else if (used_time_i < 604800) mInstalled.setText("Installed: " + used_time_i / 86400 + " days ago");
        else if (used_time_i < 2419200) mInstalled.setText("Installed: " + used_time_i / 604800 + " weeks ago");
        else if (used_time_i < 29030400) mInstalled.setText("Installed: " + used_time_i / 2419200 + " months ago");
        else if (used_time_i > 29030400) mInstalled.setText("Installed: " + used_time_i / 29030400 + " years ago");
          
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_is_paid", false))
        {
            if (used_time_i < 440000)
            {
                if (rem_time_i < 60) mAppMode.setText("Evaluation Mode, expiring " + rem_time_i + " seconds from Now");
                else if (rem_time_i < 3600) mAppMode.setText("Evaluation Mode, expiring " + rem_time_i / 60 + " minutes from Now");
                else if (rem_time_i < 86400) mAppMode.setText("Evaluation Mode, expiring " + rem_time_i / 3600 + " hours from Now");
                else if (rem_time_i < 440000) mAppMode.setText("Evaluation Mode, expiring " + rem_time_i / 86400 + " days from Now");
            }
            else if (used_time_i > 440000)
            {
                if (expired_i < 60) mAppMode.setText("Evaluation Mode, expired " + expired_i + " seconds ago");
                else if (expired_i < 3600) mAppMode.setText("Evaluation Mode, expired " + expired_i / 60 + " minutes ago");
                else if (expired_i < 86400) mAppMode.setText("Evaluation Mode, expired " + expired_i / 3600 + " hours ago");
                else if (expired_i < 440000) mAppMode.setText("Evaluation Mode, expired " + expired_i / 86400 + " days ago");
            }
        }
        else if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_is_paid", false)) {
        {
            mAppMode.setText("Premuim Mode!");
            mEvaluation.setVisibility(View.GONE);
        }   
      }
	}

    public void ActivateNow(View view) {
    	String vsbCode = mActivaText.getText().toString();
    	ActivateVsongbook(vsbCode);
    	mActivaText.setText("");
    	if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_is_paid", true))
    		mEvaluation.setVisibility(View.GONE);
    }
    
    public void ShowPaymentHint(View view) {
    	HowToPayMessage();
    }
    
    public void ResetApp(View view) {
    	ResetThisApp();
    }
}
