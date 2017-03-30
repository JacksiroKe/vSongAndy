package com.jackson_siro.visongbook.ownbooks;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	// database version
	private static final int database_VERSION = 1;
	// database name
	private static final String database_NAME = "SongDB";
	private static final String table_SONGS = "songs";
	private static final String song_ID = "id";
	private static final String song_TITLE = "title";
	private static final String song_CONTENT = "content";

	private static final String[] COLUMNS = { song_ID, song_TITLE, song_CONTENT };

	public SQLiteHelper(Context context) {
		super(context, database_NAME, null, database_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create song table
		String CREATE_SONG_TABLE = "CREATE TABLE songs ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT, " + "content TEXT )";
		db.execSQL(CREATE_SONG_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop songs table if already exists
		db.execSQL("DROP TABLE IF EXISTS songs");
		this.onCreate(db);
	}

	public void createSong(OwnSong song) {
		// get reference of the SongDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(song_TITLE, song.getTitle());
		values.put(song_CONTENT, song.getContent());

		// insert song
		db.insert(table_SONGS, null, values);

		// close database transaction
		db.close();
	}

	public OwnSong readSong(int id) {
		// get reference of the SongDB database
		SQLiteDatabase db = this.getReadableDatabase();

		// get song query
		Cursor cursor = db.query(table_SONGS, // a. table
				COLUMNS, " id = ?", new String[] { String.valueOf(id) }, null, null, null, null);

		// if results !=null, parse the first one
		if (cursor != null)
			cursor.moveToFirst();

		OwnSong song = new OwnSong();
		song.setId(Integer.parseInt(cursor.getString(0)));
		song.setTitle(cursor.getString(1));
		song.setContent(cursor.getString(2));

		return song;
	}

	public List<OwnSong> getAllSongs() {
		List<OwnSong> songs = new LinkedList<OwnSong>();

		// select song query
		String query = "SELECT  * FROM " + table_SONGS;

		// get reference of the SongDB database
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// parse all results
		OwnSong song = null;
		if (cursor.moveToFirst()) {
			do {
				song = new OwnSong();
				song.setId(Integer.parseInt(cursor.getString(0)));
				song.setTitle(cursor.getString(1));
				song.setContent(cursor.getString(2));

				// Add song to songs
				songs.add(song);
			} while (cursor.moveToNext());
		}
		return songs;
	}

	public int updateSong(OwnSong song) {

		// get reference of the SongDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put("title", song.getTitle()); // get title
		values.put("content", song.getContent()); // get content

		// update
		int i = db.update(table_SONGS, values, song_ID + " = ?", new String[] { String.valueOf(song.getId()) });

		db.close();
		return i;
	}

	// Deleting single song
	public void deleteSong(OwnSong song) {

		// get reference of the SongDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// delete song
		db.delete(table_SONGS, song_ID + " = ?", new String[] { String.valueOf(song.getId()) });
		db.close();
	}
}
