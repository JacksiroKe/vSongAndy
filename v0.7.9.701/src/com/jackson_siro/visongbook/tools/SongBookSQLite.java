package com.jackson_siro.visongbook.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class SongBookSQLite extends SQLiteOpenHelper{

	public static final String TAG = "SongDatabase", DATABASE = "vSongBookUlt";
	public static final String M_TABLE = "as_minesongs",B_TABLE = "as_books", B_ID = "bookid", B_NUMBER = "book_number", B_TITLE = "book_title", B_CONTENT = "book_content", B_CODE = "book_code", B_CREATED = "book_created", B_SCOUNT = "book_scount", B_ACTIVE = "book_active";
	public static final String S_TABLE = "as_mainsongs", S_ID = "_id", S_TITLE = "song_title", S_CONTENT = "song_content", S_KEY = "song_key", S_BOOK = "song_book", S_AUTHOR = "song_author", S_POSTED = "song_posted", S_UPDATED = "song_updated", S_UPDATEDBY = "song_updatedby", S_FAVOURED = "song_favour", S_TRASHED = "song_trash", S_TYPE = "song_type", S_STATUS = "song_status", S_ICON = "song_icon";
	public static final String O_TABLE = "as_options", O_ID = "optionid", O_TITLE = "option_title", O_CONTENT = "option_content", O_UPDATED = "option_updated";
	
	public static final String[] B_COLUMNS = { B_ID, B_NUMBER, B_TITLE, B_CONTENT, B_CODE, B_CREATED, B_SCOUNT };
	public static final String[] S_COLUMNS = { S_ID, S_BOOK, S_TITLE, S_CONTENT, S_KEY, S_POSTED, S_AUTHOR, S_UPDATED, S_UPDATEDBY, S_FAVOURED, S_TRASHED};
	public static final String[] M_COLUMNS = { S_ID, S_TITLE, S_CONTENT, S_KEY, S_POSTED, S_UPDATED, S_TYPE, S_STATUS};
	public static final String[] O_COLUMNS = { O_ID, O_TITLE, O_CONTENT, O_UPDATED };
	
	public static final int VERSION = 1;
    
	public SongBookSQLite(Context context, String name, CursorFactory factory, int version ) {
		super(context, DATABASE, factory, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + B_TABLE + "(" + B_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + B_NUMBER + " INTEGER, " + B_TITLE + " VARCHAR, " + B_CONTENT + " VARCHAR, " + B_CODE + " VARCHAR, " + B_CREATED + " VARCHAR, " + B_SCOUNT + " INTEGER)");
		db.execSQL("CREATE TABLE " + M_TABLE + "(" + S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + S_TITLE + " VARCHAR, " + S_CONTENT + " VARCHAR, " + S_KEY + " VARCHAR, " + S_POSTED + " VARCHAR, " + S_UPDATED + " VARCHAR, " + S_TYPE + " VARCHAR, " + S_STATUS + " INTEGER, " + S_ICON + " VARCHAR)");
		db.execSQL("CREATE TABLE " + O_TABLE + "(" + O_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + O_TITLE + " VARCHAR, " + O_CONTENT + " VARCHAR, " + O_UPDATED + " VARCHAR)");
		db.execSQL("CREATE TABLE " + S_TABLE + "(" + S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + S_BOOK + " INTEGER, " + S_TITLE + " VARCHAR, " + S_CONTENT + " VARCHAR, " + S_KEY + " VARCHAR, " + S_POSTED + " VARCHAR, " + S_AUTHOR + " INTEGER, " + S_UPDATED + " VARCHAR, " + S_UPDATEDBY + " INTEGER, "+ S_FAVOURED + " INTEGER, " + S_TRASHED + " INTEGER, " + S_ICON + " VARCHAR)");
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + B_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + M_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + O_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + S_TABLE);
		
		this.onCreate(db);
	}

	public void createSong(SongItem songitem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(S_BOOK, songitem.getBook()); 
		values.put(S_TITLE, songitem.getTitle());
		values.put(S_CONTENT, songitem.getContent()); 
		values.put(S_KEY, songitem.getKey());
		values.put(S_POSTED, songitem.getPosted());
		values.put(S_AUTHOR, songitem.getAuthor());
		
		db.insert(S_TABLE, null, values);
		db.close();
	}

	public List<SongItem> getAllSongs(int SONGBOOK) {
		List<SongItem> songitems = new LinkedList<SongItem>();
		String query = "SELECT * FROM " + S_TABLE + " WHERE " + S_BOOK + "=" + SONGBOOK;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		SongItem songitem = null;
		if (cursor.moveToFirst()) {
			do {
				songitem = new SongItem();
				songitem.setSongid(Integer.parseInt(cursor.getString(0)));
				songitem.setTitle(convertTexts(cursor.getString(2)));
				String songitemCont = convertTexts(cursor.getString(3));
				if (songitemCont.length() > 50)
					songitemCont =songitemCont.substring(0, 50);
				songitem.setContent(songitemCont);
				songitems.add(songitem);
			} while (cursor.moveToNext());
		}
		db.close();
		return songitems;
	}

	public List<SongItem> getFavouriteSongs() {
		List<SongItem> songitems = new LinkedList<SongItem>();
		String query = "SELECT * FROM " + S_TABLE + " WHERE " + S_FAVOURED + "=1";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		SongItem songitem = null;
		if (cursor.moveToFirst()) {
			do {
				songitem = new SongItem();
				songitem.setSongid(Integer.parseInt(cursor.getString(0)));
				songitem.setTitle(convertTexts(cursor.getString(2)));
				String songitemCont = convertTexts(cursor.getString(3));
				if (songitemCont.length() > 50)
					songitemCont =songitemCont.substring(0, 50);
				songitem.setContent(songitemCont);
				songitems.add(songitem);
			} while (cursor.moveToNext());
		}
		db.close();
		return songitems;
	}

	public SongItem readSong(int songid) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(S_TABLE, // a. table
				S_COLUMNS, S_ID + "=?", new String[] { String.valueOf(songid) }, null, null, null, null);

		// if results !=null, parse the first one
		if (cursor != null)
			cursor.moveToFirst();

		SongItem songitem = new SongItem();

		songitem.setSongid(Integer.parseInt(cursor.getString(0)));
		songitem.setBook(cursor.getString(1));
		songitem.setTitle(convertTexts(cursor.getString(2)));
		songitem.setContent(convertContent(cursor.getString(3)));		
		songitem.setKey(cursor.getString(4));
		songitem.setPosted(cursor.getString(5));
		songitem.setAuthor(cursor.getString(6));

		db.close();
		return songitem;
	}
	
	public String convertTexts(String thistext){
		return thistext.replace("#", ".").replace(" ` ", " ").replace("$", " ").replace("^", "'");
	}
	
	public String convertContent(String thistext){
		return thistext.replace("$", "\n").replace("+", "\"");
	}

	public int updateSong(SongItem songitem) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(S_BOOK, songitem.getBook());
		values.put(S_TITLE, songitem.getTitle());
		values.put(S_CONTENT, songitem.getContent());
		values.put(S_UPDATED,  CurrentDateTime()); 
		values.put(S_UPDATEDBY, songitem.getUpdatedby());
		
		int i = db.update(S_TABLE, values, S_ID + " = ?", new String[] { String.valueOf(songitem.getSongid()) });
		db.close();
		return i;
	}

	public int favoriteSong(SongItem songitem) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues(); 
		values.put(S_FAVOURED, 1);

		int i = db.update(S_TABLE, values, S_ID + " = ?", new String[] { String.valueOf(songitem.getSongid()) });
		db.close();
		return i;
	}
	public int clearSong(SongItem songitem) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues(); 
		values.put(S_FAVOURED, 0);

		// update
		int i = db.update(S_TABLE, values, S_ID + " = ?", new String[] { String.valueOf(songitem.getSongid()) });

		db.close();
		return i;
	}
	public int trashSong(SongItem songitem) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues(); 
		values.put(S_TRASHED, 1);

		// update
		int i = db.update(S_TABLE, values, S_ID + " = ?", new String[] { String.valueOf(songitem.getSongid()) });

		db.close();
		return i;
	}
	
	public int restoreSong(SongItem songitem) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues(); 
		values.put(S_TRASHED, 0);
		int i = db.update(S_TABLE, values, S_ID + " = ?", new String[] { String.valueOf(songitem.getSongid()) });
		db.close();
		return i;
	}
	
	public void updateSong(SongMine songitem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(S_TITLE, songitem.getTitle());
		values.put(S_CONTENT, songitem.getContent()); 
		values.put(S_KEY, songitem.getKey());
		values.put(S_POSTED,  CurrentDateTime()); 
		values.put(S_TYPE, songitem.getType()); 
		values.put(S_STATUS, songitem.getStatus());
		
		db.insert(M_TABLE, null, values);
		db.close();
	}

	public void createMySong(SongMine songitem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(S_TITLE, songitem.getTitle());
		values.put(S_CONTENT, songitem.getContent()); 
		values.put(S_KEY, songitem.getKey());
		values.put(S_POSTED,  CurrentDateTime());
		values.put(S_TYPE, songitem.getType()); 
		
		db.insert(M_TABLE, null, values);
		db.close();
	}

	public List<SongMine> getAllMySongs() {
		List<SongMine> songitems = new LinkedList<SongMine>();
		String query = "SELECT * FROM " + M_TABLE + " WHERE " + S_TYPE + "='O'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		SongMine songitem = null;
		if (cursor.moveToFirst()) {
			do {
				songitem = new SongMine();
				songitem.setSongid(Integer.parseInt(cursor.getString(0)));
				songitem.setTitle(convertTexts(cursor.getString(1)));
				String songitemCont = convertTexts(cursor.getString(2));
				if (songitemCont.length() > 50)
					songitemCont =songitemCont.substring(0, 50);
				songitem.setContent(songitemCont);
				songitems.add(songitem);
			} while (cursor.moveToNext());
		}
		db.close();
		return songitems;
	}

	public List<SongMine> getAllEdittedSongs() {
		List<SongMine> songitems = new LinkedList<SongMine>();
		String query = "SELECT * FROM " + M_TABLE + " WHERE " + S_TYPE + "='M'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		SongMine songitem = null;
		if (cursor.moveToFirst()) {
			do {
				songitem = new SongMine();
				songitem.setSongid(Integer.parseInt(cursor.getString(0)));
				songitem.setTitle(convertTexts(cursor.getString(1)));
				String songitemCont = convertTexts(cursor.getString(2));
				if (songitemCont.length() > 50)
					songitemCont =songitemCont.substring(0, 50);
				songitem.setContent(songitemCont);
				songitems.add(songitem);
			} while (cursor.moveToNext());
		}
		db.close();
		return songitems;
	}

	public SongMine readMySong(int songid) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(M_TABLE, // a. table
				M_COLUMNS, S_ID + "=?", new String[] { String.valueOf(songid) }, null, null, null, null);

		// if results !=null, parse the first one
		if (cursor != null)
			cursor.moveToFirst();

		SongMine songitem = new SongMine();
		
		songitem.setSongid(Integer.parseInt(cursor.getString(0)));
		songitem.setTitle(convertTexts(cursor.getString(1)));
		songitem.setContent(convertContent(cursor.getString(2)));		
		songitem.setKey(cursor.getString(3));
		songitem.setPosted(cursor.getString(4));

		db.close();
		return songitem;
	}
	
	public int updateMySong(SongMine songitem) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(S_TITLE, songitem.getTitle());
		values.put(S_KEY, songitem.getKey());
		values.put(S_CONTENT, songitem.getContent());
		values.put(S_UPDATED,  CurrentDateTime());
		int i = db.update(M_TABLE, values, S_ID + " = ?", new String[] { String.valueOf(songitem.getSongid()) });
		db.close();
		return i;
	}

	public void deleteSong(SongMine songitem) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(M_TABLE, S_ID + " = ?", new String[] { String.valueOf(songitem.getSongid()) });
		db.close();
	}

	public void newSongBook(SongBook songbook) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(B_NUMBER, songbook.getNumber()); 
		values.put(B_TITLE, songbook.getTitle());
		values.put(B_CONTENT, songbook.getContent()); 
		values.put(B_CODE, songbook.getCode());
		values.put(B_CREATED, songbook.getCreated());
		values.put(B_SCOUNT, songbook.getScount()); 
		
		db.insert(B_TABLE, null, values);
		db.close();
	}

	public SongBook readSongBook(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(B_TABLE, // a. table
				B_COLUMNS, B_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		SongBook songbook = new SongBook();
		songbook.setBookid(Integer.parseInt(cursor.getString(0)));
		songbook.setNumber(cursor.getString(1));
		songbook.setTitle(cursor.getString(2));
		songbook.setContent(cursor.getString(3));
		songbook.setCode(cursor.getString(4));
		songbook.setCreated(cursor.getString(5));
		songbook.setScount(cursor.getString(6));

		db.close();
		return songbook;
	}
	
	public SongBook getSongbookName(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(B_TABLE, // a. table
				B_COLUMNS, B_NUMBER + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		SongBook songbook = new SongBook();
		songbook.setTitle(cursor.getString(2) + " (" + cursor.getString(4) + ")");

		db.close();
		return songbook;
	}
	
	public List<SongBook> getAllSongBooks() {
		List<SongBook> songbooks = new LinkedList<SongBook>();
		String query = "SELECT  * FROM " + B_TABLE + "  ORDER BY " + B_NUMBER + " ASC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		SongBook songbook = null;
		if (cursor.moveToFirst()) {
			do {
				songbook = new SongBook();
				songbook.setBookid(Integer.parseInt(cursor.getString(0)));
				songbook.setNumber(cursor.getString(1));
				songbook.setTitle(cursor.getString(2));
				
				songbook.setContent(cursor.getString(3));
				songbook.setCode(cursor.getString(4));
				songbook.setCreated(cursor.getString(5));
				songbook.setScount(cursor.getString(6));
				
				// Add songbook to songbooks
				songbooks.add(songbook);
			} while (cursor.moveToNext());
		}
		db.close();
		return songbooks;
	}
	
	public int updateBook(SongBook songbook) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(B_NUMBER, songbook.getNumber()); 
		values.put(B_TITLE, songbook.getTitle());
		values.put(B_CONTENT, songbook.getContent()); 
		values.put(B_CODE, songbook.getCode());
		values.put(B_CREATED, songbook.getCreated());
		values.put(B_SCOUNT, songbook.getScount());
		
		// update
		int i = db.update(B_TABLE, values, B_ID + " = ?", new String[] { String.valueOf(songbook.getBookid()) });

		db.close();
		return i;
	}

	public int songitemAdded(SongBook songbook) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(B_SCOUNT, songbook.getScount());
		
		int i = db.update(B_TABLE, values, B_ID + " = ?", new String[] { String.valueOf(songbook.getBookid()) });

		db.close();
		return i;
	}

	public int songitemDelete(SongBook songbook) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(B_SCOUNT, songbook.getScount());
		
		int i = db.update(B_TABLE, values, B_ID + " = ?", new String[] { String.valueOf(songbook.getBookid()) });

		db.close();
		return i;
	}

	public void deleteBook(SongBook songbook) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(B_TABLE, B_ID + " = ?", new String[] { String.valueOf(songbook.getBookid()) });
		db.close();
	}

	//OPTIONS MANAGEMENT
	public void createOption(MyOption option) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(O_TITLE, option.getTitle());
		values.put(O_CONTENT, option.getContent());
		db.insert(O_TABLE, null, values);
		db.close();
	}

	public MyOption readOption(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(O_TABLE,  O_COLUMNS, O_ID + " = ?", 
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		MyOption option = new MyOption();
		option.setOptionid(Integer.parseInt(cursor.getString(0)));
		option.setTitle(cursor.getString(1));
		option.setContent(cursor.getString(2));

		db.close();
		return option;
	}
/*
	public String getOption(String title) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(O_TABLE, O_COLUMNS, O_TITLE + " = ?", 
				new String[] { title }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		MyOption option = new MyOption();
		option.setContent(cursor.getString(2));
		
		return cursor.getString(2);
	}
*/
	public String getOption(String title) {
		String query = "SELECT  * FROM " + O_TABLE + " WHERE " + O_TITLE + "='" + title + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		if (cursor != null) {
			cursor.moveToFirst();
			db.close();
			return cursor.getString(2);
		} else {
			return "";
		}
	}
	
	public void updateOption(String opt_title, String opt_value) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(O_TITLE, opt_title);
		values.put(O_CONTENT, opt_value);
		values.put(O_UPDATED, CurrentDateTime());
		db.update(O_TABLE, values, O_TITLE + " = ?", new String[] { opt_title });
		db.close();
	}
	
	@SuppressLint("SimpleDateFormat")
	public String CurrentDateTime(){
		long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'yyyy");  
        String datetimenow = formatter.format(curDateTime);
        return datetimenow; 
	}

	public void deleteSongbooks(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(B_TABLE, null, null);
		db.close();
	}

	public void deleteMainSongs(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(S_TABLE, null, null);
		db.close();
	}

	public void deleteMySongs(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(M_TABLE, null, null);
		db.close();
	}

}
