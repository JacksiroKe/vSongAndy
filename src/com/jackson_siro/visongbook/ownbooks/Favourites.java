package com.jackson_siro.visongbook.ownbooks;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.adaptor.*;

import android.content.DialogInterface;
import android.content.Intent;


public class Favourites extends ActionBarActivity {
	ListView list;
		
	private String[] My_Text;
	private String[] My_Texti;
	private String[] My_Textii;
	
	SQLiteHelperF db = new SQLiteHelperF(this);
	List<Favour> mylist;
	ArrayAdapter<String> myAdapter;

	ArrayList<HashMap<String, String>> myFavours;
	private TextView NothingText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ob_main);
		mylist = db.getAllFavours();
		NothingText = (TextView)findViewById(R.id.text);
		FillListView();
		
		if (mylist.size() <= 0){
			NothingText.setText(R.string.no_favours);			
		} else if (mylist.size() > 0){
			NothingText.setText(R.string.my_favours);
		} 
		
	}
	
	private void FillListView(){
		mylist = db.getAllFavours();
		List<String> listId = new ArrayList<String>();
		List<String> listTitle = new ArrayList<String>();
		List<String> listCont = new ArrayList<String>();
		
		for (int i = 0; i < mylist.size(); i++) {
			listId.add(i, Integer.toString(mylist.get(i).getId()));
			listTitle.add(i, mylist.get(i).getTitle());
			listCont.add(i, mylist.get(i).getContent());			
		}
		
		My_Text = listId.toArray(new String[listId.size()]);		
		for (String string : My_Text) {	System.out.println(string);	}
		
		My_Texti = listTitle.toArray(new String[listTitle.size()]);		
		for (String stringi : My_Texti) {	System.out.println(stringi);	}
		
		My_Textii = listCont.toArray(new String[listCont.size()]);		
		for (String stringii : My_Textii) {	System.out.println(stringii);	}
		
		CustomFavourite adapter = new CustomFavourite(Favourites.this, My_Text, My_Texti, My_Textii);
		list=(ListView)findViewById(R.id.list);
		list.setAdapter(adapter);		
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
    			Intent intent = new Intent(Favourites.this, Favourite.class);
    			intent.putExtra("favour", Integer.parseInt(My_Text[+ position]));
    			startActivityForResult(intent, 1);
        		
            }
        });	
        	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		FillListView();
		
		if (mylist.size() <= 0){
			NothingText.setText(R.string.no_favours);			
		} else if (mylist.size() > 0){
			NothingText.setText(R.string.my_favours);
		} 
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favourites, menu);
        
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.information:
            	AlertDialog.Builder builder = new AlertDialog.Builder(Favourites.this);
            	builder.setTitle(R.string.just_a_min);
                builder.setMessage(R.string.title_songlist_information);
                builder.setNegativeButton(R.string.action_gotit, new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Toast.makeText(getApplicationContext(), R.string.blessed, Toast.LENGTH_LONG).show();						
					}
				});
                builder.show();
            	return true;
            	
            default:
                return false;
        }
    }
	
	
		
}
