package com.jackson_siro.visongbook.adaptor;

import com.jackson_siro.visongbook.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint({ "ViewHolder", "InflateParams" })
public class CustomList extends ArrayAdapter<String>{
	
		private final Activity context;
		private final String[] mytext;
		private final String[] mytexti;
		
	public CustomList(Activity context,	String[] mytext, String[] mytexti) {
		super(context, R.layout.cc_list_single, mytext);
		this.context = context;
		this.mytext = mytext;
		this.mytexti = mytexti;

	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.cc_list_single, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		TextView txtTitlei = (TextView) rowView.findViewById(R.id.txti);
	
		txtTitle.setText(mytext[position]);
		txtTitlei.setText(mytexti[position]);
	
		return rowView;
	
	}
}