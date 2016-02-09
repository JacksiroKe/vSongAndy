package com.jackson_siro.vsongbook;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class SongbookProvider extends ContentProvider {
    String TAG = "SongbookProvider";

    public static String AUTHORITY = "com.jackson_siro.vsongbook.SongbookProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/vsongbook");

    public static final String SONGS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                                                  "/vnd.example.android.vsongbook";
    public static final String DEFINITION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                                                       "/vnd.example.android.vsongbook";

    private SongbookDatabase mSongbook;

    // UriMatcher stuff
    private static final int SEARCH_SONGS = 0;
    private static final int GET_SONG = 1;
    private static final int SEARCH_SUGGEST = 2;
    private static final int REFRESH_SHORTCUT = 3;
    private static final UriMatcher sURIMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        
        matcher.addURI(AUTHORITY, "vsongbook", SEARCH_SONGS);
        matcher.addURI(AUTHORITY, "vsongbook/#", GET_SONG);
        
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, REFRESH_SHORTCUT);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", REFRESH_SHORTCUT);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mSongbook = new SongbookDatabase(getContext());
        return true;
    }

    /**
     * Handles all the songbook searches and suggestion queries from the Search Manager.
     * When requesting a specific song, the uri alone is required.
     * When searching all of the songbook for matches, the selectionArgs argument must carry
     * the search query as the first element.
     * All other arguments are ignored.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        // Use the UriMatcher to see what kind of query we have and format the db query accordingly
        switch (sURIMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                if (selectionArgs == null) {
                  throw new IllegalArgumentException(
                      "selectionArgs must be provided for the Uri: " + uri);
                }
                return getSuggestions(selectionArgs[0]);
            case SEARCH_SONGS:
                if (selectionArgs == null) {
                  throw new IllegalArgumentException(
                      "selectionArgs must be provided for the Uri: " + uri);
                }
                return search(selectionArgs[0]);
            case GET_SONG:
                return getSong(uri);
            case REFRESH_SHORTCUT:
                return refreshShortcut(uri);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    private Cursor getSuggestions(String query) {
      query = query.toLowerCase();
      String[] columns = new String[] {
          BaseColumns._ID,
          SongbookDatabase.KEY_SONG,
          SongbookDatabase.KEY_SONGCONT,
       /* SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
                        (only if you want to refresh shortcuts) */
          SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID};

      return mSongbook.getSongMatches(query, columns);
    }

    private Cursor search(String query) {
      query = query.toLowerCase();
      String[] columns = new String[] {
          BaseColumns._ID,
          SongbookDatabase.KEY_SONG,
          SongbookDatabase.KEY_SONGCONT};

      return mSongbook.getSongMatches(query, columns);
    }

    private Cursor getSong(Uri uri) {
      String rowId = uri.getLastPathSegment();
      String[] columns = new String[] {
          SongbookDatabase.KEY_SONG,
          SongbookDatabase.KEY_SONGCONT};

      return mSongbook.getSong(rowId, columns);
    }

    private Cursor refreshShortcut(Uri uri) {
      /* This won't be called with the current implementation, but if we include
       * {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column in our suggestions table, we
       * could expect to receive refresh queries when a shortcutted suggestion is displayed in
       * Quick Search Box. In which case, this method will query the table for the specific
       * song, using the given item Uri and provide all the columns originally provided with the
       * suggestion query.
       */
      String rowId = uri.getLastPathSegment();
      String[] columns = new String[] {
          BaseColumns._ID,
          SongbookDatabase.KEY_SONG,
          SongbookDatabase.KEY_SONGCONT,
          SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
          SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID};

      return mSongbook.getSong(rowId, columns);
    }

    /**
     * This method is required in order to query the supported types.
     * It's also useful in our own query() method to determine the type of Uri received.
     */
    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case SEARCH_SONGS:
                return SONGS_MIME_TYPE;
            case GET_SONG:
                return DEFINITION_MIME_TYPE;
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            case REFRESH_SHORTCUT:
                return SearchManager.SHORTCUT_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    // Other required implementations...

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

}
