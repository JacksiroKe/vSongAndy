package com.jackson_siro.vsongbook;

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

public class SongbookDatabase {
    private static final String TAG = "SongbookDatabase";

    //The columns we'll include in the songbook table
    public static final String KEY_SONG = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String KEY_SONGCONT = SearchManager.SUGGEST_COLUMN_TEXT_2;

    private static final String DATABASE_NAME = "vsongbook";
    private static final String FTS_VIRTUAL_TABLE = "FTSsongbook";
    private static final int DATABASE_VERSION = 2;

    private final SongbookOpenHelper mDatabaseOpenHelper;
    private static final HashMap<String,String> mColumnMap = buildColumnMap();

    /**
     * Constructor
     * @param context The Context within which to work, used to create the DB
     */
    public SongbookDatabase(Context context) {
        mDatabaseOpenHelper = new SongbookOpenHelper(context);
    }

    /**
     * Builds a map for all columns that may be requested, which will be given to the 
     * SQLiteQueryBuilder. This is a good way to define aliases for column names, but must include 
     * all columns, even if the value is the key. This allows the ContentProvider to request
     * columns w/o the need to know real column names and create the alias itself.
     */
    private static HashMap<String,String> buildColumnMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(KEY_SONG, KEY_SONG);
        map.put(KEY_SONGCONT, KEY_SONGCONT);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }

    /**
     * Returns a Cursor positioned at the song specified by rowId
     *
     * @param rowId id of song to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching song, or null if not found.
     */
    public Cursor getSong(String rowId, String[] columns) {
        String selection = "rowid = ?";
        String[] selectionArgs = new String[] {rowId};

        return query(selection, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE rowid = <rowId>
         */
    }

    /**
     * Returns a Cursor over all songs that match the given query
     *
     * @param query The string to search for
     * @param columns The columns to include, if null then all are included
     * @return Cursor over all songs that match, or null if none found.
     */
    public Cursor getSongMatches(String query, String[] columns) {
        String selection = KEY_SONG + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE <KEY_SONG> MATCH 'query*'
         * which is an FTS3 search for the query text (plus a wildcard) inside the song column.
         *
         * - "rowid" is the unique id for all rows but we need this value for the "_id" column in
         *    order for the Adapters to work, so the columns need to make "_id" an alias for "rowid"
         * - "rowid" also needs to be used by the SUGGEST_COLUMN_INTENT_DATA alias in order
         *   for suggestions to carry the proper intent data.
         *   These aliases are defined in the SongbookProvider when queries are made.
         * - This can be revised to also search the songcont text with FTS3 by changing
         *   the selection clause to use FTS_VIRTUAL_TABLE instead of KEY_SONG (to search across
         *   the entire table, but sorting the relevance could be difficult.
         */
    }

    /**
     * Performs a database query.
     * @param selection The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @param columns The columns to return
     * @return A Cursor over all rows matching the query
     */
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        /* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);
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
    private static class SongbookOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        /* Note that FTS3 does not support column constraints and thus, you cannot
         * declare a primary key. However, "rowid" is automatically used as a unique
         * identifier, so when making requests, we will use "_id" as an alias for "rowid"
         */
        private static final String FTS_TABLE_CREATE =
                    "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                    " USING fts3 (" +
                    KEY_SONG + ", " +
                    KEY_SONGCONT + ");";

        SongbookOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            loadSongbook();
        }

        /**
         * Starts a thread to load the database table with songs
         */
        private void loadSongbook() {
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
            InputStream inputStream = resources.openRawResource(R.raw.songbook);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            
            try {
            	String line;
                String thesong;
                while ((line = reader.readLine()) != null) {
                	thesong = line.replace("$", System.getProperty("line.separator"));
                    String[] strings = TextUtils.split(thesong, "%");
                    if (strings.length < 2) continue;
                    long id = addSong(strings[0].trim(), strings[1].trim());
                    if (id < 0) {
                        Log.e(TAG, "unable to add song: " + strings[0].trim());
                    }
                }
            } finally {
                reader.close();
            }
            Log.d(TAG, "DONE loading songs.");
        }

        /**
         * Add a song to the songbook.
         * @return rowId or -1 if failed
         */
        public long addSong(String song, String songcont) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_SONG, songcont);
            initialValues.put(KEY_SONGCONT, song);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }

}
