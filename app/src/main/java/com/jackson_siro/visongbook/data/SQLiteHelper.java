package com.jackson_siro.visongbook.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.jackson_siro.visongbook.models.*;
import com.jackson_siro.visongbook.retrofitconfig.*;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static SQLiteDatabase db;
    String DB_PATH;
    private Context context;

    public SQLiteHelper(Context context) {
        super(context, Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
        DB_PATH = BaseUrlConfig.SqlDbPath;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Utils.CREATE_BOOKS_TABLE_SQL);
        sqLiteDatabase.execSQL(Utils.CREATE_SONGS_TABLE_SQL);
        sqLiteDatabase.execSQL(Utils.CREATE_SERMON_TABLE_SQL);
        sqLiteDatabase.execSQL(Utils.CREATE_TITHES_TABLE_SQL);
        sqLiteDatabase.execSQL(Utils.CREATE_USERS_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase SQLiteDB, int i, int i1) {
        SQLiteDB.execSQL("DROP TABLE IF EXISTS " + Utils.TBL_BOOKS);
        SQLiteDB.execSQL("DROP TABLE IF EXISTS " + Utils.TBL_SONGS);
        SQLiteDB.execSQL("DROP TABLE IF EXISTS " + Utils.TBL_SERMONS);
        SQLiteDB.execSQL("DROP TABLE IF EXISTS " + Utils.TBL_TITHES);
        SQLiteDB.execSQL("DROP TABLE IF EXISTS " + Utils.TBL_USERS);
        onCreate(SQLiteDB);
    }

    public int isIntNull(String myint)
    {
        return myint.isEmpty() ? Integer.parseInt(myint) : 0;
    }

    public void CheckDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try { db.execSQL("ALTER TABLE " + Utils.TBL_SONGS + " ADD " + Utils.ISFAV + " INTEGER;"); }
        catch (Exception ex) {}

        try { db.execSQL("ALTER TABLE " + Utils.TBL_SONGS + " ADD " + Utils.UPDATED + " TEXT;"); }
        catch (Exception ex) {}
        db.close();
    }

    public void CheckSermonTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try{ db.execSQL(Utils.CREATE_SERMON_TABLE_SQL); }
        catch (Exception ex) {}
        db.close();
    }

    public void CheckTithingTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try{ db.execSQL(Utils.CREATE_TITHES_TABLE_SQL); }
        catch (Exception ex) {}
        db.close();
    }

    public void addBook(CategoryModel book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Utils.CATEGORYID, book.categoryid);
        values.put(Utils.TITLE, book.title);
        values.put(Utils.TAGS, book.tags);
        values.put(Utils.QCOUNT, book.qcount);
        values.put(Utils.POSITION, book.position);
        values.put(Utils.CONTENT, book.content);
        values.put(Utils.BACKPATH, book.backpath);

        try{
            db.insert(Utils.TBL_BOOKS, null, values);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        db.close();
    }

    public void addSong(PostModel song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Utils.POSTID, song.postid);
        values.put(Utils.BOOKID, song.bookid);
        values.put(Utils.CATEGORYID, song.categoryid);
        values.put(Utils.BASETYPE, song.basetype);
        values.put(Utils.NUMBER, song.number);
        values.put(Utils.ALIAS, song.alias);
        values.put(Utils.TITLE, song.title);
        values.put(Utils.TAGS, song.tags);
        values.put(Utils.CONTENT, song.content);
        values.put(Utils.CREATED, song.created);
        values.put(Utils.UPDATED, song.updated);
        values.put(Utils.WHAT, song.what);
        values.put(Utils.WHEN, song.what);
        values.put(Utils.WHERE, song.what);
        values.put(Utils.WHO, song.what);
        values.put(Utils.NETTHUMBS, song.netthumbs);
        values.put(Utils.VIEWS, song.views);
        values.put(Utils.ACOUNT, song.acount);
        values.put(Utils.USERID, song.userid);
        values.put(Utils.ISFAV, song.isfav);

        try{
            db.insert(Utils.TBL_SONGS, null, values);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        db.close();
    }

    public List<CategoryModel> getBookList() {
        List<CategoryModel> SongBooks = new LinkedList<CategoryModel>();
        String query = "SELECT * FROM " + Utils.TBL_BOOKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                CategoryModel book = new CategoryModel(
                    Integer.parseInt(cursor.getString(0)),
                    Integer.parseInt(cursor.getString(1)),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
                );
                SongBooks.add(book);
            } while (cursor.moveToNext());
        }
        return SongBooks;
    }

    public List<SearchModel> searchSongs(String searchThis){
        List<SearchModel> SearchLists = new ArrayList<SearchModel>();
        String whereQuery = "";

        if (searchThis.length() > 1) {
            if (TextUtils.isDigitsOnly(searchThis))
                whereQuery = " WHERE " + Utils.NUMBER + "=" + searchThis;
            else
                whereQuery = " WHERE " + Utils.TITLE + " LIKE '%" + searchThis + "%' OR " + Utils.CONTENT + " LIKE '%" + searchThis + "%'";
        }

        String fullQuery = "SELECT  * FROM " + Utils.TBL_SONGS + whereQuery + " ORDER BY " + Utils.BOOKID + " LIMIT 12";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(fullQuery, null);
        SearchModel searchresult;
        if (cursor.moveToFirst()) {
            do {
                searchresult = new SearchModel(cursor.getString(7), Integer.parseInt(cursor.getString(0)));
                SearchLists.add(searchresult);
            } while (cursor.moveToNext());
        }

        return SearchLists;
    }

    public List<PostModel> searchForSongs(String searchthis) {
        List<PostModel> SongsList = new LinkedList<PostModel>();
        String whereQuery = "";

        if (searchthis.length() > 1) {
            if (TextUtils.isDigitsOnly(searchthis)) 
                whereQuery = " WHERE " + Utils.TBL_SONGS + "." + Utils.NUMBER + "=" + searchthis;
            else
                whereQuery = " WHERE " + Utils.TBL_SONGS + "." + Utils.TITLE + " LIKE '%" + searchthis +
                        "%' OR " + Utils.TBL_SONGS + "." + Utils.CONTENT + " LIKE '%" + searchthis + "%'";
        }

        String fullQuery = Utils.SONG_SELECT_SQL + whereQuery + " ORDER BY " + Utils.TBL_SONGS + "." + Utils.BOOKID + " LIMIT 12";

        SQLiteDatabase db = this.getWritableDatabase();
        PostModel song;
        try {
            Cursor cursor = db.rawQuery(fullQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    song = new PostModel();
                    song.songid = Integer.parseInt(cursor.getString(0));
                    song.bookid = cursor.getInt(1);
                    song.number = cursor.getInt(2);
                    song.alias = cursor.getString(3);
                    song.title = cursor.getString(4);
                    song.tags = cursor.getString(5);
                    song.content = cursor.getString(6);
                    song.categoryname = cursor.getString(7);
                    song.isfav = isIntNull(cursor.getString(8) + "");
                    song.created = cursor.getString(9);
                    song.updated = cursor.getString(10);

                    SongsList.add(song);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception ex) { CheckDatabase(); }
        return SongsList;
    }

    public List<PostModel> GetSongsList(int songbook) {
        List<PostModel> SongsList = new LinkedList<PostModel>();
        String whereQuery = (songbook == 0) ? "" : " WHERE " + Utils.TBL_SONGS + "." + Utils.CATEGORYID + "=" + songbook;

        String fullQuery = Utils.SONG_SELECT_SQL + whereQuery + " ORDER BY " + Utils.TBL_SONGS + "." + Utils.NUMBER;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(fullQuery, null);
            PostModel song;
            if (cursor.moveToFirst()) {
                do {
                    song = new PostModel();
                    song.songid = Integer.parseInt(cursor.getString(0));
                    song.bookid = cursor.getInt(1);
                    song.number = cursor.getInt(2);
                    song.alias = cursor.getString(3);
                    song.title = cursor.getString(4);
                    song.tags = cursor.getString(5);
                    song.content = cursor.getString(6);
                    song.categoryname = cursor.getString(7);
                    song.isfav = isIntNull(cursor.getString(8) + "");
                    song.created = cursor.getString(9);
                    song.updated = cursor.getString(10);

                    SongsList.add(song);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception ex) { CheckDatabase(); }
        return SongsList;
    }

    public String GetDate()
    {
        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);

        SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'yyyy");
        return formatter.format(curDateTime);
    }

    public List<PostModel> GetFavourites(String searchthis) {
        List<PostModel> SongsList = new LinkedList<PostModel>();
        String whereQry = Utils.TBL_SONGS + "." + Utils.ISFAV + "='1'";
        String whereQuery = " WHERE " + whereQry;

        if (searchthis.length() > 1) {
            if (TextUtils.isDigitsOnly(searchthis))
                whereQuery = whereQuery + " AND " + Utils.TBL_SONGS + "." + Utils.NUMBER + "=" + searchthis;
            else
                whereQuery = whereQuery + " AND " + Utils.TBL_SONGS + "." + Utils.TITLE + " LIKE '%" + searchthis +
                        "%' OR " + whereQry + " AND " + Utils.TBL_SONGS + "." + Utils.CONTENT + " LIKE '%" + searchthis + "%'";
        }

        String fullQuery = Utils.SONG_SELECT_SQL + whereQuery + " ORDER BY " + Utils.TBL_SONGS + "." + Utils.UPDATED + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(fullQuery, null);
        PostModel song;
        try {
            if (cursor.moveToFirst()) {
                do {
                    song = new PostModel();
                    song.songid = Integer.parseInt(cursor.getString(0));
                    song.bookid = cursor.getInt(1);
                    song.number = cursor.getInt(2);
                    song.alias = cursor.getString(3);
                    song.title = cursor.getString(4);
                    song.tags = cursor.getString(5);
                    song.content = cursor.getString(6);
                    song.categoryname = cursor.getString(7);
                    song.isfav = isIntNull(cursor.getString(8) + "");
                    song.created = cursor.getString(9);
                    song.updated = cursor.getString(10);

                    SongsList.add(song);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception ex) { CheckDatabase(); }
        return SongsList;
    }

    public List<PostModel> GetNotes(String searchthis) {
        List<PostModel> SongsList = new LinkedList<PostModel>();
        String whereQry = Utils.BOOKID + "='0'";
        String whereQuery = " WHERE " + whereQry;

        if (searchthis.length() > 1) {
            if (TextUtils.isDigitsOnly(searchthis))
                whereQuery = whereQuery + " AND " + Utils.NUMBER + "=" + searchthis;
            else
                whereQuery = whereQuery + " AND " + Utils.TITLE + " LIKE '%" + searchthis +
                        "%' OR " + whereQry + " AND " + Utils.CONTENT + " LIKE '%" + searchthis + "%'";
        }

        String fullQuery = Utils.NOTE_SELECT_SQL + whereQuery + " ORDER BY " + Utils.CREATED;

        PostModel song;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(fullQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    song = new PostModel();
                    song.songid = Integer.parseInt(cursor.getString(0));
                    song.bookid = cursor.getInt(1);
                    song.number = cursor.getInt(2);
                    song.alias = cursor.getString(3);
                    song.title = cursor.getString(4);
                    song.tags = cursor.getString(5);
                    song.content = cursor.getString(6);
                    song.isfav = isIntNull(cursor.getString(7) + "");
                    song.created = cursor.getString(8);
                    song.updated = cursor.getString(9);

                    SongsList.add(song);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception ex) { CheckDatabase(); }
        return SongsList;
    }

    public List<TitheModel> GetThithing() {
        List<TitheModel> TithingList = new LinkedList<TitheModel>();

        TitheModel tithes;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(Utils.TITHES_SELECT_SQL, null);
            if (cursor.moveToFirst()) {
                do {
                    tithes = new TitheModel();
                    tithes.titheid = Integer.parseInt(cursor.getString(0));
                    tithes.source = cursor.getString(1);
                    tithes.mode = cursor.getString(2);
                    tithes.amount = cursor.getInt(3);
                    tithes.extra = cursor.getString(4);
                    tithes.created = cursor.getString(5);

                    TithingList.add(tithes);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception ex) { CheckTithingTable(); }
        return TithingList;
    }

    public PostModel viewSong(int songid) {
        String query = Utils.SONG_SELECT_SQL + " WHERE " + Utils.SONGID + "=" + songid;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) cursor.moveToFirst();

        PostModel song = new PostModel();
        song.songid = Integer.parseInt(cursor.getString(0));
        song.bookid = cursor.getInt(1);
        song.number = cursor.getInt(2);
        song.alias = cursor.getString(3);
        song.title = cursor.getString(4);
        song.tags = cursor.getString(5);
        song.content = cursor.getString(6);
        song.categoryname = cursor.getString(7);
        song.isfav = isIntNull(cursor.getString(8) + "");
        song.created = cursor.getString(9);
        song.updated = cursor.getString(10);

        return song;
    }

    public int Favourite(PostModel song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Utils.ISFAV, song.isfav);

        int i = db.update(Utils.TBL_SONGS, values, Utils.SONGID + " = ?", new String[] { String.valueOf(song.songid) });
        db.close();
        return i;
    }

    public void addSermon(SermonModel sermon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Utils.SERMONID, sermon.sermonid);
        values.put(Utils.CATEGORYID, sermon.categoryid);
        values.put(Utils.TITLE, sermon.title);
        values.put(Utils.SUBTITLE, sermon.subtitle);
        values.put(Utils.PREACHER, sermon.preacher);
        values.put(Utils.PLACE, sermon.place);
        values.put(Utils.EXTRA, sermon.extra);
        values.put(Utils.TAGS, sermon.tags);
        values.put(Utils.CONTENT, sermon.content);
        values.put(Utils.CREATED, sermon.created);
        values.put(Utils.UPDATED, sermon.updated);
        values.put(Utils.STATE, sermon.state);
        values.put(Utils.ISFAV, sermon.isfav);

        try{
            db.insert(Utils.TBL_SONGS, null, values);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        db.close();
    }

    public List<SermonModel> GetSermons(String searchthis) {
        List<SermonModel> SermonList = new LinkedList<SermonModel>();
        String whereQuery = "";

        if (searchthis.length() > 1) {
            whereQuery = " WHERE " +
                    Utils.TITLE + " LIKE '%" + searchthis + "%' OR " +
                    Utils.SUBTITLE + " LIKE '%" + searchthis + "%' OR " +
                    Utils.PREACHER + " LIKE '%" + searchthis + "%' OR " +
                    Utils.PLACE + " LIKE '%" + searchthis + "%' OR " +
                   Utils.CONTENT + " LIKE '%" + searchthis + "%'";
        }

        String fullQuery = Utils.SERMON_SELECT_SQL + whereQuery + " ORDER BY " + Utils.CREATED;

        SermonModel sermon;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(fullQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    sermon = new SermonModel();
                    sermon.sermonid = Integer.parseInt(cursor.getString(0));
                    sermon.categoryid = cursor.getInt(1);
                    sermon.title = cursor.getString(2);
                    sermon.subtitle = cursor.getString(3);
                    sermon.preacher = cursor.getString(4);
                    sermon.place = cursor.getString(5);
                    sermon.extra = cursor.getString(6);
                    sermon.content = cursor.getString(7);
                    sermon.created = cursor.getString(8);
                    sermon.updated = cursor.getString(9);
                    sermon.isfav = isIntNull(cursor.getString(10) + "");
                    sermon.updated = cursor.getString(11);

                    SermonList.add(sermon);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception ex) { CheckSermonTable(); }
        return SermonList;
    }

    public SermonModel viewSermon(int sermonid) {
        String query = Utils.SERMON_SELECT_SQL + " WHERE " + Utils.SERMONID + "=" + sermonid;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) cursor.moveToFirst();

        SermonModel sermon = new SermonModel();
        sermon.sermonid = Integer.parseInt(cursor.getString(0));
        sermon.categoryid = cursor.getInt(1);
        sermon.title = cursor.getString(2);
        sermon.subtitle = cursor.getString(3);
        sermon.preacher = cursor.getString(4);
        sermon.place = cursor.getString(5);
        sermon.extra = cursor.getString(6);
        sermon.content = cursor.getString(7);
        sermon.created = cursor.getString(8);
        sermon.updated = cursor.getString(9);
        sermon.isfav = isIntNull(cursor.getString(10) + "");
        sermon.updated = cursor.getString(11);

        return sermon;
    }

    public void deleteData(long id){
        try {
            db.delete(Utils.TBL_SONGS, Utils.POSTID+"="+id, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void deleteAllData(){
        try {
            db.delete(Utils.TBL_SONGS, null, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void close(){
        db.close();
    }

}
