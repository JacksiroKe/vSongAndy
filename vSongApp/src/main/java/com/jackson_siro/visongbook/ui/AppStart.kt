package com.jackson_siro.visongbook.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.content.SharedPreferences

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

import com.jackson_siro.visongbook.R
import com.jackson_siro.visongbook.models.callback.CallbackApp
import com.jackson_siro.visongbook.models.AppModel
import com.jackson_siro.visongbook.setting.CheckNetwork
import com.jackson_siro.visongbook.setting.CheckVersion

import retrofit2.Call

class AppStart : AppCompatActivity() {
    private var imgicon: ImageView? = null
    private var imgcopyright: ImageView? = null

    private var ms: Long = 0
    private val splashTime: Long = 5000
    private val splashActive = true
    private val paused = false
    private var prefget: SharedPreferences? = null
    private var prefedit: SharedPreferences.Editor? = null

    private val Appdata: AppModel? = null
    private val appCall: Call<CallbackApp>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_start)

        prefget = PreferenceManager.getDefaultSharedPreferences(this)
        prefedit = prefget!!.edit()
        imgicon = findViewById(R.id.imgicon)
        imgcopyright = findViewById(R.id.imgcopyright)

        setFirstUse()

        val animation1 = AnimationUtils.loadAnimation(applicationContext, R.anim.fade)
        imgicon!!.startAnimation(animation1)

        val animation2 = AnimationUtils.loadAnimation(applicationContext, R.anim.fade1)
        imgcopyright!!.startAnimation(animation2)

        val splashThread = object : Thread() {
            override fun run() {
                try {
                    while (splashActive && ms < splashTime) {
                        if (!paused) ms = ms + 100
                        //Thread.sleep(100)
                    }
                } catch (e: Exception) {
                } finally {
                    checkSession()
                }
            }
        }
        splashThread.start()
    }

    fun checkSession() {
        if (CheckNetwork.isConnectCheck(this)) CheckVersion.isOutdatedCheck(this)

        if (!prefget!!.getBoolean("app_user_signedin", false)) {
            startActivity(Intent(this, BbUserSignin::class.java))
        } else {
            if (!prefget!!.getBoolean("app_books_loaded", false))
                startActivity(Intent(this, CcBooksLoad::class.java))
            else if (prefget!!.getBoolean("app_books_reload", false))
                startActivity(Intent(this, CcBooksLoad::class.java))
            else if (prefget!!.getBoolean("app_books_loaded", false) && !prefget!!.getBoolean("app_songs_loaded", false))
                startActivity(Intent(this, CcSongsLoad::class.java))
            else
                startActivity(Intent(this, DdMainView::class.java))
        }

        finish()
    }

    fun setFirstUse() {
        if (!prefget!!.getBoolean("app_first_use", false)) {
            prefedit!!.putBoolean("app_first_use", true).apply()
            prefedit!!.putBoolean("app_user_signedin", false).apply()
            prefedit!!.putBoolean("app_user_donated", false).apply()
            prefedit!!.putLong("app_first_data", System.currentTimeMillis()).apply()
        }
    }
}