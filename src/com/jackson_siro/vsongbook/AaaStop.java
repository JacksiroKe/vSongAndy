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
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AaaStop extends Activity {
	RelativeLayout MySong;
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String show_tutorial = "show_tutorial";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		getWindow().setBackgroundDrawable(new ClippedDrawable(getWallpaper()));
		AlertDialog.Builder builder = new AlertDialog.Builder(AaaStop.this);
		builder.setTitle("Just a Minute ...");
		builder.setMessage("God bless you for taking time to install this great app on your " +
				"device. This App is a free version and its in Evaluation/Trial Mode and will " +
				"terminate after seven (7) days of using it beginning from the time of " +
				"installation. It will remain locked until you send at least KES. 200 via " +
				"M-Pesa to 0711474787. \n\n OR via AirtelMoney to 0731973180 \n\n Please proceed to the Tutorial To learn more");
		builder.setPositiveButton("View Tutorial", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
				settings.edit().putBoolean("show_tutorial", false).commit();
				
				Intent intent = new Intent(AaaStop.this, AaaDemo.class);
			    startActivity(intent);
			    finish();
			}
		});
		
		builder.show();
		
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
