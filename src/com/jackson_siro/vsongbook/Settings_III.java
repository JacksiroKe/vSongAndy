package com.jackson_siro.vsongbook;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Settings_III extends Activity{
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String SHOW_SPLASH = "show_splash";	
	public static final String SHOW_TUTORIAL = "show_tutorial";

	public static final String BELIEVERS_SONGS = "believers_songs";
	public static final String TENZI_ZA_ROHONI = "tenzi_za_rohoni";
	public static final String CIA_KUINIRA = "cia_kuinira";
	public static final String MY_OWN_SONGS = "my_own_songs";
	
	private CheckBox animation;
	private CheckBox tutorial;
	private CheckBox believers;
	private CheckBox zarohoni;
	private CheckBox ciakuinira;
	private CheckBox ownsongs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
	super .onCreate(savedInstanceState);
	setContentView(R.layout.settings);
	SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
    String show_splash = settings.getString(SHOW_SPLASH, "0");    
    String show_tutorial = settings.getString(SHOW_TUTORIAL, "0");

    String believers_songs = settings.getString(BELIEVERS_SONGS, "0");
    String tenzi_za_rohoni = settings.getString(TENZI_ZA_ROHONI, "0");
    String cia_kuinira = settings.getString(CIA_KUINIRA, "0");
    String my_own_songs = settings.getString(MY_OWN_SONGS, "0");
    
    animation = (CheckBox) findViewById(R.id.checkBox1);
    tutorial = (CheckBox) findViewById(R.id.checkBox2);
	believers = (CheckBox) findViewById(R.id.checkBox3);
	zarohoni = (CheckBox) findViewById(R.id.checkBox4);
	ciakuinira = (CheckBox) findViewById(R.id.checkBox5);
	ownsongs = (CheckBox) findViewById(R.id.checkBox6);	
	
	if (show_splash == "1") animation.setChecked(true);
	else if (show_splash == "0") animation.setChecked(false);
	
	if (show_tutorial == "1") tutorial.setChecked(true);
	else if (show_tutorial == "0") tutorial.setChecked(false);
	
	if (believers_songs == "1") believers.setChecked(true);
	else if (believers_songs == "0") believers.setChecked(false);

	if (tenzi_za_rohoni == "1") zarohoni.setChecked(true);
	else if (tenzi_za_rohoni == "0") zarohoni.setChecked(false);
	
	if (cia_kuinira == "1") ciakuinira.setChecked(true);
	else if (cia_kuinira == "0") ciakuinira.setChecked(false);
	
	if (my_own_songs == "1") ownsongs.setChecked(true);
	else if (my_own_songs == "0") ownsongs.setChecked(false);
	
	animation.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			String anim_choice;
			if (animation.isChecked()) anim_choice = "1";
			else anim_choice = "0";
			
			SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
			settings.edit().putString(SHOW_SPLASH, anim_choice).commit();
		}
	}); 
	
	tutorial.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			String tuto_choice;
			if (tutorial.isChecked()) tuto_choice = "1";
			else tuto_choice = "0";
			
			SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
			settings.edit().putString(SHOW_TUTORIAL, tuto_choice).commit();
		}
	}); 
	
	believers.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			String tuto_choice;
			if (believers.isChecked()) tuto_choice = "1";
			else tuto_choice = "0";
			
			SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
			settings.edit().putString(BELIEVERS_SONGS, tuto_choice).commit();
		}
	});
	
	zarohoni.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			String tuto_choice;
			if (zarohoni.isChecked()) tuto_choice = "1";
			else tuto_choice = "0";
			
			SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
			settings.edit().putString(TENZI_ZA_ROHONI, tuto_choice).commit();
		}
	});
	
	ciakuinira.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			String tuto_choice;
			if (ciakuinira.isChecked()) tuto_choice = "1";
			else tuto_choice = "0";
			
			SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
			settings.edit().putString(CIA_KUINIRA, tuto_choice).commit();
		}
	});	
	
	ownsongs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
			String tuto_choice;
			if (ownsongs.isChecked()) tuto_choice = "1";
			else tuto_choice = "0";
			
			SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
			settings.edit().putString(MY_OWN_SONGS, tuto_choice).commit();
		}
	});
	
	}
		
}
