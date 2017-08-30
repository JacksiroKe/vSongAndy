package com.jackson_siro.visongbook.adaptor;

import com.jackson_siro.visongbook.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class CustomSongView extends ArrayAdapter<String>{

	private final Activity context;
	private final String[] mytext;
	private final int myfont;
		
	public CustomSongView(Activity context,	String[] mytext, int myfont) {
		super(context, R.layout.song_reader, mytext);
		this.context = context;
		this.mytext = mytext;
		this.myfont = myfont;

	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.song_reader, null, true);
		TextView txtText = (TextView) rowView.findViewById(R.id.stanzatxt);
		txtText.setText(mytext[position]);
		txtText.setTextSize(myfont);
		
		return rowView;
	
	}
}