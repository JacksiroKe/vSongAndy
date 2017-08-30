package com.jackson_siro.visongbook.songbooks;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.tools.*;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class Searchable extends AppFunctions {

    private TextView mTextView;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchable);
        
        mTextView = (TextView) findViewById(R.id.katitext);
        mListView = (ListView) findViewById(R.id.katilist);
        
        handleIntent(getIntent());
        changeStatusBarColor();	
	}

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
            Intent wordIntent = new Intent(this, SongView.class);
            wordIntent.setData(intent.getData());
            startActivity(wordIntent);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            //showResults(query);
            QuickActivate(query);
        }
    }
    
    private void showResults(String query) {
    	QuickActivate(query);
    	Cursor cursor = managedQuery(SongBookProvider.CONTENT_URI, null, null, new String[] {query}, null);

        if (cursor == null)
        	mTextView.setText(getString(R.string.no_results, new Object[] {query}));
        else {
            int count = cursor.getCount();
            String countString = getResources().getQuantityString(R.plurals.search_results, count, new Object[] {count, query});
            mTextView.setText(countString);
            String[] from = new String[] { SongBookDatabase.S_TITLE, SongBookDatabase.S_CONTENT };

            int[] to = new int[] { R.id.katiname,R.id.katicont };
            SimpleCursorAdapter words = new SimpleCursorAdapter(this, R.layout.song_result, cursor, from, to);
            mListView.setAdapter(words);
            
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent wordIntent = new Intent(getApplicationContext(), SongView.class);
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
        getMenuInflater().inflate(R.menu.searchable, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: 
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }
    
}