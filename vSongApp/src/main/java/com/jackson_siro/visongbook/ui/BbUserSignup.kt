package com.jackson_siro.visongbook.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager

import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.android.material.snackbar.Snackbar
import com.jackson_siro.visongbook.data.Countries
import com.jackson_siro.visongbook.models.callback.CallbackUser
import com.jackson_siro.visongbook.models.UserModel
import com.jackson_siro.visongbook.retrofitconfig.CallJson
import com.jackson_siro.visongbook.R

import java.util.ArrayList

class BbUserSignup : AppCompatActivity() {

    var inputFirstname: EditText? = null
    var inputLastname: EditText? = null
    var inputGender: EditText? = null
    var inputCity: EditText? = null
    var inputChurch: EditText? = null
    var btnProceed: Button? = null
    var progressDialog: ProgressDialog? = null
    var coordinator_layout: CoordinatorLayout? = null
    var alertDialog1: AlertDialog? = null

    var prefget: SharedPreferences? = null
    var prefedit: SharedPreferences.Editor? = null

    var toolbar: Toolbar? = null
    private val ctrlist: Countries? = null

    var Appuser: UserModel? = null
    private var usersCall: Call<CallbackUser>? = null
    var cntry: Int = 0

    private val countries = ArrayList(ctrlist!!.createSampleData())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bb_user_signup)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        prefget = this.getSharedPreferences("com.jackson_siro.visongbook", 0)
        prefedit = prefget!!.edit()

        inputFirstname = findViewById(R.id.input_firstname)
        inputLastname = findViewById(R.id.input_lastname)
        inputGender = findViewById(R.id.input_gender)
        inputCity = findViewById(R.id.input_city)
        inputChurch = findViewById(R.id.input_church)
        coordinator_layout = findViewById(R.id.coordinator_layout)
        btnProceed = findViewById(R.id.btn_proceed)

        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)

        val genderValues = arrayOf<CharSequence>("Brother", "Sister")
        inputGender!!.setOnClickListener {
            val builder = AlertDialog.Builder(this@BbUserSignup)
            builder.setTitle("You are a?")

            builder.setSingleChoiceItems(genderValues, -1) { dialog, item ->
                inputGender!!.setText(genderValues[item])
                alertDialog1!!.dismiss()
            }
            alertDialog1 = builder.create()
            alertDialog1!!.show()
        }

        btnProceed!!.setOnClickListener { view ->
            val firstname = inputFirstname!!.text.toString().trim { it <= ' ' }
            val lastname = inputLastname!!.text.toString().trim { it <= ' ' }
            val gender = inputGender!!.text.toString().trim { it <= ' ' }
            val city = inputCity!!.text.toString().trim { it <= ' ' }
            val church = inputChurch!!.text.toString().trim { it <= ' ' }

            if (firstname.isEmpty())
                Snackbar.make(view, "Your first name is important!", Snackbar.LENGTH_SHORT).show()
            else if (!isValid(firstname, lastname))
                Snackbar.make(view, "Your first name is invalid!", Snackbar.LENGTH_SHORT).show()
            else if (lastname.isEmpty())
                Snackbar.make(view, "Your last name is important too!", Snackbar.LENGTH_SHORT).show()
            else if (!isValid(lastname, firstname))
                Snackbar.make(view, "Your last name is invalid!", Snackbar.LENGTH_SHORT).show()
            else if (gender.isEmpty())
                Snackbar.make(view, "Being a brother or a sister is important to us!", Snackbar.LENGTH_SHORT).show()
            else if (city.isEmpty())
                Snackbar.make(view, "Your locality matters to us too!", Snackbar.LENGTH_SHORT).show()
            else if (!isValid(city, firstname))
                Snackbar.make(view, "Your city is invalid!", Snackbar.LENGTH_SHORT).show()
            else if (church.isEmpty())
                Snackbar.make(view, "You can't afford to be without a church!", Snackbar.LENGTH_SHORT).show()
            else if (!isValid(church, firstname))
                Snackbar.make(view, "Your church is invalid!", Snackbar.LENGTH_SHORT).show()
            else
                signupUser()
        }

    }

    private fun isValid(checkStr: String, checkStr1: String): Boolean {
        var checkStr = checkStr
        var checkStr1 = checkStr1
        checkStr = checkStr.trim { it <= ' ' }.toUpperCase()
        checkStr1 = checkStr1.trim { it <= ' ' }.toUpperCase()

        return if (checkStr.length < 2)
            false
        else if (checkStr === checkStr1)
            false
        else if (checkStr.contains("aaa"))
            false
        else if (checkStr.contains("bbb"))
            false
        else if (checkStr.contains("ccc"))
            false
        else if (checkStr.contains("ddd"))
            false
        else if (checkStr.contains("eee"))
            false
        else if (checkStr.contains("fff"))
            false
        else if (checkStr.contains("ggg"))
            false
        else if (checkStr.contains("hhh"))
            false
        else if (checkStr.contains("iii"))
            false
        else if (checkStr.contains("jjj"))
            false
        else if (checkStr.contains("kkk"))
            false
        else if (checkStr.contains("lll"))
            false
        else if (checkStr.contains("mmm"))
            false
        else if (checkStr.contains("nnn"))
            false
        else if (checkStr.contains("ooo"))
            false
        else if (checkStr.contains("ppp"))
            false
        else if (checkStr.contains("qqq"))
            false
        else if (checkStr.contains("rrr"))
            false
        else if (checkStr.contains("sss"))
            false
        else if (checkStr.contains("ttt"))
            false
        else if (checkStr.contains("uuu"))
            false
        else if (checkStr.contains("vvv"))
            false
        else if (checkStr.contains("www"))
            false
        else if (checkStr.contains("xxx"))
            false
        else if (checkStr.contains("yyy"))
            false
        else
            true
    }

    private fun signupUser() {
        showDialog(true)
        val country = prefget!!.getString("user_country_ccode", "")
        val mobile = prefget!!.getString("user_mobile", "")
        val firstname = inputFirstname!!.text.toString().trim { it <= ' ' }
        val lastname = inputLastname!!.text.toString().trim { it <= ' ' }
        val gender = inputGender!!.text.toString().trim { it <= ' ' }
        val city = inputCity!!.text.toString().trim { it <= ' ' }
        val church = inputChurch!!.text.toString().trim { it <= ' ' }

        val api = CallJson.callJson()
        usersCall = api.UserSignup(firstname, lastname, country, mobile, gender, city, church)
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
                } //else apiResult(5, "null response");
            }

            override fun onFailure(call: Call<CallbackUser>, t: Throwable) {
                showDialog(false)
                //apiResult(5, t.getMessage());
            }

        })
    }

    private fun handleUserData() {
        for (i in countries.indices) {
            if (countries[i].ccode == Appuser!!.country) {
                cntry = i
                break
            }
        }
        prefedit!!.putString("user_country_name", countries[cntry].country).apply()
        prefedit!!.putString("user_country_icode", countries[cntry].icode).apply()
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

        prefedit!!.putBoolean("app_user_signedin", true)
        startActivity(Intent(this@BbUserSignup, DdMainView::class.java))
        finish()
    }

    private fun apiResult(errorno: String, errormsg: String) {
        val firstname = inputFirstname!!.text.toString().trim { it <= ' ' }
        val lastname = inputLastname!!.text.toString().trim { it <= ' ' }
        val gender = inputGender!!.text.toString().trim { it <= ' ' }
        val city = inputCity!!.text.toString().trim { it <= ' ' }
        val church = inputChurch!!.text.toString().trim { it <= ' ' }

        prefedit!!.putString("user_firstname", firstname)
        prefedit!!.putString("user_lastname", lastname)
        prefedit!!.putString("user_gender", gender)
        prefedit!!.putString("user_city", city)
        prefedit!!.putString("user_church", church)
        prefedit!!.putBoolean("app_user_signedin", false)
        prefedit!!.apply()

        when (Integer.parseInt(errorno)) {
            0 -> showFeedback(errormsg, 0)

            1 -> checkDatabase()

            2 -> {
                startActivity(Intent(this@BbUserSignup, BbUserSignup::class.java))
                finish()
            }

            3 -> showFeedback(errormsg, 0)

            5 -> showFeedback("Unfortunately you can not connect to our server at the moment due to the error of:\n\n" + errormsg +
                    "\n\n Kindly take a screenshot of this error, a screenshot of your About Phone (Under Settings) and email " +
                    "it the developers on: appsmatake@gmail.com with a simple report of what you were doing " +
                    "before the error occurred", 0)
        }

    }

    fun checkDatabase() {
        if (!prefget!!.getBoolean("app_books_loaded", false))
            startActivity(Intent(this@BbUserSignup, CcBooksLoad::class.java))
        else if (prefget!!.getBoolean("app_books_loaded", false) && !prefget!!.getBoolean("app_songs_loaded", false))
            startActivity(Intent(this@BbUserSignup, CcSongsLoadX::class.java))
        else
            startActivity(Intent(this@BbUserSignup, DdMainView::class.java))
        finish()
    }

    private fun showFeedback(message: String, type: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Oops! Signing you up Failed")
        builder.setMessage(message)
        builder.setNegativeButton("Okay Got it") { arg0, arg1 -> }
        if (type == 1) {
            builder.setPositiveButton("Retry") { arg0, arg1 -> signupUser() }
        }
        builder.show()
    }

    private fun showDialog(show: Boolean) {
        if (show) {
            progressDialog!!.setTitle("Signing you up")
            progressDialog!!.setMessage("Some patience please . . .")
            progressDialog!!.show()
        } else
            progressDialog!!.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bb_proceed, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_proceed -> {
                signupUser()
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
