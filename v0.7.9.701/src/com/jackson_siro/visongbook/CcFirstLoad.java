package com.jackson_siro.visongbook;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jackson_siro.visongbook.tools.*;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class CcFirstLoad extends AppFunctions {
	SbListAdapter dataAdapter = null;
	
	ArrayList<SbItem> SongBookList = new ArrayList<SbItem>(); 
	String SelectedSbnos, SelectedSbcodes, selectedMsg;
	ListView SongBooks;
	TextView TitleError, TitleLoad;
	ImageView Splashme;

    SharedPreferences vSettings;
	SharedPreferences.Editor localEditor;
	Button btnRetry;
	ProgressBar mProgressView;
	RelativeLayout AmLoading, TrackingStep1;
	
	SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
	
	JSONParser jParser = new JSONParser();
	ArrayList<HashMap<String, String>> songbookList;

	private static final String TAG_SUCCESS = "success", TAG_SONGBOOKS = "songbooks", TAG_NUMBER = "book_number", TAG_TITLE = "book_title", TAG_CONTENT = "book_content", TAG_CODE = "book_code", TAG_SCOUNT = "book_scount", TAG_ACTIVE = "book_active", TAG_CREATED = "book_created";

	List<SbItem> mylist;
	List<SongBook> sblist;
	ArrayAdapter<String> myAdapter;
	
	JSONArray sbooks = null;
	ListView list;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bb_first_load);
        vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        
        changeStatusBarColor();	
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_logged_in_user", false)) {
    		localEditor.putBoolean("as_vsb_logged_in_user", false);
		    localEditor.commit();		    
		    startActivity(new Intent(this, BbLogin.class));
		    finish();
  		} 
        btnRetry = (Button) findViewById(R.id.btnretry); 
        SongBooks = (ListView) findViewById(R.id.songbooklist);
        TitleError = (TextView) findViewById(R.id.txtload);
		TitleLoad = (TextView) findViewById(R.id.TitleLoad);
		Splashme = (ImageView) findViewById(R.id.splashme);
		mProgressView = (ProgressBar) findViewById(R.id.loading);
		AmLoading = (RelativeLayout) findViewById(R.id.Step1);
		
		//songbookList = new ArrayList<HashMap<String, String>>();
		//new LoadAllSongbooks().execute();
		
		if (isInternetOn()){
			songbookList = new ArrayList<HashMap<String, String>>();
			new LoadAllSongbooks().execute();
		}
	    else NotConnected();
	}
    
	protected void NotConnected() {
		mProgressView.setVisibility(View.GONE);
		TitleError.setText(R.string.cant_connect);
		btnRetry.setVisibility(View.VISIBLE);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			SongBooks.setVisibility(show ? View.GONE : View.VISIBLE);
			SongBooks.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							SongBooks.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});
		} else {
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			SongBooks.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	public void SelectSongBooks(View view)  {
		SelectedSbnos = "";
		SelectedSbcodes = "";
		selectedMsg = "";
		boolean sbselected = false;
		ArrayList<SbItem> SongBookList = dataAdapter.SongBookList;   
		for(int i=0;i<SongBookList.size();i++){      
			SbItem SbItem = SongBookList.get(i);    
			if(SbItem.isSelected()){       
				sbselected = true;
				selectedMsg = selectedMsg + "\n" + SbItem.getBook();
				SelectedSbnos = SelectedSbnos + SbItem.getNumber() + ",";
				SelectedSbcodes = SelectedSbcodes + SbItem.getCode() + ",";
			}  
		}
		
		if (sbselected){
			SelectedSbnos = SelectedSbnos.substring(0, (SelectedSbnos.length()-1));
			selectedMsg = "You have selected the following songbooks..." + 
					selectedMsg + "\n\nPlease Proceed to the next " +
					"step or Cancel to reselect.";
			localEditor.putString("as_vsb_sbnos", SelectedSbnos);	
			localEditor.putString("as_vsb_sbcodes", SelectedSbcodes);    
		    localEditor.commit();
		    proceedLoading(selectedMsg);
		} else cantProceed();
	}

    public void proceedLoading(String responseText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.just_a_min);
        builder.setMessage(responseText);
        builder.setNegativeButton(R.string.cancel_step, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//finish();	none_selected
				//Do nothing
			}
		});
        
        builder.setPositiveButton(R.string.proceed_now, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
		        GotoNextStep();
			}
		});
        
        builder.show();
	
	  }

    public void cantProceed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.just_a_min);
        builder.setMessage(R.string.none_selected);
        builder.setNegativeButton(R.string.okay_got_it, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//finish();	
				//Do nothing
			}
		});
        
        builder.show();
	
	  }
	
    private void GotoNextStep(){
    	localEditor.putBoolean("as_vsb_finished_loading", true);
	    localEditor.commit();
	    startActivity(new Intent(this, CcLoading.class));
    }
    public String getBookPrice(String contstr){
		String[] strings = TextUtils.split(contstr, "|");
		return strings[0];
	}
    
    private void displaySongbookList(){
    	sblist = sbDB.getAllSongBooks();
		for (int i = 0; i < sblist.size(); i++) {
			String song_title = sblist.get(i).getTitle();
			String song_songs = sblist.get(i).getScount();
			String song_cont = sblist.get(i).getContent();
			String song_code = sblist.get(i).getCode();
			String song_nom = sblist.get(i).getNumber();
			SongBookList.add(new SbItem(song_title, song_songs + " Songs, Price: " + 
					song_cont, song_code,song_nom, false));
		}
		
		dataAdapter = new SbListAdapter(this, R.layout.cc_list_select, SongBookList); 
		ListView listView = (ListView) findViewById(R.id.songbooklist); 
		listView.setAdapter(dataAdapter);
		
		AmLoading.setVisibility(View.GONE);
		setTitle(R.string.select_songbooks);
	}
	
	@SuppressLint("InflateParams")
	private class SbListAdapter extends ArrayAdapter<SbItem> {
		ArrayList<SbItem> SongBookList;
		
		public SbListAdapter(Context context, int textViewResourceId,  
		ArrayList<SbItem> SongBookList) {    
			super(context, textViewResourceId, SongBookList);  
			this.SongBookList = new ArrayList<SbItem>();  
			this.SongBookList.addAll(SongBookList); 
		}
	
		private class ViewHolder { CheckBox book; TextView title; TextView descri; }
		
		@SuppressLint("InflateParams")
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;  Log.v("ConvertView", String.valueOf(position));
			if (convertView == null) {   
				LayoutInflater vi = (LayoutInflater)getSystemService(      
				Context.LAYOUT_INFLATER_SERVICE);  
				convertView = vi.inflate(R.layout.cc_list_select, null);
				holder = new ViewHolder();  
				holder.title = (TextView) convertView.findViewById(R.id.book_title);
				holder.descri = (TextView) convertView.findViewById(R.id.book_descri);   
				holder.book = (CheckBox) convertView.findViewById(R.id.book_check);  
				convertView.setTag(holder);
				holder.book.setOnClickListener( new View.OnClickListener() {      
					public void onClick(View v) {       
						CheckBox cb = (CheckBox) v ;     
						SbItem SbItem = (SbItem) cb.getTag();
						SbItem.setSelected(cb.isChecked());
					}   
				});  
			} else {     
				holder = (ViewHolder) convertView.getTag();  
			}
			
			SbItem SbItem = SongBookList.get(position);    
			holder.title.setText(SbItem.getBook());      
			holder.descri.setText(SbItem.getDescri());  
			holder.book.setChecked(SbItem.isSelected());  
			holder.book.setTag(SbItem);
			return convertView;
		}
	}
	
	public class SbItem {  
		String book = null; 
		String descri = null;
		String code = null; 
		String number; 
		boolean selected = false;
		
		public SbItem(String book, String descri, String code, String number, boolean selected) {   
			super(); 
			this.book = book;
			this.descri = descri;
			this.code = code; 
			this.number = number; 
			this.selected = selected;
		}
		public String getBook() 
		{ 
			return book; 
		}		
		public void setBook(String book) 
		{ 
			this.book = book; 
		}	
		public String getDescri() 
		{ 
			return descri; 
		}		
		public void setSongs(String descri) 
		{ 
			this.descri = descri; 
		}
		public String getCode() 
		{ 
			return code; 
		}		
		public void setCode(String code) 
		{ 
			this.code = code; 
		}	
		public String getNumber() 
		{ 
			return number; 
		}		
		public void setNumber(String number) 
		{ 
			this.number = number; 
		}	
		public boolean isSelected() 
		{ 
			return selected; 
		}
		public void setSelected(boolean selected) 
		{ 
			this.selected = selected;
		} 
	}

	class LoadAllSongbooks extends AsyncTask<String, String, String> {
		
		SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		String songbook = vSettings.getString("as_vsb_sbnos", "NA");
		String siteurl = vSettings.getString("as_vsb_siteurl", "NA") + "as_mobile/as_books_list.php";
		String audiourl = vSettings.getString("as_vsb_siteurl", "NA") + "as_media/";
		String AciveBook = TAG_ACTIVE;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress(true);
		}
		
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();			
			params.add(new BasicNameValuePair("songbook", songbook));				
			JSONObject json = jParser.makeHttpRequest(siteurl, "GET", params);		
			
			//Log.d("All songbooks: ", json.toString());
			sbDB.deleteSongbooks();
			
			try {
				int success = json.getInt(TAG_SUCCESS);				
				if (success == 1) {
					sbooks = json.getJSONArray(TAG_SONGBOOKS);
					for (int i = 0; i < sbooks.length(); i++) {
						JSONObject c = sbooks.getJSONObject(i);
						String b_number = c.getString(TAG_NUMBER);
						String b_title = c.getString(TAG_TITLE);
						String b_content = c.getString(TAG_CONTENT);
						String b_code = c.getString(TAG_CODE);
						String b_created = c.getString(TAG_CREATED);
						String b_scount = c.getString(TAG_SCOUNT);
						
						sbDB.newSongBook(new SongBook(b_number, b_title, b_content, b_code, b_created, b_scount));
					}
				} 
			} catch (JSONException e) {
				e.printStackTrace();
				showProgress(false);
				finish();
			}
			return null;
		}

		protected void onPostExecute(String endresult) {
			showProgress(false);
			displaySongbookList();
		}
	}
}