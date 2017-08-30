package com.jackson_siro.visongbook.songbooks;

import java.util.Calendar;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.ownbooks.Favourites;
import com.jackson_siro.visongbook.ownbooks.OwnSongList;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class Searchable extends ActionBarActivity {

    private TextView mTextView;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.katiba_search);
        
        mTextView = (TextView) findViewById(R.id.katitext);
        mListView = (ListView) findViewById(R.id.katilist);
        
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
            Intent wordIntent = new Intent(this, SongBookView.class);
            wordIntent.setData(intent.getData());
            startActivity(wordIntent);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }
    

    public void ActivateApp(View view) {
    	
        

    }

    private void showResults(String query) {
    	SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
      	
    	if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_is_paid", false))
        {            
    		Calendar rightNow = Calendar.getInstance();
    	    int CurHour = rightNow.get(Calendar.HOUR_OF_DAY);
    	    
    		if (query.contains("JCSR"))
            {
                String[] CodeHr = query.split("JCSR");
                int CdHour = Integer.parseInt(CodeHr[1]);
                
                if ((CurHour - CdHour) < 3)
                {
                	editor.putBoolean("js_vsb_is_paid", true);
    		  		editor.commit();
    		  		//YouMustJustPay(1);
    		  		Toast.makeText(this, "vSongBook is Now Premium!", Toast.LENGTH_SHORT).show();
                }
                //else YouMustJustPay(2);
            }
        }
    	
        @SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(SongBookProvider.CONTENT_URI, null, null,
                                new String[] {query}, null);

        if (cursor == null) {
            // There are no results
            mTextView.setText(getString(R.string.no_results, new Object[] {query}));
        } else {
            // Display the number of results
            int count = cursor.getCount();
            String countString = getResources().getQuantityString(R.plurals.search_results,
                                    count, new Object[] {count, query});
            mTextView.setText(countString);

            //ResultList mAdapter = new ResultList(this, cursor);
            //mListView.setAdapter(mAdapter); 
            
            String[] from = new String[] { SongBookDatabase.SONG_TITLE, SongBookDatabase.SONG_CONTENT };

            // Specify the corresponding layout elements where we want the columns to go
            int[] to = new int[] { R.id.katiname,R.id.katicont };

            // Create a simple cursor adapter for the definitions and apply them to the ListView
            SimpleCursorAdapter words = new SimpleCursorAdapter(this, R.layout.song_result, cursor, from, to);
            mListView.setAdapter(words);
            
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open WordActivity with a specific word Uri
                    Intent wordIntent = new Intent(getApplicationContext(), SongBookView.class);
                    Uri data = Uri.withAppendedPath(SongBookProvider.CONTENT_URI, String.valueOf(id));
                    wordIntent.setData(data);
                    startActivity(wordIntent);
                }
            });
        }
    }

    public void YouMustJustPay(int showthis) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	if (showthis==1) {
    		builder.setTitle("App Unlocked!");
    		builder.setMessage("God bless you. vSongBook Application on your device has been unlocked. It is now Premium. If you loose this app in anyway and install again please do not hesitate to contact the developer Bro. Jack Siro for an Activation Code via SMS on +254711474787 or Email on smataweb@gmail.com. God bless you.");
    	}
    	else if (showthis==2) {
    		builder.setTitle("Error in the code!");
    		builder.setMessage("God bless you. Unfortunately vSongBook Application on your device can not be unlocked. The code you enterered is invalid or has expired_i. Please try again or request for another from the developer Bro. Jack Siro for help via SMS on +254711474787 or Email on smataweb@gmail.com. God bless you.");
    	}
    	else if (showthis==3) {
    		builder.setTitle("Error in the code!");
    		builder.setMessage("God bless you. Unfortunately vSongBook Application on your device can not be unlocked. The code you enterered is invalid or has expired_i. Please try again or request for another from the developer Bro. Jack Siro for help via SMS on +254711474787 or Email on smataweb@gmail.com. God bless you.");
    	}
        builder.setPositiveButton("Okay, Got It", new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
								
			}
		});
    	
        builder.show();
        finish();
	  }
    
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: 
                onSearchRequested();
                return true;
            case R.id.favours:
                startActivity(new Intent(this, Favourites.class));
                return true;
            case R.id.notepad:
                startActivity(new Intent(this, OwnSongList.class));
                return true;
            case R.id.about:
                startActivity(new Intent(this, EeAbout.class));
                return true;
            case R.id.tutorial:
            	startActivity(new Intent(this, BbDemo.class));
                return true;
            case R.id.helpdesk:
                startActivity(new Intent(this, EeHelpDesk.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            default:
                return false;
        }
    }
    
}