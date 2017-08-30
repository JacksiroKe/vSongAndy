package com.jackson_siro.visongbook.adaptor;

import java.util.ArrayList;

import com.jackson_siro.visongbook.*;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log; 
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.ViewGroup; 
import android.view.View.OnClickListener; 
import android.widget.AdapterView; 
import android.widget.ArrayAdapter; 
import android.widget.Button; 
import android.widget.CheckBox; 
import android.widget.ListView; 
import android.widget.TextView; 
import android.widget.Toast; 
import android.widget.AdapterView.OnItemClickListener;

public class CustomSelectList extends Activity {
	MyCustomAdapter dataAdapter = null;
 
	@Override  
	public void onCreate(Bundle savedInstanceState) {   
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.bb_song_load);		  
		displayListView();
		checkButtonClick();
	}
	
	private void displayListView() {
		//Array list of countries   
		ArrayList<SongBook> SongBookList = new ArrayList<SongBook>(); 
		SongBook SongBook = new SongBook("Afghanistan",false); 
		SongBookList.add(SongBook); 
		SongBook = new SongBook("Albania",true); 
		SongBookList.add(SongBook); 
		SongBook = new SongBook("Algeria",false); 
		SongBookList.add(SongBook); 
		SongBookList.add(SongBook);
		
		dataAdapter = new MyCustomAdapter(CustomSelectList.this, R.layout.cc_list_select, SongBookList); 
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

	private class MyCustomAdapter extends ArrayAdapter<SongBook> {
		private ArrayList<SongBook> SongBookList;

		public MyCustomAdapter(Context context, int textViewResourceId,  
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
						SongBook SongBook = (SongBook) cb.getTag();     
						Toast.makeText(getApplicationContext(),        
						"Clicked on Checkbox: " + cb.getText() +        " is " + cb.isChecked(),        
						Toast.LENGTH_LONG).show();     SongBook.setSelected(cb.isChecked());    
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
	
	private void checkButtonClick() {
		Button myButton = (Button) findViewById(R.id.select); 
		myButton.setOnClickListener(new OnClickListener() {
			@Override    public void onClick(View v) {
				StringBuffer responseText = new StringBuffer();   
				responseText.append("The following were selected...\n");
				ArrayList<SongBook> SongBookList = dataAdapter.SongBookList;   
				for(int i=0;i<SongBookList.size();i++){      
					SongBook SongBook = SongBookList.get(i);    
					if(SongBook.isSelected()){       
						responseText.append("\n" + SongBook.getName());    
					}   
				}
				Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
			}
		});
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