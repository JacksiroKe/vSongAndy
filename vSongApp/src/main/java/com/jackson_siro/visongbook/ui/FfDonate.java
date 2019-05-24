package com.jackson_siro.visongbook.ui;

<<<<<<< HEAD
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
=======
>>>>>>> c0aed1253c303a33b706b3212d96bfbef706cfcc
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jackson_siro.visongbook.R;

public class FfDonate extends AppCompatActivity {

<<<<<<< HEAD
    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;
=======
>>>>>>> c0aed1253c303a33b706b3212d96bfbef706cfcc
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ff_donate);
<<<<<<< HEAD

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        toolbarSet();

        prefedit.putBoolean("app_user_donated", true).apply();
=======
        toolbarSet();

>>>>>>> c0aed1253c303a33b706b3212d96bfbef706cfcc
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
