package com.jackson_siro.visongbook.songbooks;

import com.jackson_siro.visongbook.*;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SongBook7 extends ListFragment implements LoaderCallbacks<Cursor> {
    
	SimpleCursorAdapter mCursorAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    	doSearch("NyimboCiaKuiniraNgai");
    	String[] from = new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2 };
        int[] to = new int[] { R.id.katiname,R.id.katicont };

        mCursorAdapter = new SimpleCursorAdapter(getActivity().getBaseContext(), R.layout.song_result, null, from, to, 0);        
		setListAdapter(mCursorAdapter); 
    	
        return super.onCreateView(inflater, container, savedInstanceState);

    }    
    

    public void doSearch(String query) {
        Bundle data = new Bundle();
        data.putString("query", query);
        getLoaderManager().initLoader(1, data, this);
    }

    public void onStart() {
        super.onStart();
        getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                
                Intent countryIntent = new Intent(getActivity().getApplicationContext(), SongBookView.class);
                Uri data = Uri.withAppendedPath(SongBookProvider.CONTENT_URI, String.valueOf(id));
                countryIntent.setData(data);
                startActivity(countryIntent);
            }
        });
    }

    public Loader<Cursor> onCreateLoader(int arg0, Bundle data) {
        Uri uri = SongBookProvider.CONTENT_URI;
        return new CursorLoader(getContext(), uri, null, null, new String[]{data.getString("query")}, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        this.mCursorAdapter.swapCursor(c);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }
}