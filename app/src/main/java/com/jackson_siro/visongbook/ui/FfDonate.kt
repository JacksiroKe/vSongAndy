package com.jackson_siro.visongbook.ui

import android.content.SharedPreferences
import androidx.preference.PreferenceManager

import android.os.Bundle

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.jackson_siro.visongbook.R

class FfDonate : AppCompatActivity() {

    private var prefget: SharedPreferences? = null
    private var prefedit: SharedPreferences.Editor? = null

    private var toolbar: Toolbar? = null
    private var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ff_donate)

        prefget = PreferenceManager.getDefaultSharedPreferences(this)
        prefedit = prefget!!.edit()

        toolbarSet()

        /*builder.setMessage("Hello " + mGender + mFullname + "! vSongBook is proudly non-profit, non-corporate and non-compromised. " +
                "A lot of users like you and your friends help us a lot to stand up for a free vSongBook for all. We now rely on donations to carry out our " +
                "mission to give everyone using our app the freedom to sing anywhere anytime. Any amount of money will be " +
                "highly appreciated by our team of developers. Would you like to know about this?"
        );*/
        prefedit!!.putBoolean("app_user_donated", true).apply()
    }

    private fun toolbarSet() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
        actionBar!!.title = "Support us, Donate"
    }

}
