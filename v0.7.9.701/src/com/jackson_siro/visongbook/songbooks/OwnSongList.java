package com.jackson_siro.visongbook.songbooks;

import java.util.ArrayList;
import java.util.List;
import com.jackson_siro.visongbook.adaptor.*;
import com.jackson_siro.visongbook.tools.*;

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
import android.widget.ArrayAdapter;

public class OwnSongList extends ListFragment implements LoaderCallbacks<Cursor> {
    
	public SongBookSQLite db;
	
	private String[] My_Text, My_Texti, My_Textii;
	List<SongMine> mylist;
	ArrayAdapter<String> listAdapter;
	SimpleCursorAdapter mCursorAdapter;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    	db = new SongBookSQLite(getActivity().getBaseContext(), SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
    	mylist = db.getAllMySongs();
    	
    	List<String> listSongid = new ArrayList<String>();
		List<String> listTitle = new ArrayList<String>();
		List<String> listContent = new ArrayList<String>();
		
		for (int i = 0; i < mylist.size(); i++) {
			listSongid.add(i, Integer.toString(mylist.get(i).getSongid()));
			listTitle.add(i, mylist.get(i).getTitle());
			listContent.add(i, mylist.get(i).getContent());
		}
		
		My_Text = listSongid.toArray(new String[listSongid.size()]);
		My_Texti = listTitle.toArray(new String[listTitle.size()]);	
		My_Textii = listContent.toArray(new String[listContent.size()]);
		setListAdapter(new CustomSongList(getActivity(), My_Text, My_Texti, My_Textii));
 
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    public void onStart() {
        super.onStart();
        getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                
                String songid = Integer.toString(mylist.get(position).getSongid());
            	Intent myIntent = new Intent(getActivity().getApplicationContext(), OwnSongView.class);
            	Uri data = Uri.withAppendedPath(SongBookProvider.CONTENT_URI, songid);
                myIntent.setData(data);
                startActivity(myIntent);
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