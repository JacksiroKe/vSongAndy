package com.jackson_siro.visongbook.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jackson_siro.visongbook.R;

public class GgAboutapp extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView txttitle, txtcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gg_aboutapp);
        txttitle = findViewById(R.id.txttitle);
        txtcontent = findViewById(R.id.txtcontent);
        toolbarSet();
        txttitle.setText("vSongBook for Android");
        txtcontent.setText("vSongBook (Virtual SongBook)");
    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("About vSongBook");
    }

}
