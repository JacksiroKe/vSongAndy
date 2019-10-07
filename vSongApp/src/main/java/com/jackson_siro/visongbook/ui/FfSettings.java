package com.jackson_siro.visongbook.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jackson_siro.visongbook.R;

public class FfSettings extends AppCompatActivity  {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private static final String TITLE_TAG = "settingsActivityTitle";

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ff_settings);
        toolbarSet();
        displayProfile();

        getSupportFragmentManager().beginTransaction().replace(R.id.app_settings, new SettingsFragment()).commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.fabaction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivity(new Intent(FfSettings.this, FfSettingsProfile.class));
            }
        });
    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("vSongBook Settings");
    }

    private void displayProfile() {
        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        String mGender = prefget.getString("user_gender", "1") == "1" ? "Bro. " : "Sis. ";
        String mFullname = prefget.getString("user_firstname", "") + " " + prefget.getString("user_lastname", "");

        TextView full_name = findViewById(R.id.full_name);
        TextView mobile_phone = findViewById(R.id.mobile_phone);
        TextView profile_text = findViewById(R.id.profile_text);

        full_name.setText(String.format(mGender + mFullname));
        mobile_phone.setText(prefget.getString("user_mobile", "-"));
        profile_text.setText(String.format(
                prefget.getString("user_church", "-") + " Church\n" +
                        prefget.getString("user_city", "-") + " " + prefget.getString("user_country_ccode", ""))
        );

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_app, rootKey);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.ff_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                return true;

            default:
                return false;
        }
    }

}
