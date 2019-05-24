package com.jackson_siro.visongbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.Callback.CallbackApp;
import com.jackson_siro.visongbook.models.AppModel;
import com.jackson_siro.visongbook.retrofitconfig.API;
import com.jackson_siro.visongbook.retrofitconfig.BaseUrlConfig;
import com.jackson_siro.visongbook.retrofitconfig.CallJsonX;
import com.jackson_siro.visongbook.setting.CheckNetwork;
import com.jackson_siro.visongbook.setting.CheckVersion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppStart extends AppCompatActivity {
    private TextView mytext;
    private ImageView myimage;

    private long ms=0, splashTime=5000;
    private boolean splashActive = true, paused=false;
    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private AppModel Appdata;
    private Call<CallbackApp> appCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();
        mytext = findViewById(R.id.text);
        myimage = findViewById(R.id.image);

        setFirstUse();

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        myimage.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade1);
        mytext.startAnimation(animation2);

        Thread mythread = new Thread() {
            public void run() {
            try {
                while (splashActive && ms < splashTime) {
                    if(!paused)
                        ms=ms+100;
                    sleep(100);
                }
            } catch(Exception e) {}
            finally {
                checkSession();
            }
            }
        };
        mythread.start();
    }

    public void checkSession() {
        if (CheckNetwork.isConnectCheck(this)) CheckVersion.isOutdatedCheck(this);

        if (!prefget.getBoolean("app_user_signedin", false)) {
            startActivity(new Intent(AppStart.this, BbUserSignin.class));
        }
        else {
            if (!prefget.getBoolean("app_books_loaded", false)) startActivity(new Intent(AppStart.this, BbBooksLoad.class));
            else if (prefget.getBoolean("app_books_reload", false)) startActivity(new Intent(AppStart.this, BbBooksLoad.class));
            else if (prefget.getBoolean("app_books_loaded", false) && !prefget.getBoolean("app_songs_loaded", false))
                startActivity(new Intent(AppStart.this, BbSongsLoad.class));
            else startActivity(new Intent(AppStart.this, CcHomeView.class));
        }

        finish();
    }

    public void setFirstUse() {
        if (!prefget.getBoolean("app_first_use", false)) {
<<<<<<< HEAD
            prefedit.putBoolean("app_first_use", true).apply();
            prefedit.putBoolean("app_user_signedin", false).apply();
            prefedit.putBoolean("app_user_donated", false).apply();
            prefedit.putLong("app_first_data", System.currentTimeMillis()).apply();
=======
            prefedit.putBoolean("app_first_use", true);
            prefedit.putBoolean("app_user_signedin", false);
            prefedit.putLong("app_first_data", System.currentTimeMillis());
            prefedit.commit();
>>>>>>> c0aed1253c303a33b706b3212d96bfbef706cfcc
        }
    }
}