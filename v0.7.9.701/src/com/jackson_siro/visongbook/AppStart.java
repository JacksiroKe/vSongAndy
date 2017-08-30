package com.jackson_siro.visongbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class AppStart extends ActionBarActivity {
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs", SHOW_TUTORIAL = "show_tutorial";
	Button ProceedBtn;
	TextView PaymentInfo, PaymentNote;
	SharedPreferences vSettings;
	SharedPreferences.Editor localEditor;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aa_b); 
		vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        
        PaymentInfo = (TextView) findViewById(R.id.textView1);
		PaymentNote = (TextView) findViewById(R.id.textView2);
		ProceedBtn = (Button) findViewById(R.id.proceedNow);
	    
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_change_over", true))
		{
			setTitle(R.string.thanks_updating);
			PaymentInfo.setText(R.string.thanks_updating_i);
			PaymentNote.setText(R.string.thanks_updating_ii);
			ProceedBtn.setText(R.string.proceed_btn);
		}			    
        changeStatusBarColor();	
	}

	// Making notification bar transparent
    @SuppressLint("NewApi")
	private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.orange));
        }
    }
 
	public void Proceed(View view) {
		startActivity(new Intent(AppStart.this, BbDemo.class));
	    finish();
    }
	
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.proceed, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.donehere:
            	SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        	    localEditor.putBoolean("as_vsb_show_tutorial", true);
        	    localEditor.commit();
        	    
        	    startActivity(new Intent(AppStart.this, BbDemo.class));
        	    finish();
                return true;
            
            default:
                return false;
        }
    }

}