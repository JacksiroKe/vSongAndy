package com.jackson_siro.visongbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

@SuppressWarnings("deprecation")
public class AppStart extends ActionBarActivity {
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String SHOW_TUTORIAL = "show_tutorial";

    private ScrollView ScrollScroll;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aa_c);
		setTitle(R.string.welcome_tovsb);
		ScrollScroll = (ScrollView) findViewById(R.id.ScrollStart);
        getLayoutInflater().inflate(R.layout.aa_d, this.ScrollScroll);        
					
	}
	
	public void Proceed(View view)   {
		SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    localEditor.putBoolean("js_vsb_show_tutorial", true);
	    localEditor.commit();
	  		
	    startActivity(new Intent(AppStart.this, BbDemo.class));
	    finish();
    }
	
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start:
            	SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        	    localEditor.putBoolean("js_vsb_show_tutorial", true);
        	    localEditor.commit();
        	    
        	    startActivity(new Intent(AppStart.this, BbDemo.class));
        	    finish();
                return true;
            
            default:
                return false;
        }
    }

}