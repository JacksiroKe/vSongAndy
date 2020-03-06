package com.jackson_siro.visongbook.adapters;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.ui.*;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class HomeGridAdapter extends BaseAdapter {

    private Context context;
    private final Integer[] gridIcons;
    private final String[] gridValues;

    public HomeGridAdapter(Context context, Integer[] gridIcons, String[] gridValues) {
        this.context = context;
        this.gridIcons = gridIcons;
        this.gridValues = gridValues;
    }

    @Override
    public int getCount() {
        return gridValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {
            gridView = inflater.inflate(R.layout.grid_item, null);
            FloatingActionButton iconView = gridView.findViewById(R.id.item_fab);
            final TextView textView = gridView.findViewById(R.id.item_label);

            iconView.setImageDrawable(ContextCompat.getDrawable(context, gridIcons[position]));
            textView.setText(gridValues[position]);

            iconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence selectedTxt = textView.getText();

                    if (selectedTxt == context.getString(R.string.new_song))
                        context.startActivity(new Intent(context, DdSongPad.class));

                    else if (selectedTxt == context.getString(R.string.new_sermon))
                        context.startActivity(new Intent(context, DdSermonPad.class));

                    else if (selectedTxt == context.getString(R.string.my_songbooks))
                        context.startActivity(new Intent(context, EeBooksView.class));

                    else if (selectedTxt == context.getString(R.string.how_it_works))
                        context.startActivity(new Intent(context, GgTutorial.class));

                    else if (selectedTxt == context.getString(R.string.songs_pad))
                        context.startActivity(new Intent(context, DdHomeView.class));

                    else if (selectedTxt == context.getString(R.string.sermon_pad))
                        context.startActivity(new Intent(context, DdHomeView.class));

                    else if (selectedTxt == context.getString(R.string.manage_songbooks))
                        context.startActivity(new Intent(context, DdSongPad.class));

                    else if (selectedTxt == context.getString(R.string.app_settings))
                        context.startActivity(new Intent(context, FfSettings.class));

                    else if (selectedTxt == context.getString(R.string.favorites))
                        context.startActivity(new Intent(context, DdHomeView.class));

                    else if (selectedTxt == context.getString(R.string.help_desk))
                        context.startActivity(new Intent(context, GgHelpdesk.class));

                    else if (selectedTxt == context.getString(R.string.app_donate))
                        context.startActivity(new Intent(context, FfDonate.class));

                    //else if (selectedTxt == context.getString(R.string.about_app))
                     //   context.startActivity(new Intent(context, GgAboutapp.class));

                    else context.startActivity(new Intent(context, EeBooksView.class));
                }
            });
        } else gridView = convertView;

        return gridView;
    }

}
