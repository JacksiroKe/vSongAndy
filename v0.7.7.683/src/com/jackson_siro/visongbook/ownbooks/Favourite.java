package com.jackson_siro.visongbook.ownbooks;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.adaptor.SimpleGestureFilter;
import com.jackson_siro.visongbook.adaptor.SimpleGestureFilter.SimpleGestureListener;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Favourite extends ActionBarActivity implements SimpleGestureListener {
	
	public static final String VSB_SETTINGS = "Vsb_Settings";
	public static final String FONT_SIZE = "font_size";
	public static final String CUR_OWN_FAVOUR = "12";
	
	TextView favourTitle;
	TextView contentName;
	Favour selectedFavour;
	SQLiteHelperF db;
	private TableLayout MyToolbar;
	
	private SimpleGestureFilter detector;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ob_song);
		ScrollView ScrollScroll = (ScrollView) findViewById(R.id.ScrollSong);
		getLayoutInflater().inflate(R.layout.ob_song_scroll, ScrollScroll);
		
		ShowToolBar();
		
		contentName = (TextView) findViewById(R.id.content);
		
		detector = new SimpleGestureFilter(this,this);
		
		Intent intent = getIntent();
		int qid = intent.getIntExtra("favour", -1);

		db = new SQLiteHelperF(getApplicationContext());
		selectedFavour = db.readFavour(qid);
		
		SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 12);		
        String favourid = Integer.toString(qid);
	    settings.edit().putString(CUR_OWN_FAVOUR, favourid).commit(); 

		initializeViews();
	}
	
	public String PassText(String str) {
        return str.replace('+', '\"').replace("^", "'").replace("$", System.getProperty("line.separator")).replace("&hellip;", "...").replace("&mdash;", "\u2014").replace("&nbsp;", " ").replace("&rsquo;", "'");
    }
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent me){
        
         this.detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
    }
    @Override
     public void onSwipe(int direction) {
      String str = "";
            
      switch (direction) {
      
      case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
      	  
	      break;
      case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
      		
	      break;
      }
       
     }
      
     @Override
     public void onDoubleTap() {
    	 MyToolbar = (TableLayout) findViewById(R.id.myToolbar);
    	 
    	 if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_show_toolbar", false)) {
  			SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
  	 	    localEditor.putBoolean("js_vsb_show_toolbar", true);
  	 	    localEditor.commit();
  	 	    Toast.makeText(this, "Toolbar Hidden", Toast.LENGTH_SHORT).show();
  	 	    MyToolbar.setVisibility(View.GONE);
  			
  		} else  {
  			SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
  	 	    localEditor.putBoolean("js_vsb_show_toolbar", false);
  	 	    localEditor.commit();
  	 	    Toast.makeText(this, "Toolbar Reshown", Toast.LENGTH_SHORT).show();
  			MyToolbar.setVisibility(View.VISIBLE);
  		} 
     }
     
     public void KeepScreenOn(){
    	contentName = (TextView) findViewById(R.id.content);
 		
 		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("keep_screen_on", false)) {
 			//getWindow().setFlags(this.getWindow().getFlags() & )
 		} else {
 			
 		}
 	}
     
     public void ShowToolBar(){
  		MyToolbar = (TableLayout) findViewById(R.id.myToolbar);
  		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_show_toolbar", false)) {
  			MyToolbar.setVisibility(View.VISIBLE);
  		} else {
  			MyToolbar.setVisibility(View.GONE);
  		}
  	}
     
 	public void Previous (View view){
 		SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 12);
         String current_favour = settings.getString(CUR_OWN_FAVOUR, "12");
         int qid = (Integer.parseInt(current_favour)-1);
 	    
 		db = new SQLiteHelperF(getApplicationContext());
 		selectedFavour = db.readFavour(qid);
 		initializeViews();
 		
 		String favourid = Integer.toString(qid);
 	    settings.edit().putString(CUR_OWN_FAVOUR, favourid).commit();

 	}

 	public void Next (View view){
 		SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 12);
         String current_favour = settings.getString(CUR_OWN_FAVOUR, "12");
         int qid = (Integer.parseInt(current_favour)+1);
 	    
 		db = new SQLiteHelperF(getApplicationContext());
 		selectedFavour = db.readFavour(qid);
 		initializeViews();
 		
 		String favourid = Integer.toString(qid);
 	    settings.edit().putString(CUR_OWN_FAVOUR, favourid).commit();
 	}
 	
	public void Minus (View view){
		SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 25);
        String font_size = settings.getString(FONT_SIZE, "25");
        
        int newFonts = (Integer.parseInt(font_size)-2);
        String New_Fonts = Integer.toString(newFonts);
	    settings.edit().putString(FONT_SIZE, New_Fonts).commit();
	    		
		contentName.setTextSize(newFonts);
		
	}
	
	public void Plus (View view){
		SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 25);
        String font_size = settings.getString(FONT_SIZE, "25");
        
        int newFonts = (Integer.parseInt(font_size)+2);
        String New_Fonts = Integer.toString(newFonts);
	    settings.edit().putString(FONT_SIZE, New_Fonts).commit();	    
	    		
		contentName.setTextSize(newFonts);
		
	}

	public void initializeViews() {
		SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 25);
        String font_size = settings.getString(FONT_SIZE, "25");
        
		setTitle(selectedFavour.getTitle());
		contentName.setText(PassText(selectedFavour.getContent()));
		//contentName.setMovementMethod(new ScrollingMovementMethod());
		
		int myFonts = Integer.parseInt(font_size);
		contentName.setTextSize(myFonts);
        
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favourite, menu);
        
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            
            case R.id.delete:
            	AlertDialog.Builder builder = new AlertDialog.Builder(Favourite.this);
				builder.setTitle("Just a minute...");
				builder.setMessage("Are you sure you want to delete this song: " + selectedFavour.getTitle() + "? This action is not reversable.");
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Toast.makeText(getApplicationContext(), "God bless you", Toast.LENGTH_LONG).show();
						
					}
				});

				builder.setPositiveButton("Amen", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Toast.makeText(getApplicationContext(), "You have deleted the song " + selectedFavour.getTitle(), Toast.LENGTH_LONG).show();
						db.deleteFavour(selectedFavour);
		        		finish();
					}
				});
				
				builder.show(); //To show the AlertDialog
				
                return true;

            case R.id.share:
            	shareThisFavour();
            	return true;
            	
            default:
                return false;
        }
	}
	
	private void shareThisFavour() {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		
		String thetitle = selectedFavour.getTitle();
		String thecontent = selectedFavour.getContent();
		
		share.putExtra(Intent.EXTRA_SUBJECT, PassText(thetitle));
        share.putExtra(Intent.EXTRA_TEXT, PassText(thecontent) +"\n\nVia vSongBook Android App");
		startActivity(Intent.createChooser(share, "Share this Song"));
	}
}
