package com.jackson_siro.vsongbook;

import com.jackson_siro.vsongbook.notepad.*;

import android.support.v7.app.ActionBarActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class SongSearch extends ActionBarActivity {

    private TextView mTextView;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        handleIntent(getIntent());
        
        mTextView = (TextView) findViewById(R.id.text);
        mListView = (ListView) findViewById(R.id.list);

        Cursor cursor = managedQuery(SongbookProvider .CONTENT_URI, null, null,
                new String[] {"Songs of Worship"}, null);
	
		String[] from = new String[] { SongbookDatabase .KEY_SONGCONT, 
									SongbookDatabase .KEY_SONG };
		mTextView.setText(R.string.worship_songs);
		int[] to = new int[] { R.id.song, R.id.songcont };
		
		SimpleCursorAdapter songs = new SimpleCursorAdapter(this,
		       R.layout.result, cursor, from, to);
		mListView.setAdapter(songs);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent songIntent = new Intent(getApplicationContext(), SongView.class);
			Uri data = Uri.withAppendedPath(SongbookProvider .CONTENT_URI,
			                     String.valueOf(id));
			songIntent.setData(data);
			startActivity(songIntent);
			
			}
		});
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show song
            Intent songIntent = new Intent(this, SongView.class);
            songIntent.setData(intent.getData());
            startActivity(songIntent);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }

    /**
     * Searches the songbook and displays results for the given query.
     * @param query The search query
     */
    private void showResults(String query) {

        Cursor cursor = managedQuery(SongbookProvider.CONTENT_URI, null, null,
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

            // Specify the columns we want to display in the result
            String[] from = new String[] { SongbookDatabase.KEY_SONGCONT,
                                           SongbookDatabase.KEY_SONG };

            // Specify the corresponding layout elements where we want the columns to go
            int[] to = new int[] { R.id.song,
                                   R.id.songcont };

            // Create a simple cursor adapter for the contents and apply them to the ListView
            SimpleCursorAdapter songs = new SimpleCursorAdapter(this,
                                          R.layout.result, cursor, from, to);
            mListView.setAdapter(songs);

            // Define the on-click listener for the list items
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open SongActivity with a specific song Uri
                    Intent songIntent = new Intent(getApplicationContext(), SongView.class);
                    Uri data = Uri.withAppendedPath(SongbookProvider.CONTENT_URI,                                               String.valueOf(id));
                    songIntent.setData(data);
                    startActivity(songIntent);
                }
            });
        }
    }
   
    @Override
 	public void onBackPressed() {
    	 finish();
 	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.song_search, menu);
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
                
            case R.id.notepad:
                Intent intent = new Intent(this, OwnSongList.class);
 				startActivity(intent);
                return true;
                
            case R.id.tutorial:
                Intent intent1 = new Intent(this, AaaDemo.class);
 				startActivity(intent1);
                return true;
                
            case R.id.about:
                Intent intent2 = new Intent(this, About.class);
 				startActivity(intent2);
                return true;
                
            case R.id.helpdesk:
                Intent intent3 = new Intent(this, HelpDesk.class);
 				startActivity(intent3);
                return true;
                
            case R.id.settings:
                Intent intent4 = new Intent(this, SetApp.class);
 				startActivity(intent4);
                return true;
            
            default:
                return false;
        }
    } 
}
