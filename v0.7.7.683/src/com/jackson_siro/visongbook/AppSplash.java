package com.jackson_siro.visongbook;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppSplash extends Activity {
	private long ms=0;
	private long splashTime=5000;
	private boolean splashActive = true;
	private boolean paused=false;
	RelativeLayout MySong;
	
	private TextView mytext;
	private ImageView myimage;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aa_b);
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        long used_time_l = System.currentTimeMillis() - vSettings.getLong("js_vsb_first_data", 0);
        
        int used_time_i = (int)(used_time_l / 1000);
		
        
		createDirIfNotExits("AppSmata/vSongBook");		
		
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_first_use", false)) {
			Toast.makeText(this, "Welcome to vSongBook", Toast.LENGTH_SHORT).show();
		    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		    localEditor.putBoolean("js_vsb_first_use", true);
		    localEditor.putBoolean("js_vsb_is_paid", false);
		    localEditor.putLong("js_vsb_first_data", System.currentTimeMillis());
		    localEditor.putLong("js_vsb_expire_data", System.currentTimeMillis() + 440000000);
		    
		    localEditor.commit();
		}
		
	    if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_finished_loading", false)) 
	    {
	    	startActivity(new Intent(AppSplash.this, AppStart.class));
			finish();
			
		 } 
	     else  
		 {
			if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_is_paid", false))
			{
				if (used_time_i > 440000) {
					startActivity(new Intent(this, XxxCritical.class));
					finish(); 
				} 
				else 
				{
					startActivity(new Intent(this, CcSongBook.class));
					finish();
				}
			}
			else 
			{
				startActivity(new Intent(this, CcSongBook.class));
				finish();
			}
		 } 
		
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
		
	
}