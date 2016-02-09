package com.jackson_siro.vsongbook;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jackson_siro.vsongbook.adaptor.SimpleGestureFilter;
import com.jackson_siro.vsongbook.adaptor.SimpleGestureFilter.SimpleGestureListener;

@SuppressWarnings("deprecation")
public class SongView extends ActionBarActivity implements SimpleGestureListener {
	private SimpleGestureFilter detector;
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String FONT_SIZE = "font_size";
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song);
        
        SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 25);
        String font_size = settings.getString(FONT_SIZE, "25");
        /*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/

        Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);

        if (cursor == null) {
            finish();
        } else {
            cursor.moveToFirst();

            TextView content = (TextView) findViewById(R.id.songcont);

            int wIndex = cursor.getColumnIndexOrThrow(SongbookDatabase.KEY_SONGCONT);
            int dIndex = cursor.getColumnIndexOrThrow(SongbookDatabase.KEY_SONG);

            //song.setText(cursor.getString(wIndex));
            setTitle(cursor.getString(wIndex));
            content.setText(cursor.getString(dIndex));
            content.setMovementMethod(new ScrollingMovementMethod());
            
            int myFonts = Integer.parseInt(font_size);
            content.setTextSize(myFonts);
        }
        detector = new SimpleGestureFilter(this,this);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        
         this.detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
    }
    @Override
     public void onSwipe(int direction) {
      String str = "";
      SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 25);
      String font_size = settings.getString(FONT_SIZE, "25");
      
      TextView content = (TextView) findViewById(R.id.songcont);

      int myFonts = Integer.parseInt(font_size);
      
      switch (direction) {
      
      case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
      	  int lowfont = myFonts - 2;
      	  
	      	String low_fonts = Integer.toString(lowfont);
		    settings.edit().putString(FONT_SIZE, low_fonts).commit();
      	  
      	  content.setTextSize(lowfont);
	      //Toast.makeText(this, "Font size decreased upto " + lowfont, Toast.LENGTH_SHORT).show();
	      break;
      case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
      		int highfont = myFonts + 2;  
      		
      		String high_fonts = Integer.toString(highfont);
		    settings.edit().putString(FONT_SIZE, high_fonts).commit();
      		
      		content.setTextSize(highfont ); 	
	      //Toast.makeText(this, "Font size increased upto " + highfont, Toast.LENGTH_SHORT).show();
	      break;
      }
       //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
     }
      
     @Override
     public void onDoubleTap() {
    	 //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
     }
    
     @Override
  	public void onBackPressed() {
     	 Intent intent = new Intent(this, SongSearch.class);
     	 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
          finish();
  	}
     
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.song_view, menu);
        /*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;

            case R.id.share:
            	shareThisSong();
                return true;
                
            case R.id.copythis:
            	
            	Uri uri = getIntent().getData();
				Cursor cursor = managedQuery(uri, null, null, null, null);
				cursor.moveToFirst();        		        
				int wIndex = cursor.getColumnIndexOrThrow(SongbookDatabase.KEY_SONGCONT);		        
				  
				String Title = (cursor.getString(wIndex));    	
				String[] strings = TextUtils.split(Title, "#");
				String MyTitle = strings[0].trim();
				int id = Integer.parseInt(MyTitle);
				  
				Uri data = Uri.withAppendedPath(SongbookProvider.CONTENT_URI, String.valueOf(id));				
				Intent songIntent = new Intent(getApplicationContext(), SongViewRaw.class);
				songIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				songIntent.setData(data);
				startActivity(songIntent);
                
                return true;
                
            case R.id.favour:
            	Toast.makeText(this, R.string.sorry_text, Toast.LENGTH_LONG).show();
                return true;
                
            case R.id.favourites:
            	//Intent intent = new Intent(SongView.this, SongViewRaw.class);
    			//startActivity(intent);
            	Toast.makeText(this, R.string.sorry_text, Toast.LENGTH_LONG).show();
                return true;
                
            case R.id.complain:
            	Toast.makeText(this, R.string.sorry_text, Toast.LENGTH_LONG).show();
                return true;
                
            default:
                return false;
        }
    } 
    
    private void shareThisSong() {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);
        cursor.moveToFirst();        
        int wIndex = cursor.getColumnIndexOrThrow(SongbookDatabase.KEY_SONGCONT);
        int dIndex = cursor.getColumnIndexOrThrow(SongbookDatabase.KEY_SONG);
        String thesong = (cursor.getString(dIndex));
        String thetitle = (cursor.getString(wIndex));

		share.putExtra(Intent.EXTRA_SUBJECT, thetitle);
        share.putExtra(Intent.EXTRA_TEXT, thesong+"\n\nVia vSongBook Android App");
		startActivity(Intent.createChooser(share, "Share this Song"));
	}
    
}
