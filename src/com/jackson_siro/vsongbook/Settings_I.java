package com.jackson_siro.vsongbook;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Settings_I extends ActionBarActivity{
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String FONT_SIZE = "font_size";
	public static final String SET_THEME = "set_theme";
	
	private SeekBar seekBar;
	private TextView preview;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
	super .onCreate(savedInstanceState);
	setContentView(R.layout.settings_i);
	SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
    String font_size = settings.getString(FONT_SIZE, "25");
    
	seekBar = (SeekBar) findViewById(R.id.seekBar1);
	preview = (TextView) findViewById(R.id.textView2);	
	textView = (TextView) findViewById(R.id.textView1);
		
	final int myFonts = Integer.parseInt(font_size);
	preview.setTextSize(myFonts);
	textView.setText( "Font Size: " + myFonts);
	seekBar.setProgress(myFonts);
	
	seekBar.setOnSeekBarChangeListener(new	OnSeekBarChangeListener() {	
		int progress = myFonts ;
		
			@Override	
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser){
				progress = progresValue;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override	
			public void onStopTrackingTouch(SeekBar	seekBar) {	
				textView.setText( "Font Size: " + progress);	
				preview.setTextSize(progress);
				
				String font_size = Integer.toString(progress);
				SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 25);
			    settings.edit().putString(FONT_SIZE, font_size).commit();
    
			}
		});
	
	}
		
}
