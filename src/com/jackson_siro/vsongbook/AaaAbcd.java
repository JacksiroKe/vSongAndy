package com.jackson_siro.vsongbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class AaaAbcd extends ActionBarActivity {
	RelativeLayout MySong;
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String SHOW_TUTORIAL = "show_tutorial";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_i);
		SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
	    String show_tutorial = settings.getString(SHOW_TUTORIAL, "0");
	    
		getWindow().setBackgroundDrawable(new ClippedDrawable(getWallpaper()));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(AaaAbcd.this);
		builder.setTitle(R.string.just_a_min);
		builder.setMessage(R.string.welcome_note);
		builder.setPositiveButton(R.string.proceed_tutorial, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				SaveTutorialFalse();
				
				Intent intent = new Intent(AaaAbcd.this, AaaDemo.class);
			    startActivity(intent);
			    finish();
			    
				//Toast.makeText(getApplicationContext(), "Ok is clicked", Toast.LENGTH_LONG).show();
				
			}
		});
		
		builder.show(); //To show the AlertDialog
			
	}
	
	public void SaveTutorialFalse()
	  {
	    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    localEditor.putBoolean("show_tutorial", true);
	    localEditor.commit();
	  }	
	
	 private class ClippedDrawable extends Drawable {
	        private final Drawable mWallpaper;

	        public ClippedDrawable(Drawable wallpaper) {
	            mWallpaper = wallpaper;
	        }

	        @Override
	        public void setBounds(int left, int top, int right, int bottom) {
	            super.setBounds(left, top, right, bottom);
	            int theleft = (left - right)/2;
	            mWallpaper.setBounds(            		
	            		theleft, top,            		
	            		theleft + mWallpaper.getIntrinsicWidth(),
	                    top + mWallpaper.getIntrinsicHeight());
	        }

	        public void draw(Canvas canvas) {
	            mWallpaper.draw(canvas);
	        }

	        public void setAlpha(int alpha) {
	            mWallpaper.setAlpha(alpha);
	        }

	        public void setColorFilter(ColorFilter cf) {
	            mWallpaper.setColorFilter(cf);
	        }

	        public int getOpacity() {
	            return mWallpaper.getOpacity();
	        }
	    }
}
