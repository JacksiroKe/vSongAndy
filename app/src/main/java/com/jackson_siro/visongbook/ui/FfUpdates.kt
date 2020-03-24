package com.jackson_siro.visongbook.ui

import android.os.Bundle

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.jackson_siro.visongbook.R

class FfUpdates : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ff_updates)
        toolbarSet()

    }

    private fun toolbarSet() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
        actionBar!!.title = "Updates for your vSongBook"
    }

}
