package com.jackson_siro.visongbook.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.snackbar.Snackbar;
import com.jackson_siro.visongbook.data.Countries;
import com.jackson_siro.visongbook.models.callback.CallbackUser;
import com.jackson_siro.visongbook.models.CountryModel;
import com.jackson_siro.visongbook.models.UserModel;
import com.jackson_siro.visongbook.retrofitconfig.API;
import com.jackson_siro.visongbook.retrofitconfig.CallJson;
import com.jackson_siro.visongbook.R;

import java.util.ArrayList;

public class BbUserSignup extends AppCompatActivity {

    private EditText inputFirstname, inputLastname, inputGender, inputCity, inputChurch;
    private Button btnProceed;
    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinator_layout;
    private AlertDialog alertDialog1;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private Toolbar toolbar;
    private Countries ctrlist = null;

    private UserModel Appuser;
    private Call<CallbackUser> usersCall;
    private int cntry;

    private ArrayList<CountryModel> countries = new ArrayList<CountryModel>(ctrlist.createSampleData());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bb_user_signup);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        inputFirstname = findViewById(R.id.input_firstname);
        inputLastname = findViewById(R.id.input_lastname);
        inputGender = findViewById(R.id.input_gender);
        inputCity = findViewById(R.id.input_city);
        inputChurch = findViewById(R.id.input_church);
        coordinator_layout = findViewById(R.id.coordinator_layout);
        btnProceed = findViewById(R.id.btn_proceed);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        final CharSequence[] genderValues = {"Brother","Sister"};
        inputGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BbUserSignup.this);
            builder.setTitle("You are a?");

            builder.setSingleChoiceItems(genderValues, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    inputGender.setText(genderValues[item]);
                    alertDialog1.dismiss();
                }
            });
            alertDialog1 = builder.create();
            alertDialog1.show();
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = inputFirstname.getText().toString().trim();
                String lastname = inputLastname.getText().toString().trim();
                String gender = inputGender.getText().toString().trim();
                String city = inputCity.getText().toString().trim();
                String church = inputChurch.getText().toString().trim();

                if (firstname.isEmpty()) Snackbar.make(view, "Your first name is important!", Snackbar.LENGTH_SHORT).show();
                else if (!isValid(firstname, lastname))
                    Snackbar.make(view, "Your first name is invalid!", Snackbar.LENGTH_SHORT).show();

                else if (lastname.isEmpty()) Snackbar.make(view, "Your last name is important too!", Snackbar.LENGTH_SHORT).show();
                else if (!isValid(lastname, firstname)) Snackbar.make(view, "Your last name is invalid!", Snackbar.LENGTH_SHORT).show();

                else if (gender.isEmpty()) Snackbar.make(view, "Being a brother or a sister is important to us!", Snackbar.LENGTH_SHORT).show();

                else if (city.isEmpty()) Snackbar.make(view, "Your locality matters to us too!", Snackbar.LENGTH_SHORT).show();
                else if (!isValid(city, firstname)) Snackbar.make(view, "Your city is invalid!", Snackbar.LENGTH_SHORT).show();

                else if (church.isEmpty()) Snackbar.make(view, "You can't afford to be without a church!", Snackbar.LENGTH_SHORT).show();
                else if (!isValid(church, firstname)) Snackbar.make(view, "Your church is invalid!", Snackbar.LENGTH_SHORT).show();

                else signupUser();
            }
        });

    }

    private boolean isValid(String checkStr, String checkStr1){
        checkStr = checkStr.trim().toUpperCase();
        checkStr1 = checkStr1.trim().toUpperCase();

        if (checkStr.length() < 2) return false;
        else if (checkStr == checkStr1) return false;
        else if (checkStr.contains("aaa")) return false;
        else if (checkStr.contains("bbb")) return false;
        else if (checkStr.contains("ccc")) return false;
        else if (checkStr.contains("ddd")) return false;
        else if (checkStr.contains("eee")) return false;
        else if (checkStr.contains("fff")) return false;
        else if (checkStr.contains("ggg")) return false;
        else if (checkStr.contains("hhh")) return false;
        else if (checkStr.contains("iii")) return false;
        else if (checkStr.contains("jjj")) return false;
        else if (checkStr.contains("kkk")) return false;
        else if (checkStr.contains("lll")) return false;
        else if (checkStr.contains("mmm")) return false;
        else if (checkStr.contains("nnn")) return false;
        else if (checkStr.contains("ooo")) return false;
        else if (checkStr.contains("ppp")) return false;
        else if (checkStr.contains("qqq")) return false;
        else if (checkStr.contains("rrr")) return false;
        else if (checkStr.contains("sss")) return false;
        else if (checkStr.contains("ttt")) return false;
        else if (checkStr.contains("uuu")) return false;
        else if (checkStr.contains("vvv")) return false;
        else if (checkStr.contains("www")) return false;
        else if (checkStr.contains("xxx")) return false;
        else if (checkStr.contains("yyy")) return false;
        else return true;
    }

    private void signupUser() {
        showDialog(true);
        String country = prefget.getString("user_country_ccode", "");
        String mobile = prefget.getString("user_mobile", "");
        String firstname = inputFirstname.getText().toString().trim();
        String lastname = inputLastname.getText().toString().trim();
        String gender = inputGender.getText().toString().trim();
        String city = inputCity.getText().toString().trim();
        String church = inputChurch.getText().toString().trim();

        API api = CallJson.callJson();
        usersCall = api.UserSignup(firstname, lastname, country, mobile, gender, city, church);
        usersCall.enqueue(new Callback<CallbackUser>() {
            @Override
            public void onResponse(Call<CallbackUser> call, Response<CallbackUser> response) {
                showDialog(false);
                CallbackUser cl = response.body();
                if (cl != null){
                    Appuser = cl.data;
                    if (cl.data.success == "1") handleUserData();
                    else apiResult(cl.data.success, cl.data.message);
                } //else apiResult(5, "null response");
            }

            @Override
            public void onFailure(Call<CallbackUser> call, Throwable t) {
                showDialog(false);
                //apiResult(5, t.getMessage());
            }

        });
    }

    private void handleUserData() {
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCcode().equals(Appuser.country)) {
                cntry = i;
                break;
            }
        }
        prefedit.putString("user_country_name", countries.get(cntry).getCountry()).apply();
        prefedit.putString("user_country_icode", countries.get(cntry).getIcode()).apply();
        prefedit.putString("user_country_ccode", Appuser.country).apply();

        prefedit.putInt("user_userid", Integer.parseInt(Appuser.userid)).apply();
        //prefedit.putString("user_uniqueid", Appuser.uid).apply();
        prefedit.putString("user_firstname", Appuser.firstname).apply();
        prefedit.putString("user_lastname", Appuser.lastname);
        prefedit.putString("user_mobile", Appuser.mobile).apply();
        prefedit.putString("user_gender", Appuser.gender).apply();
        prefedit.putString("user_city", Appuser.city).apply();
        prefedit.putString("user_church", Appuser.church).apply();
        prefedit.putString("user_email", Appuser.email).apply();
        prefedit.putString("user_level", Appuser.level).apply();
        prefedit.putString("user_handle", Appuser.handle).apply();
        prefedit.putString("user_created", Appuser.created).apply();
        prefedit.putString("user_signedin", Appuser.signedin).apply();
        prefedit.putString("user_points", Appuser.points).apply();
        prefedit.putString("user_wallposts", Appuser.wallposts).apply();

        prefedit.putBoolean("app_user_signedin", true).apply();

        prefedit.putBoolean("app_user_signedin", true);
        startActivity(new Intent(BbUserSignup.this, DdMainView.class));
        finish();
    }

    private void apiResult(final String errorno, final String errormsg){
        String firstname = inputFirstname.getText().toString().trim();
        String lastname = inputLastname.getText().toString().trim();
        String gender = inputGender.getText().toString().trim();
        String city = inputCity.getText().toString().trim();
        String church = inputChurch.getText().toString().trim();

        prefedit.putString("user_firstname", firstname);
        prefedit.putString("user_lastname", lastname);
        prefedit.putString("user_gender", gender);
        prefedit.putString("user_city", city);
        prefedit.putString("user_church", church);
        prefedit.putBoolean("app_user_signedin", false);
        prefedit.apply();

        switch (Integer.parseInt(errorno)) {
            case 0:
                showFeedback(errormsg, 0);
                break;

            case 1:
                checkDatabase();
                break;

            case 2:
                startActivity(new Intent(BbUserSignup.this, BbUserSignup.class));
                finish();
                break;

            case 3:
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
        if (!prefget.getBoolean("app_books_loaded", false)) startActivity(new Intent(BbUserSignup.this, CcBooksLoad.class));
        else if (prefget.getBoolean("app_books_loaded", false) && !prefget.getBoolean("app_songs_loaded", false))
            startActivity(new Intent(BbUserSignup.this, CcSongsLoad.class));
        else startActivity(new Intent(BbUserSignup.this, DdMainView.class));
        finish();
    }

    private void showFeedback(String message, int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Oops! Signing you up Failed");
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
                    signupUser();
                }
            });
        }
        builder.show();
    }

    private void showDialog(final boolean show){
        if (show){
            progressDialog.setTitle("Signing you up");
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
               signupUser();
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
