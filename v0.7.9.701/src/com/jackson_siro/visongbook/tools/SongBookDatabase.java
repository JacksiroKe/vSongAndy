package com.jackson_siro.visongbook.tools;

import java.util.HashMap;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

public class SongBookDatabase{
	
	private SongBookSQLite sbDB;
	
	public static final String TAG = "SongDatabase", DATABASE = "vSongBookUlt", S_TABLE = "as_mainsongs", B_TABLE = "as_books";
	public static final String B_ID = "bookid", B_NUMBER = "book_number", B_TITLE = "book_title", B_CONTENT = "book_content", B_CODE = "book_code", B_CREATED = "book_created", B_SCOUNT = "book_scount", B_ACTIVE = "book_active";
	public static final String S_ID = "_id", S_ICON = "song_icon", S_TITLE = "song_title", S_CONTENT = "song_content", S_KEY = "song_key", S_BOOK = "song_book", S_AUTHOR = "song_author", S_POSTED = "song_posted", S_UPDATED = "song_updated", S_UPDATEDBY = "song_updatedby", S_FAVOURED = "song_favour", S_TRASHED = "song_trash";
	
    public static final int VERSION = 1;

	private HashMap<String, String> mAliasMap;
	
	
	public SongBookDatabase(Context context){
		context.deleteDatabase("vsongbook");
    	context.deleteDatabase("visongbook");
    	context.deleteDatabase("VirtualSongbookI");
    	context.deleteDatabase("VirtualSongbookII");
    	context.deleteDatabase("VirtualSongbookIII");
    	context.deleteDatabase("vSongBook1");
    	context.deleteDatabase("vSongBook_One");
    	
    	sbDB = new SongBookSQLite(context, DATABASE, null, VERSION);
		
    	mAliasMap = new HashMap<String, String>();
    	mAliasMap.put("_ID", S_ID + " AS " + "_id" );
    	mAliasMap.put( SearchManager.SUGGEST_COLUMN_ICON_1, S_ICON + " AS " + SearchManager.SUGGEST_COLUMN_ICON_1);
    	mAliasMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1, S_TITLE + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
    	mAliasMap.put(SearchManager.SUGGEST_COLUMN_TEXT_2, S_CONTENT + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_2);
    	mAliasMap.put( SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, S_ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID );
		
	}
	
    public Cursor getSongs(String[] selectionArgs){	
    	String selection = S_TITLE + " LIKE ? ";
    	if(selectionArgs!=null)
    		selectionArgs[0] = "%"+selectionArgs[0] + "%";   		
    	
    	SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    	queryBuilder.setProjectionMap(mAliasMap);    	
    	queryBuilder.setTables(S_TABLE);    	
    	
    	Cursor c = queryBuilder.query(
			sbDB.getReadableDatabase(), 
			new String[] { "_ID", 
				SearchManager.SUGGEST_COLUMN_TEXT_1,
				SearchManager.SUGGEST_COLUMN_TEXT_2,  
				SearchManager.SUGGEST_COLUMN_ICON_1, 
				SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID 
			},
			selection, 
			selectionArgs, 
			null, 
			null,
			S_TITLE + " ASC ","10"
		);  	    	
	    return c;
	    
    }
	
    public Cursor getSong(String id){
    	
    	SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();    	
    	
    	queryBuilder.setTables(S_TABLE);    	
    	
    	Cursor c = queryBuilder.query(sbDB.getReadableDatabase(), 
    			new String[] { S_ID, S_ICON, S_TITLE, S_CONTENT } ,
				S_ID + " = ?", new String[] { id } , null, null, null ,"1"
			);  	
    	
    	return c;
    }

}