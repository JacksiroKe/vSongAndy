package com.jackson_siro.visongbook.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import com.google.android.material.snackbar.Snackbar;
import com.jackson_siro.visongbook.components.SearchDialogCompat;
import com.jackson_siro.visongbook.data.Countries;
import com.jackson_siro.visongbook.models.Callback.*;
import com.jackson_siro.visongbook.models.*;
import com.jackson_siro.visongbook.retrofitconfig.API;
import com.jackson_siro.visongbook.retrofitconfig.*;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.core.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BbUserSignin extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private UserModel Appuser;
    private Call<CallbackUser> usersCall;

    private Toolbar toolbar;
    private Button btnProceed;
    private EditText inputCountry, inputCode, inputPhone;
    private int cntry;

    private ArrayList<CountryModel> countries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bb_user_signin);
        countries = new ArrayList<CountryModel>(Countries.createSampleData());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        inputCountry = findViewById(R.id.input_country);
        inputCode = findViewById(R.id.input_code);
        inputPhone = findViewById(R.id.input_phone);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        inputCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryDialog();
            }
        });

        inputCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryDialog();
            }
        });

        setDefaults();

        btnProceed = findViewById(R.id.btn_proceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = inputCode.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();

                //int phoneno = Integer.parseInt(phone);
                if (phone.isEmpty())
                    Snackbar.make(view, "Please enter your mobile number", Snackbar.LENGTH_SHORT).show();
                else if (phone.length() < 8)
                    Snackbar.make(view, "Please enter a valid mobile number", Snackbar.LENGTH_SHORT).show();
                else if (phone.startsWith("0"))
                    Snackbar.make(view, "Please omit the first zero in your mobile number", Snackbar.LENGTH_SHORT).show();
                else {
                    prefedit.putString("user_mobile", code + phone).apply();
                    signinUser(code + phone);
                }
            }
        });
    }

    private void setDefaults(){
        if (prefget.getString("user_country_name", "").isEmpty()) {
            prefedit.putString("user_country_name", "Kenya").apply();
            prefedit.putString("user_country_icode", "254").apply();
            prefedit.putString("user_country_ccode", "KE").apply();
            inputCountry.setText("Kenya");
            inputCode.setText("254");
        } else {
            String icode = prefget.getString("user_country_icode", "");
            for (int i = 0; i < countries.size(); i++) {
                if (countries.get(i).getIcode().equals(icode)) {
                    cntry = i;
                    break;
                }
            }

            String mobile = prefget.getString("user_mobile", "");
            String[] mobilesaved = TextUtils.split(mobile, icode);

            inputCountry.setText(prefget.getString("user_country_name", ""));
            inputCode.setText(prefget.getString("user_country_icode", ""));
            inputCode.setText(countries.get(cntry).getIcode());
            //inputPhone.setText(mobilesaved[1]);
        }
    }

    private void countryDialog(){
        SearchDialogCompat<CountryModel> dialog = new SearchDialogCompat<CountryModel>(BbUserSignin.this,
            "Which is your Country?", "Search for your country", null, countries, new SearchResultListener<CountryModel>() {
                @Override
                public void onSelected( BaseSearchDialogCompat dialog, CountryModel country, int position )
                {
                    inputCountry.setText(country.getCountry());
                    inputCode.setText(country.getIcode());
                    prefedit.putString("user_country_name", country.getCountry()).apply();
                    prefedit.putString("user_country_icode", country.getIcode()).apply();
                    prefedit.putString("user_country_ccode", country.getCcode()).apply();
                    dialog.dismiss();
                }
            }
        );
        dialog.show();
        dialog.getSearchBox().setTypeface(Typeface.SERIF);
    }

    private void signinUser(final String mobile) {
        showDialog(true);
        API api = CallJson.callJson();
        usersCall = api.UserSignin(mobile);
        usersCall.enqueue(new Callback<CallbackUser>() {
            @Override
            public void onResponse(Call<CallbackUser> call, Response<CallbackUser> response) {
                showDialog(false);
                CallbackUser cl = response.body();
                if (cl != null){
                    Appuser = cl.data;
                    if (cl.data.success == 1) handleUserData();
                    else apiResult(cl.data.success, cl.data.message);
                } else apiResult(5, "null response");
            }

            @Override
            public void onFailure(Call<CallbackUser> call, Throwable t) {
                showDialog(false);
                apiResult(5, t.getMessage());
            }

        });
    }

    private void handleUserData() {
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCcode() == Appuser.country) {
                cntry = 1;
            }
        }

        prefedit.putString("user_country_name", countries.get(cntry).getCountry()).apply();
        prefedit.putString("user_country_icode", countries.get(cntry).getIcode()).apply();
        prefedit.putString("user_country_ccode", Appuser.country).apply();

        prefedit.putInt("user_userid", Appuser.userid).apply();
        //prefedit.putString("user_uniqueid", Appuser.uid).apply();
        prefedit.putString("user_firstname", Appuser.firstname).apply();
        prefedit.putString("user_lastname", Appuser.lastname);
        prefedit.putString("user_mobile", Appuser.mobile).apply();
        prefedit.putInt("user_gender", Appuser.gender).apply();
        prefedit.putString("user_city", Appuser.city).apply();
        prefedit.putString("user_church", Appuser.church).apply();
        prefedit.putString("user_email", Appuser.email).apply();
        prefedit.putString("user_level", Appuser.level).apply();
        prefedit.putString("user_handle", Appuser.handle).apply();
        prefedit.putString("user_created", Appuser.created).apply();
        prefedit.putString("user_signedin", Appuser.signedin).apply();
        prefedit.putInt("user_points", Appuser.points).apply();
        prefedit.putInt("user_wallposts", Appuser.wallposts).apply();

        prefedit.putBoolean("app_user_signedin", true).apply();

        if (!prefget.getBoolean("app_books_loaded", false)) {
            startActivity(new Intent(BbUserSignin.this, CcBooksLoad.class));
        }
        else startActivity(new Intent(BbUserSignin.this, DdHomeView.class));
        finish();
    }

    private void apiResult(final int errorno, final String errormsg){
        prefedit.putBoolean("app_user_signedin", false);
        prefedit.apply();

        switch (errorno) {
            case 1:
                if (!prefget.getBoolean("app_books_loaded", false)) {
                    startActivity(new Intent(BbUserSignin.this, CcBooksLoad.class));
                }
                else if (prefget.getBoolean("app_books_reload", false)) {
                    startActivity(new Intent(BbUserSignin.this, CcBooksLoad.class));
                }
                else startActivity(new Intent(BbUserSignin.this, DdHomeView.class));
                finish();
                break;

            case 2:
                startActivity(new Intent(BbUserSignin.this, BbUserSignup.class));
                finish();
                break;

            case 3:
                showFeedback(errormsg, 0);
                break;

            case 4:
                showFeedback(errormsg, 0);
                break;

            case 5:
                showFeedback("Unfortunately you can not connect to our server at the moment due to the error of:\n\n" + errormsg +
                        "\n\n Kindly take a screenshot of this error, a screenshot of your About Phone (Under Settings) and email " +
                        "it the developers on: appsmatake@gmail.com with a simple report of what you were doing " +
                        "before the error occurred", 0);
        }

    }

    public void checkDatabase() {
        if (!prefget.getBoolean("app_books_loaded", false)) startActivity(new Intent(BbUserSignin.this, CcBooksLoad.class));
        else if (prefget.getBoolean("app_books_loaded", false) && !prefget.getBoolean("app_songs_loaded", false))
            startActivity(new Intent(BbUserSignin.this, CcSongsLoad.class));
        else startActivity(new Intent(BbUserSignin.this, DdHomeView.class));
        finish();
    }

    private void showFeedback(String message, int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Oops! Signing you in Failed");
        builder.setMessage(message);
        builder.setNegativeButton("Okay Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        if (type == 1) {
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    String code = inputCode.getText().toString().trim();
                    String phone = inputPhone.getText().toString().trim();
                    signinUser(code + phone);
                }
            });
        }
        builder.show();
    }

    private void showDialog(final boolean show){
        if (show){
            progressDialog.setTitle("Signing you in");
            progressDialog.setMessage("Some patience please . . .");
            progressDialog.show();
        }
        else progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.bb_proceed, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_proceed:
                String code = inputCode.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();

                if (phone.isEmpty())
                    Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_LONG).show();
                else if (phone.length() < 8)
                    Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_LONG).show();
                else {
                    prefedit.putString("user_mobile", code + phone).apply();
                    signinUser(code + phone);
                }
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
