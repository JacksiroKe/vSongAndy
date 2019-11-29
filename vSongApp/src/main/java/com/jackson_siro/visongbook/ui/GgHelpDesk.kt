package com.jackson_siro.visongbook.ui

import android.os.Bundle

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.jackson_siro.visongbook.R

class GgHelpdesk : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gg_helpdesk)
        toolbarSet()

    }

    private fun toolbarSet() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
        actionBar!!.title = "App Helpdesk"
    }

}
