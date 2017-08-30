package com.jackson_siro.visongbook;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import java.util.ArrayList;
import com.jackson_siro.visongbook.songbooks.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

@SuppressWarnings("deprecation")
public class BbLoading extends ActionBarActivity {
	SbListAdapter dataAdapter = null;
	 	
	ArrayList<SongBook> SongBookList = new ArrayList<SongBook>(); 
	private Handler customHandler = new Handler();
	
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	
	ListView SongBooks;
	private TextView TitleLoad, Counting;
	private RelativeLayout TrackingStep1, TrackingStep2, TrackingStep3;
	private ImageView Splashme;
	private long startTime = 0L;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bb_song_load);
        
        SongBooks = (ListView) findViewById(R.id.songbooklist);        
		TitleLoad = (TextView) findViewById(R.id.TitleLoad);
		Counting = (TextView) findViewById(R.id.countThis);

		TrackingStep1 =  (RelativeLayout) findViewById(R.id.Step1);
		TrackingStep2 =  (RelativeLayout) findViewById(R.id.Step2);
		TrackingStep3 = (RelativeLayout) findViewById(R.id.Step3);
		Splashme = (ImageView) findViewById(R.id.splashme);
		
		  
		displaySongbookListView();	
        //Toast.makeText(this, count + " songs", Toast.LENGTH_SHORT).show();
        /*
        if (count < 700){        	
    	    
        	startTime = SystemClock.uptimeMillis();
    		customHandler.postDelayed(updateTimerThread, 3000);
        	
        } 	else if (count < 950){
        	startTime = SystemClock.uptimeMillis();
    		customHandler.postDelayed(updateTimerThread, 3000);
        	
        }	else if (count < 1900){
        	startTime = SystemClock.uptimeMillis();
    		customHandler.postDelayed(updateTimerThread, 3000);
        	
        }	else if (count < 2300){
        	startTime = SystemClock.uptimeMillis();
    		customHandler.postDelayed(updateTimerThread, 3000);
        	
        }	else if (count < 3000){
        	startTime = SystemClock.uptimeMillis();
    		customHandler.postDelayed(updateTimerThread, 3000);
        	        	
        }	else if (count < 3600){
        	startTime = SystemClock.uptimeMillis();
    		customHandler.postDelayed(updateTimerThread, 3000);
        } else {
        	
        	Tracking.setVisibility(View.GONE);
			Proceeding.setVisibility(View.VISIBLE);
			
        } */
    }

	private void displaySongbookListView() {
		SongBookList.add(new SongBook("Songs of Worship [750]",false)); 
		SongBookList.add(new SongBook("Nyimbo za Injili [215]",false));
		SongBookList.add(new SongBook("Believers Songs [200]",false));
		SongBookList.add(new SongBook("Nyimbo za Wokovu [383]",false));
		SongBookList.add(new SongBook("Redemption Songs [400]",false));
		SongBookList.add(new SongBook("Tenzi za Rohoni [138]",false));
		SongBookList.add(new SongBook("Nyimbo cia Kuinira Ngai [603]",false));
		SongBookList.add(new SongBook("Mbathi sya Kumutaiia Ngai [470]",false));
		SongBookList.add(new SongBook("Che Kilosine Jehovah [140]",false));
		
		dataAdapter = new SbListAdapter(this, R.layout.cc_list_select, SongBookList); 
		ListView listView = (ListView) findViewById(R.id.songbooklist); 
		listView.setAdapter(dataAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {    
			public void onItemClick(AdapterView<?> parent, View view,      
				int position, long id) {         
				SongBook SongBook = (SongBook) parent.getItemAtPosition(position);   
				//Toast.makeText(getApplicationContext(), "Clicked on Row: " + SongBook.getName(), Toast.LENGTH_LONG).show();  
			} 
		});
	}
	
	public void SelectSongBooks(View view)   {
		SongBooks.setVisibility(View.GONE);
		Splashme.setVisibility(View.VISIBLE);
		
		TrackingStep1.setVisibility(View.GONE);
		TrackingStep2.setVisibility(View.VISIBLE);
		
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    localEditor.putBoolean("js_vsb_finished_loading", true);
	    localEditor.commit();
	    
		Cursor cursor = managedQuery(SongBookProvider.CONTENT_URI, null, null,
        		new String[] {"SongsOfWorship"}, null);

        if (cursor == null) {
        	startTime = SystemClock.uptimeMillis();
        	customHandler.postDelayed(updateTimerThread, 3000);
		} else {
			//int count = cursor.getCount();
			startTime = SystemClock.uptimeMillis();
        	customHandler.postDelayed(updateTimerThread, 3000);
		}
				
    }
    
    private Runnable updateTimerThread = new Runnable() {

		public void run() {
			
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			
			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			
	        //String reach = count / 3600 * 100 +" %";
			//Counting.setText(count + " songs so far ...");
			customHandler.postDelayed(this, 3000);
			
			if (secs < 5){
	        	TitleLoad.setText("Now Loading: 5 %");
	        	Counting.setText("750 Songs of Worship...");
	        	Splashme.setImageResource(R.drawable.splash_i);
	        	
	        } 	else if (secs < 10){
	        	TitleLoad.setText("Loading Songbook: 10 %");
	        	Counting.setText("215 Nyimbo za Injili...");
	        	Splashme.setImageResource(R.drawable.splash_ii);
	        	
	        }	else if (secs < 30){
	        	TitleLoad.setText("Loading Songbook: 30 %");
	        	Counting.setText("1083 Believers Songs...");
	        	Splashme.setImageResource(R.drawable.splash_iii);
	        	
	        }	else if (secs < 50){
	        	TitleLoad.setText("Loading Songbook: 50 %");
	        	Counting.setText("383 Nyimbo za Wokovu...");
	        	Splashme.setImageResource(R.drawable.splash_iv);
	        	
	        }	else if (secs < 65){
	        	TitleLoad.setText("Patience pays: 65 %");
	        	Counting.setText("712 Redemption Songs...");
	        	Splashme.setImageResource(R.drawable.splash_v);
	        	        	
	        }	else if (secs < 80){
	        	TitleLoad.setText("Almost Done Loading: 80 %");
	        	Counting.setText("136 Tenzi Za Rohoni...");
	        	Splashme.setImageResource(R.drawable.splash_vi);
	        	
	        }	else if (secs < 95){
	        	TitleLoad.setText("Just Finalizing: 95 %");
	        	Counting.setText("603 Nyimbo cia Kuinira Ngai...");
	        	Splashme.setImageResource(R.drawable.splash_vii);
	        	
	        } else if (secs > 100){
	        	timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);
	        	Splashme.setImageResource(R.drawable.splash_viii);
	        	
	        	//setContentView(new MainGamePanel(this));
	        	//TitleLoad.setText("Done Loading SongBook: " + " | 100 %");
				//Counting.setText("Thanks for your patience.");

	    		TrackingStep2.setVisibility(View.GONE);
	    		TrackingStep3.setVisibility(View.VISIBLE);
	        }
		}

	};
	
	public void StartSinging(View view)   {
		startActivity(new Intent(this, CcSongBook.class));		
    }
		
	private class SbListAdapter extends ArrayAdapter<SongBook> {
		private ArrayList<SongBook> SongBookList;

		public SbListAdapter(Context context, int textViewResourceId,  
		ArrayList<SongBook> SongBookList) {    
			super(context, textViewResourceId, SongBookList);  
			this.SongBookList = new ArrayList<SongBook>();  
			this.SongBookList.addAll(SongBookList); 
		}
	
		private class ViewHolder { CheckBox name; }
		
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;  Log.v("ConvertView", String.valueOf(position));
			if (convertView == null) {   
				LayoutInflater vi = (LayoutInflater)getSystemService(      
				Context.LAYOUT_INFLATER_SERVICE);  
				convertView = vi.inflate(R.layout.cc_list_select, null);
				holder = new ViewHolder();  
				//holder.code = (TextView) convertView.findViewById(R.id.code);  
				holder.name = (CheckBox) convertView.findViewById(R.id.checkbox);  
				convertView.setTag(holder);
				holder.name.setOnClickListener( new View.OnClickListener() {      
					public void onClick(View v) {       
						CheckBox cb = (CheckBox) v ;     
						//SongBook SongBook = (SongBook) cb.getTag(); 
					}   
				});  
			} else {     
				holder = (ViewHolder) convertView.getTag();  
			}
			
			SongBook SongBook = SongBookList.get(position);  
			//holder.code.setText(" (" +  SongBook.getCode() + ")");  
			holder.name.setText(SongBook.getName());  
			holder.name.setChecked(SongBook.isSelected());  
			holder.name.setTag(SongBook);
			return convertView;
		}
	}
	
	public class SongBook {  
		String name = null; boolean selected = false;
		public SongBook(String name, boolean selected) {   
			super(); this.name = name; this.selected = selected;
		}
		public String getName() { return name; }		
		public void setName(String name) { this.name = name; }		
		public boolean isSelected() { return selected; }
		public void setSelected(boolean selected) { this.selected = selected;} 
	}
}