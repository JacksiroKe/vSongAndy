package com.jackson_siro.visongbook;

import java.util.Calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class XxxCritical extends ActionBarActivity {
	RelativeLayout MySong;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.critical);		
		setTitle("vSongBook has Terminated");
	}
		
	public void onExit(View v) {
		finish();
	}	

    public void ActivateApp(View view) {
    	EditText ActivaText = (EditText) findViewById(R.id.activation_code);
    	String ActivatorCode = ActivaText.getText().toString();
    	Calendar rightNow = Calendar.getInstance();
	    int CurHour = rightNow.get(Calendar.HOUR_OF_DAY);
	    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
      	
        if (ActivatorCode.contains("JCSR"))
        {
            String[] CodeHr = ActivatorCode.split("JCSR");
            int CdHour = Integer.parseInt(CodeHr[1]);
            
            if ((CurHour - CdHour) < 3)
            {
            	editor.putBoolean("js_vsb_is_paid", true);
		  		editor.commit();
		  		YouMustJustPay(1);
		  		ActivaText.setText("");
            }
            else
            {
            	YouMustJustPay(2);
		  		ActivaText.setText("");
                
            }
        }
        else
        {
        	YouMustJustPay(3);
	  		ActivaText.setText("");
        }

    }

    public void YouMustJustPay(int showthis) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	if (showthis==1) {
    		builder.setTitle("App Unlocked!");
    		builder.setMessage("God bless you. vSongBook Application on your device has been unlocked. It is now Premium. If you loose this app in anyway and install again please do not hesitate to contact the developer Bro. Jack Siro for an Activation Code via SMS on +254711474787 or Email on smataweb@gmail.com. God bless you.");
    		builder.setPositiveButton("Okay, Got It", new DialogInterface.OnClickListener() {					
    			@Override
    			public void onClick(DialogInterface arg0, int arg1) {
    				startActivity(new Intent(XxxCritical.this, AppStart.class));
    				finish();		
    			}
    		});
    	}
    	else if (showthis==2) {
    		builder.setTitle("Error in the code!");
    		builder.setMessage("God bless you. Unfortunately vSongBook Application on your device can not be unlocked. The code you enterered is invalid or has expired_i. Please try again or request for another from the developer Bro. Jack Siro for help via SMS on +254711474787 or Email on smataweb@gmail.com. God bless you.");
    		builder.setPositiveButton("Okay, Got It", new DialogInterface.OnClickListener() {					
    			@Override
    			public void onClick(DialogInterface arg0, int arg1) {
    								
    			}
    		});
    	}
    	else if (showthis==3) {
    		builder.setTitle("Error in the code!");
    		builder.setMessage("God bless you. Unfortunately vSongBook Application on your device can not be unlocked. The code you enterered is invalid or has expired_i. Please try again or request for another from the developer Bro. Jack Siro for help via SMS on +254711474787 or Email on smataweb@gmail.com. God bless you.");
    		builder.setPositiveButton("Okay, Got It", new DialogInterface.OnClickListener() {					
    			@Override
    			public void onClick(DialogInterface arg0, int arg1) {
    								
    			}
    		});
    	}
            	
        builder.show();
	
	  }
    
}