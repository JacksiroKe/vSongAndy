package com.jackson_siro.visongbook.songbooks;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.jackson_siro.visongbook.*;

public class SongBookDatabase {
    private static final String TAG = "SongDatabase";
    		
    public static final String SONG_ICON = SearchManager.SUGGEST_COLUMN_ICON_1;
    public static final String SONG_TITLE = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String SONG_CONTENT = SearchManager.SUGGEST_COLUMN_TEXT_2;

    private static final String DATABASE_NAME = "vSongBook_One";
    private static final String DATABASE_TABLE = "mysongbook";
    private static final int DATABASE_VERSION = 2;

    private final SongBookOpenHelper mDatabaseOpenHelper;
    private static final HashMap<String,String> mColumnMap = buildColumnMap();

    
    public SongBookDatabase(Context context) {
    	context.deleteDatabase("vsongbook");
    	context.deleteDatabase("visongbook");
    	context.deleteDatabase("VirtualSongbookI");
    	context.deleteDatabase("VirtualSongbookII");
    	context.deleteDatabase("VirtualSongbookIII");
    	context.deleteDatabase("vSongBook1");
    	
    	mDatabaseOpenHelper = new SongBookOpenHelper(context);
    }

    private static HashMap<String,String> buildColumnMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(SONG_ICON, SONG_ICON);
        map.put(SONG_TITLE, SONG_TITLE);
        map.put(SONG_CONTENT, SONG_CONTENT);
        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
    	
    	map.put( SearchManager.SUGGEST_COLUMN_ICON_1, "rowid  as " + SearchManager.SUGGEST_COLUMN_ICON_1);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " + SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
    	return map;
    }

    public Cursor getSong(String rowId, String[] columns) {
        String selection = "rowid = ?";
        String[] selectionArgs = new String[] {rowId};

        return query(selection, selectionArgs, columns);

    }

    public Cursor getSongMatches(String query, String[] columns) {
        String selection = SONG_CONTENT + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);

    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DATABASE_TABLE);
        builder.setProjectionMap(mColumnMap);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }


    /**
     * This creates/opens the database.
     */
    private static class SongBookOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        /* Note that FTS3 does not support column constraints and thus, you cannot
         * declare a primary key. However, "rowid" is automatically used as a unique
         * identifier, so when making requests, we will use "_id" as an alias for "rowid"
         */
        private static final String FTS_TABLE_CREATE =  "CREATE VIRTUAL TABLE " + DATABASE_TABLE +
                    " USING fts3 (" + SONG_ICON + ", " + SONG_TITLE + ", " + SONG_CONTENT + ");";

        SongBookOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            loadDictionary();
        }

        /**
         * Starts a thread to load the database table with words
         */
        private void loadDictionary() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadSongs();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void loadSongs() throws IOException {
        	Log.d(TAG, "Loading songs...");
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.vsongbook);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            
            try {
            	String line;
                String thesong;
                while ((line = reader.readLine()) != null) {
                	thesong = line.replace("$", System.getProperty("line.separator"));
                	String[] strings = TextUtils.split(thesong, "%");
                    if (strings.length < 2) continue;
                    long id = addSong(strings[0].trim(), strings[1].trim(), strings[2].trim());
                    if (id < 0) {
                        Log.e(TAG, "unable to add song: " + strings[0].trim());
                    }
                }
            } finally {
                reader.close();
            }
            Log.d(TAG, "DONE loading songs.");
        }


        public long addSong(String title, String content, String icon) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(SONG_ICON, icon);
            initialValues.put(SONG_TITLE, title);
            initialValues.put(SONG_CONTENT, content);

            return mDatabase.insert(DATABASE_TABLE, null, initialValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

}
