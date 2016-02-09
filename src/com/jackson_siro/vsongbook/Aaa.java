package com.jackson_siro.vsongbook;

import java.io.File;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.appcompat.*;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.jackson_siro.vsongbook.*;

public class Aaa extends Activity {
	NotificationCompat.Builder notification;
	PendingIntent pIntent;
	NotificationManager manager;
	Intent resultIntent;
	TaskStackBuilder stackBuilder;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		createDirIfNotExits("AppSmata");		
		setFirstUse();
		checkMyNumber();
		
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		Long first_use = vSettings.getLong("vsb_first_data", 0);		
		Long secs_Used = (System.currentTimeMillis() - first_use) / 1000;
		
		
		 if 
			(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("vsb_is_paid", false)) {
				if (secs_Used > 432000 ) {
					startNotification(1);
					Toast.makeText(this, "Your vSongBook trial has expired", Toast.LENGTH_SHORT).show();
				    Intent intent = new Intent(Aaa.this, XxxCritical.class);
					startActivity(intent);
					finish();
				} 	else if (secs_Used < 432000 ) {
					GetOpeningActivity();
				}
			} 	else 	{
				GetOpeningActivity();
			}
	}
	
	public void checkMyNumber() {
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("vsb_is_paid", false)) {
			SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
			String strNumber = vSettings.getString("phone_number", "NA");
			
			if (strNumber.equals("3571240144")) vsbActivate();
			else if (strNumber.equals("0722935952")) vsbActivate();
			else if (strNumber.equals("0722146746")) vsbActivate();
			else if (strNumber.equals("0723739712")) vsbActivate();
			else if (strNumber.equals("0715879751")) vsbActivate();
			else if (strNumber.equals("0711474787")) vsbActivate();
		} 
	  }
	
	public void vsbActivate(){
		startNotification(2);
		SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    localEditor.putBoolean("vsb_is_paid", true);
	    localEditor.commit();
	    Toast.makeText(this, "Your vSongBook is now Premium!", Toast.LENGTH_SHORT).show();
	    
	}
	
	public void setFirstUse() {
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("vsb_first_use", false)) {
			Toast.makeText(this, "Welcome to vSongBook", Toast.LENGTH_SHORT).show();
		    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		    localEditor.putBoolean("vsb_first_use", true);
		    localEditor.putBoolean("vsb_is_paid", false);
		    localEditor.putLong("vsb_first_data", System.currentTimeMillis());
		    localEditor.putLong("vsb_expire_data", System.currentTimeMillis() + 432000000);
		    localEditor.commit();
		}
		
	  }
	
	public void GetOpeningActivity(){
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("show_animation", false)) {
			Intent intent = new Intent(Aaa.this, AaaAbc.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(Aaa.this, SongSearch.class);
			startActivity(intent);
			finish();
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
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	protected void startNotification(int notify) {
		notification = new NotificationCompat.Builder(Aaa.this);
		notification.setSmallIcon(R.drawable.ic_launcher);
		stackBuilder = TaskStackBuilder.create(Aaa.this);
			
		if (notify==1){
			notification.setContentTitle("vSongBook has Terminated!");
			notification.setContentText("Touch to learn more about this.");
			notification.setTicker("vSongBook trial mode has expired!");
			stackBuilder.addParentStack(XxxCritical.class);
			resultIntent = new Intent(Aaa.this, XxxCritical.class);
		}
		
		if (notify==2){
			notification.setContentTitle("vSongBook is now Premium");
			notification.setContentText("Touch to learn more about this.");
			notification.setTicker("vSongBook has been unlocked!");
			stackBuilder.addParentStack(Settings_II.class);
			resultIntent = new Intent(Aaa.this, Settings_II.class);
		}
		stackBuilder.addNextIntent(resultIntent);
		pIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setContentIntent(pIntent);
		manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		manager.notify(0, notification.build());			
	}
	
}
