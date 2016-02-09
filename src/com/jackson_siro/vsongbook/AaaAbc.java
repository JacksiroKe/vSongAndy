package com.jackson_siro.vsongbook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AaaAbc extends Activity {
	private long ms=0;
	private long splashTime=5000;
	private boolean splashActive = true;
	private boolean paused=false;
	RelativeLayout MySong;
	
	private ImageView finger;
	private ImageView left;
	private ImageView right;
	private ImageView music;
	private ImageView splash;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
	    
		getWindow().setBackgroundDrawable(new ClippedDrawable(getWallpaper()));
		finger = (ImageView) findViewById(R.id.finger);
	  	left = (ImageView) findViewById(R.id.left); 
	  	right = (ImageView) findViewById(R.id.right);
	  	music = (ImageView) findViewById(R.id.music);
	  	splash = (ImageView) findViewById(R.id.splash);	
  		
	  	Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_left);
  		left.startAnimation(animation1);
		  
  		Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_right);
  		right.startAnimation(animation2);
		  
  		Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
  		finger.startAnimation(animation3);
		
  		Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_down);
  		music.startAnimation(animation4);
  		
  		Animation animation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
  		splash.startAnimation(animation5); 
  		  		
  		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("show_tutorial", false))
	     {
  			Thread mythread = new Thread() {
  				public void run() {
  					try {
  						while (splashActive && ms < splashTime) {
  							if(!paused)
  								ms=ms+100;
  							sleep(100);
  						}
  					} catch(Exception e) {}
  					finally {
  						Intent intent = new Intent(AaaAbc.this, AaaAbcd.class);
  						startActivity(intent);
  						finish();  						 
  					}
  				}
  			};
  			mythread.start();
  			
		 } else  {
			 Thread mythread = new Thread() {
	  				public void run() {
	  					try {
	  						while (splashActive && ms < splashTime) {
	  							if(!paused)
	  								ms=ms+100;
	  							sleep(100);
	  						}
	  					} catch(Exception e) {}
	  					finally {
	  						Intent intent = new Intent(AaaAbc.this, SongSearch.class);
	  						startActivity(intent);
	  						finish();  						 
	  					}
	  				}
	  			};
	  			mythread.start();  			
		 } 
		
		
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
