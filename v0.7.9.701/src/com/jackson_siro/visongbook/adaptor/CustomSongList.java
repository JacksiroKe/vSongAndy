package com.jackson_siro.visongbook.adaptor;

import com.jackson_siro.visongbook.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class CustomSongList extends ArrayAdapter<String>{
	
		private final Activity context;
		private final String[] mytext;
		private final String[] mytexti;
		private final String[] mytextii;
		
	public CustomSongList(Activity context,	String[] mytext, String[] mytexti, String[] mytextii) {
		super(context, R.layout.cc_list_songs, mytext);
		this.context = context;
		this.mytext = mytext;
		this.mytexti = mytexti;
		this.mytextii = mytextii;

	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.cc_list_songs, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		TextView txtTitlei = (TextView) rowView.findViewById(R.id.txti);
		TextView txtTitleii = (TextView) rowView.findViewById(R.id.txtii);
	
		txtTitle.setText(mytext[position]);
		txtTitlei.setText(mytexti[position]);
		txtTitleii.setText(mytextii[position]);
	
		return rowView;
	
	}
}