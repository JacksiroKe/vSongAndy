package com.jackson_siro.visongbook.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jackson_siro.visongbook.R

import kotlinx.android.synthetic.main.gg_helpdesk.*

class GgHelpdesk : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gg_helpdesk)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
