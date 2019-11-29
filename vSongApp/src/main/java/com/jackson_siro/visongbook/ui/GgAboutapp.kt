package com.jackson_siro.visongbook.ui

import android.os.Bundle
import android.widget.TextView

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.jackson_siro.visongbook.R

class GgAboutapp : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var actionBar: ActionBar? = null
    private var txttitle: TextView? = null
    private var txtcontent: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gg_aboutapp)
        txttitle = findViewById(R.id.txttitle)
        txtcontent = findViewById(R.id.txtcontent)
        toolbarSet()
        txttitle!!.text = "vSongBook for Android"
        txtcontent!!.text = "vSongBook (Virtual SongBook)"
    }

    private fun toolbarSet() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
        actionBar!!.title = "About vSongBook"
    }

}
