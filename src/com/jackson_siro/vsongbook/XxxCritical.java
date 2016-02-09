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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class XxxCritical extends Activity {
	RelativeLayout MySong;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.critical);		
		getWindow().setBackgroundDrawable(new ClippedDrawable(getWallpaper()));	
		
	}
	
	public void onProtest(View v) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(XxxCritical.this);
		builder.setTitle("Why pay for vSongBook");
		builder.setMessage("Developing this app has not been easy. It came with a number"
				+ " of challenegs from the ones of researching for codes, combining designs"
				+ " and development, preparing the songs into songbooks. \n\nFinancial "
				+ "constaraints are the worst obstacles we have faced and with your little"
				+ " support of Ksh. 200 we will overcome it. We need to clear some bills,"
				+ " impove the user interface, acquire our own playstore account, acquire"
				+ "a Windows Phone Store account among many other things");
		builder.setNeutralButton("Okay Got It", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		});
		
		builder.show();
		
	}
	
	public void onExit(View v) {
		finish();
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
