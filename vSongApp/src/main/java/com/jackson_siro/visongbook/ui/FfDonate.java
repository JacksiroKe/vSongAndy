package com.jackson_siro.visongbook.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jackson_siro.visongbook.R;

public class FfDonate extends AppCompatActivity {

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ff_donate);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        toolbarSet();

        prefedit.putBoolean("app_user_donated", true).apply();
    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Support us, Donate");
    }

}
