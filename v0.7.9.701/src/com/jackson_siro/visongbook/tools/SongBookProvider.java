package com.jackson_siro.visongbook.tools;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class SongBookProvider extends ContentProvider {
	SongBookDatabase mSongBookDB = null;
	public static final String AUTHORITY = "com.jackson_siro.visongbook.tools.SongBookProvider";
	public static final String databasestr = SongBookDatabase.DATABASE;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + databasestr );
	private static final int SUGGESTIONS_SONG = 1, SEARCH_SONG = 2, GET_SONG = 3;
	UriMatcher mUriMatcher = buildUriMatcher();
	 
    private UriMatcher buildUriMatcher(){
    	 UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);    	 
    	 
    	 // Suggestion items of Search Dialog is provided by this uri
    	 uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,SUGGESTIONS_SONG);
    	 
    	 // This URI is invoked, when user presses "Go" in the Keyboard of Search Dialog
    	 // Listview items of SearchableActivity is provided by this uri    	 
    	 // See android:searchSuggestIntentData="content://in.wptrafficanalyzer.searchdialogdemo.provider/countries" of searchable.xml
    	 uriMatcher.addURI(AUTHORITY, databasestr, SEARCH_SONG);
    	 
    	 // This URI is invoked, when user selects a suggestion from search dialog or an item from the listview
    	 // Song details for SongActivity is provided by this uri    	 
    	 // See, SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID in SongDB.java
    	 uriMatcher.addURI(AUTHORITY, databasestr + "/#", GET_SONG);
    	 
    	 return uriMatcher;
     }
     
     
     @Override
	 public boolean onCreate() {
		 	mSongBookDB = new SongBookDatabase(getContext());
		 	return true;
	 }
     
     @Override
	 public Cursor query(Uri uri, String[] projection, String selection,
			 String[] selectionArgs, String sortOrder) {
    	 
    	 Cursor c = null;
    	 switch(mUriMatcher.match(uri)){
    	 case SUGGESTIONS_SONG :
    		 c = mSongBookDB.getSongs(selectionArgs);
    		 break;
    	 case SEARCH_SONG :
    		 c = mSongBookDB.getSongs(selectionArgs);
    		 break;
    	 case GET_SONG :
    		 String id = uri.getLastPathSegment();
    		 c = mSongBookDB.getSong(id);    		
    	 }

    	 return c;
    	 
	}     

	 @Override
	 public int delete(Uri uri, String selection, String[] selectionArgs) {
		 	throw new UnsupportedOperationException();
	 }

	 @Override
	 public String getType(Uri uri) {
		 	throw new UnsupportedOperationException();
	 }

	 @Override
	 public Uri insert(Uri uri, ContentValues values) {
		 	throw new UnsupportedOperationException();
	 }	 
	 

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
	 		throw new UnsupportedOperationException();
	}
}