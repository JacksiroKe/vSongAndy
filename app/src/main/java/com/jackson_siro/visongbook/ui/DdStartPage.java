package com.jackson_siro.visongbook.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.adapters.*;

import java.util.ArrayList;
import java.util.List;

public class DdStartPage extends AppCompatActivity {

    GridView homeGrid;
    Integer[] homeIcons = new Integer[] { R.drawable.ic_edit };
    String[] homeActions = new String[] { "1"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_start_page);
        homeGrid = findViewById(R.id.home_grid);

        homeInit();

    }

    private void homeInit()
    {

        List<Integer> listIcons = new ArrayList<Integer>();
        List<String> listActions = new ArrayList<String>();

        listIcons.add(R.drawable.ic_edit);
        listIcons.add(R.drawable.ic_edit);
        listIcons.add(R.drawable.ic_books);
        listIcons.add(android.R.drawable.ic_dialog_info);
        listIcons.add(R.drawable.ic_books);
        listIcons.add(R.drawable.ic_books);
        listIcons.add(android.R.drawable.ic_menu_preferences);
        listIcons.add(R.drawable.ic_preferences);
        listIcons.add(R.drawable.ic_favorite_black);
        listIcons.add(android.R.drawable.ic_menu_call);
        listIcons.add(R.drawable.ic_money);
        listIcons.add(android.R.drawable.ic_dialog_info);

        listActions.add(getString(R.string.new_song));
        listActions.add(getString(R.string.new_sermon));
        listActions.add(getString(R.string.my_songbooks));
        listActions.add(getString(R.string.how_it_works));
        listActions.add(getString(R.string.songs_pad));
        listActions.add(getString(R.string.sermon_pad));
        listActions.add(getString(R.string.manage_songbooks));
        listActions.add(getString(R.string.app_settings));
        listActions.add(getString(R.string.favorites));
        listActions.add(getString(R.string.help_desk));
        listActions.add(getString(R.string.app_donate));
        listActions.add(getString(R.string.about_app));

        homeIcons = listIcons.toArray(new Integer[listIcons.size()]);
        homeActions = listActions.toArray(new String[listActions.size()]);

        homeGrid.setAdapter(new HomeGridAdapter(this, homeIcons, homeActions));

    }
}
