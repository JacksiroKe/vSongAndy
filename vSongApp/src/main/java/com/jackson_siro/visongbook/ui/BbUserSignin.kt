package com.jackson_siro.visongbook.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface

import android.os.Bundle
import androidx.preference.PreferenceManager
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import java.util.ArrayList

import com.google.android.material.snackbar.Snackbar
import com.jackson_siro.visongbook.components.SearchDialogCompat
import com.jackson_siro.visongbook.data.Countries
import com.jackson_siro.visongbook.models.callback.*
import com.jackson_siro.visongbook.models.*
import com.jackson_siro.visongbook.retrofitconfig.*
import com.jackson_siro.visongbook.R
import com.jackson_siro.visongbook.core.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BbUserSignin : AppCompatActivity() {

    var prefget: SharedPreferences? = null
    var prefedit: SharedPreferences.Editor? = null

    var progressDialog: ProgressDialog? = null

    var Appuser: UserModel? = null
    private var usersCall: Call<CallbackUser>? = null

    var toolbar: Toolbar? = null
    var btnProceed: Button? = null
    var inputCountry: EditText? = null
    var inputCode: EditText? = null
    var inputPhone: EditText? = null
    var cntry: Int = 0

    var countries: ArrayList<CountryModel>? = null
    var ctrlist: Countries = Countries()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bb_user_signin)
        prefget = this.getSharedPreferences("com.jackson_siro.visongbook", 0)
        prefedit = prefget!!.edit()

        countries = ArrayList(ctrlist.createSampleData())

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        inputCountry = findViewById(R.id.input_country)
        inputCode = findViewById(R.id.input_code)
        inputPhone = findViewById(R.id.input_phone)

        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)

        inputCountry!!.setOnClickListener { countryDialog() }

        inputCode!!.setOnClickListener { countryDialog() }

        setDefaults()

        btnProceed = findViewById(R.id.btn_proceed)
        btnProceed!!.setOnClickListener { view ->
            val code = inputCode!!.text.toString().trim { it <= ' ' }
            val phone = inputPhone!!.text.toString().trim { it <= ' ' }

            //int phoneno = Integer.parseInt(phone);
            if (phone.isEmpty())
                Snackbar.make(view, "Please enter your mobile number", Snackbar.LENGTH_SHORT).show()
            else if (phone.length < 8)
                Snackbar.make(view, "Please enter a valid mobile number", Snackbar.LENGTH_SHORT).show()
            else if (phone.startsWith("0"))
                Snackbar.make(view, "Please omit the first zero in your mobile number", Snackbar.LENGTH_SHORT).show()
            else {
                prefedit!!.putString("user_mobile", code + phone).apply()
                signinUser(code + phone)
            }
        }
    }

    private fun setDefaults() {
        if (prefget!!.getString("user_country_name", "")!!.isEmpty()) {
            prefedit!!.putString("user_country_name", "Kenya").apply()
            prefedit!!.putString("user_country_icode", "254").apply()
            prefedit!!.putString("user_country_ccode", "KE").apply()
        } else {
            val icode = prefget!!.getString("user_country_icode", "")
            for (i in countries!!.indices) {
                if (countries!![i].icode == icode) {
                    cntry = i
                    break
                }
            }

            val mobile = prefget!!.getString("user_mobile", "")
            val mobilesaved = TextUtils.split(mobile, icode)

            inputCountry!!.setText(prefget!!.getString("user_country_name", ""))
            inputCode!!.setText(prefget!!.getString("user_country_icode", ""))
            inputCode!!.setText(countries!![cntry].icode)
            //inputPhone.setText(mobilesaved[1]);
        }
    }

    private fun countryDialog() {
        val dialog = SearchDialogCompat(this@BbUserSignin, "Which is your Country?", "Search for your country", null, countries, SearchResultListener { dialog, country, position ->
            inputCountry!!.setText(country.country)
            inputCode!!.setText(country.icode)
            prefedit!!.putString("user_country_name", country.country).apply()
            prefedit!!.putString("user_country_icode", country.icode).apply()
            prefedit!!.putString("user_country_ccode", country.ccode).apply()
            dialog.dismiss()
        }
        )
        dialog.show()
        dialog.searchBox.typeface = Typeface.SERIF
    }

    private fun signinUser(mobile: String) {
        showDialog(true)
        val api = CallJson.callJson()
        usersCall = api.UserSignin(mobile)
        usersCall!!.enqueue(object : Callback<CallbackUser> {
            override fun onResponse(call: Call<CallbackUser>, response: Response<CallbackUser>) {
                showDialog(false)
                val cl = response.body()
                if (cl != null) {
                    Appuser = cl.data
                    if (cl.data.success === "1")
                        handleUserData()
                    else
                        apiResult(cl.data.success, cl.data.message)
                }// else apiResult(5, "null response");
            }

            override fun onFailure(call: Call<CallbackUser>, t: Throwable) {
                showDialog(false)
                //apiResult(5, t.getMessage());
            }

        })
    }

    private fun handleUserData() {
        for (i in countries!!.indices) {
            if (countries!![i].ccode === Appuser!!.country) {
                cntry = 1
            }
        }

        prefedit!!.putString("user_country_name", countries!![cntry].country).apply()
        prefedit!!.putString("user_country_icode", countries!![cntry].icode).apply()
        prefedit!!.putString("user_country_ccode", Appuser!!.country).apply()

        prefedit!!.putInt("user_userid", Integer.parseInt(Appuser!!.userid)).apply()
        //prefedit.putString("user_uniqueid", Appuser.uid).apply();
        prefedit!!.putString("user_firstname", Appuser!!.firstname).apply()
        prefedit!!.putString("user_lastname", Appuser!!.lastname)
        prefedit!!.putString("user_mobile", Appuser!!.mobile).apply()
        prefedit!!.putString("user_gender", Appuser!!.gender).apply()
        prefedit!!.putString("user_city", Appuser!!.city).apply()
        prefedit!!.putString("user_church", Appuser!!.church).apply()
        prefedit!!.putString("user_email", Appuser!!.email).apply()
        prefedit!!.putString("user_level", Appuser!!.level).apply()
        prefedit!!.putString("user_handle", Appuser!!.handle).apply()
        prefedit!!.putString("user_created", Appuser!!.created).apply()
        prefedit!!.putString("user_signedin", Appuser!!.signedin).apply()
        prefedit!!.putString("user_points", Appuser!!.points).apply()
        prefedit!!.putString("user_wallposts", Appuser!!.wallposts).apply()

        prefedit!!.putBoolean("app_user_signedin", true).apply()

        if (!prefget!!.getBoolean("app_books_loaded", false)) {
            startActivity(Intent(this@BbUserSignin, CcBooksLoad::class.java))
        } else
            startActivity(Intent(this@BbUserSignin, DdMainView::class.java))
        finish()
    }

    private fun apiResult(errorno: String, errormsg: String) {
        prefedit!!.putBoolean("app_user_signedin", false)
        prefedit!!.apply()


        when (Integer.parseInt(errorno)) {
            1 -> {
                if (!prefget!!.getBoolean("app_books_loaded", false)) {
                    startActivity(Intent(this@BbUserSignin, CcBooksLoad::class.java))
                } else if (prefget!!.getBoolean("app_books_reload", false)) {
                    startActivity(Intent(this@BbUserSignin, CcBooksLoad::class.java))
                } else
                    startActivity(Intent(this@BbUserSignin, DdMainView::class.java))
                finish()
            }

            2 -> {
                startActivity(Intent(this@BbUserSignin, BbUserSignup::class.java))
                finish()
            }

            3 -> showFeedback(errormsg, 0)

            4 -> showFeedback(errormsg, 0)

            5 -> showFeedback("Unfortunately you can not connect to our server at the moment due to an error ", 0)
        }

    }

    fun checkDatabase() {
        if (!prefget!!.getBoolean("app_books_loaded", false))
            startActivity(Intent(this@BbUserSignin, CcBooksLoad::class.java))
        else if (prefget!!.getBoolean("app_books_loaded", false) && !prefget!!.getBoolean("app_songs_loaded", false))
            startActivity(Intent(this@BbUserSignin, CcSongsLoadX::class.java))
        else
            startActivity(Intent(this@BbUserSignin, DdMainView::class.java))
        finish()
    }

    private fun showFeedback(message: String, type: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Oops! Signing you in Failed")
        builder.setMessage(message)
        builder.setNegativeButton("Okay Got it") { arg0, arg1 -> }
        if (type == 1) {
            builder.setPositiveButton("Retry") { arg0, arg1 ->
                val code = inputCode!!.text.toString().trim { it <= ' ' }
                val phone = inputPhone!!.text.toString().trim { it <= ' ' }
                signinUser(code + phone)
            }
        }
        builder.show()
    }

    private fun showDialog(show: Boolean) {
        try {
            if (show) {
                progressDialog!!.setTitle("Signing you in")
                progressDialog!!.setMessage("Some patience please . . .")
                progressDialog!!.show()
            } else
                progressDialog!!.dismiss()
        } catch (ex: Exception) {
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bb_proceed, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_proceed -> {
                val code = inputCode!!.text.toString().trim { it <= ' ' }
                val phone = inputPhone!!.text.toString().trim { it <= ' ' }

                if (phone.isEmpty())
                    Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_LONG).show()
                else if (phone.length < 8)
                    Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_LONG).show()
                else {
                    prefedit!!.putString("user_mobile", code + phone).apply()
                    signinUser(code + phone)
                }
                return true
            }

            else -> return false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
