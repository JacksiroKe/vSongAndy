package com.jackson_siro.visongbook.ownbooks;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelperF extends SQLiteOpenHelper {

	// database version
	private static final int database_VERSION = 1;
	// database name
	private static final String database_NAME = "FavourDB";
	private static final String table_FAVOURS = "favourites";
	private static final String favour_ID = "id";
	private static final String favour_TITLE = "title";
	private static final String favour_CONTENT = "content";

	private static final String[] COLUMNS = { favour_ID, favour_TITLE, favour_CONTENT };

	public SQLiteHelperF(Context context) {
		super(context, database_NAME, null, database_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create favour table
		String CREATE_FAVOUR_TABLE = "CREATE TABLE favourites ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT, " + "content TEXT )";
		db.execSQL(CREATE_FAVOUR_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop favourites table if already exists
		db.execSQL("DROP TABLE IF EXISTS favourites");
		this.onCreate(db);
	}

	public void createFavour(Favour favour) {
		// get reference of the FavourDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(favour_TITLE, favour.getTitle());
		values.put(favour_CONTENT, favour.getContent());

		// insert favour
		db.insert(table_FAVOURS, null, values);

		// close database transaction
		db.close();
	}

	public Favour readFavour(int id) {
		// get reference of the FavourDB database
		SQLiteDatabase db = this.getReadableDatabase();

		// get favour query
		Cursor cursor = db.query(table_FAVOURS, // a. table
				COLUMNS, " id = ?", new String[] { String.valueOf(id) }, null, null, null, null);

		// if results !=null, parse the first one
		if (cursor != null)
			cursor.moveToFirst();

		Favour favour = new Favour();
		favour.setId(Integer.parseInt(cursor.getString(0)));
		favour.setTitle(cursor.getString(1));
		favour.setContent(cursor.getString(2));

		return favour;
	}

	public List<Favour> getAllFavours() {
		List<Favour> favourites = new LinkedList<Favour>();

		// select favour query
		String query = "SELECT  * FROM " + table_FAVOURS;

		// get reference of the FavourDB database
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// parse all results
		Favour favour = null;
		if (cursor.moveToFirst()) {
			do {
				favour = new Favour();
				favour.setId(Integer.parseInt(cursor.getString(0)));
				favour.setTitle(cursor.getString(1));
				favour.setContent(cursor.getString(2));

				// Add favour to favourites
				favourites.add(favour);
			} while (cursor.moveToNext());
		}
		return favourites;
	}

	public int updateFavour(Favour favour) {

		// get reference of the FavourDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put("title", favour.getTitle()); // get title
		values.put("content", favour.getContent()); // get content

		// update
		int i = db.update(table_FAVOURS, values, favour_ID + " = ?", new String[] { String.valueOf(favour.getId()) });

		db.close();
		return i;
	}

	// Deleting single favour
	public void deleteFavour(Favour favour) {

		// get reference of the FavourDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// delete favour
		db.delete(table_FAVOURS, favour_ID + " = ?", new String[] { String.valueOf(favour.getId()) });
		db.close();
	}
}
