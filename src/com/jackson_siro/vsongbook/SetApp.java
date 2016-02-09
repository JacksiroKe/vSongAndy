package com.jackson_siro.vsongbook;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jackson_siro.vsongbook.adaptor.CustomList;

import android.app.Activity;
import android.content.Intent;


public class SetApp extends ActionBarActivity {
	ListView list;
	String[] mytext = {"User Preferences","Display Preferences",
			"Payment Info","More Settings"} ;
	
	Integer[] imageId = { R.drawable.ic_notify, R.drawable.ic_notify,
			R.drawable.ic_notify,R.drawable.ic_notify};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		CustomList adapter = new CustomList(SetApp.this, mytext, imageId);
		list=(ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(SetApp.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
            	if (mytext[+ position].equals("User Preferences")) {
        			Intent intent = new Intent(getBaseContext(), Settings.class);
        			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			startActivity(intent);
        		}
        		
        		else if (mytext[+ position].equals("Display Preferences")) {
        			Intent intent = new Intent(getBaseContext(), Settings_I.class);
        			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			startActivity(intent);
        		}
        		
        		else if (mytext[+ position].equals("Payment Info")) {
        			Intent intent = new Intent(getBaseContext(), Settings_II.class);
        			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			startActivity(intent);
        		}
        		
            }
        });		
				
	}
			
}
