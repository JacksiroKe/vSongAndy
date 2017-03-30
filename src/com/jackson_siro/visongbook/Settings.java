package com.jackson_siro.visongbook;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jackson_siro.visongbook.adaptor.CustomList;

import android.content.Intent;


public class Settings extends ActionBarActivity {
	ListView list;
	String[] mytext = {"item_i", "item_ii", "item_iii", "item_iv"} ;
		
	private String[] mytexti;
	private String[] mytextii;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		mytexti = getResources().getStringArray(R.array.SetAppList);
		mytextii = getResources().getStringArray(R.array.SetAppListDesc);

		CustomList adapter = new CustomList(Settings.this, mytext, mytexti, mytextii);
		list=(ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(SetApp.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
            	if (mytext[+ position].equals("item_i")) {
        			Intent intent = new Intent(getBaseContext(), Settings1.class);
        			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			startActivity(intent);
        		}
        		
        		else if (mytext[+ position].equals("item_ii")) {
        			Intent intent = new Intent(getBaseContext(), Settings2.class);
        			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			startActivity(intent);
        		}
        		
        		else if (mytext[+ position].equals("item_iii")) {
        			Intent intent = new Intent(getBaseContext(), Settings3.class);
        			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			startActivity(intent);
        		}
            	
        		else if (mytext[+ position].equals("item_iv")) {
        			Toast.makeText(Settings.this, R.string.sorry_text, Toast.LENGTH_SHORT).show();
        		}
        		
            }
        });		
				
	}
			
}
