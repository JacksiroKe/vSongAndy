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

    private var about: TextView? = null
    private var about_txt: String? = null

    private var history: TextView? = null
    private var history_txt: String? = null

    private var contributors: TextView? = null
    private var contributors_txt: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gg_aboutapp)
        about = findViewById(R.id.txt_about)
        history = findViewById(R.id.txt_history)
        contributors = findViewById(R.id.txt_contributors)
        toolbarSet()

        about_txt = ""
        about!!.text = about_txt

        history_txt = "HISTORY\nvSongBook (Virtual Songbook) is an application that first developed in September 2014 by " +
                "Jackson Siro (Jacksiro) while a student at KTTC undertaking a diploma in ICT. By then he was " +
                "just trying his hands on Android and he just wanted make a little app to have the Songs of " +
                "worship and Nyimbo za injili on his phone just like the Bible was an app on his phone. While at " +
                "home in December the same year his father who came to his room to check on him and ask 'some " +
                "little computer questions' noticed an unfamiliar app on his phone. On learning and interacting " +
                "with it he asked to have it on his phone. The happy father went to share the app Jack's elder " +
                "brothers and while at the End of Year Meetings at Mbale, Kenya he endosed it there during one of his sermons.\n"

        history!!.text = history_txt

        contributors_txt = ""
        contributors!!.text = contributors_txt

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
