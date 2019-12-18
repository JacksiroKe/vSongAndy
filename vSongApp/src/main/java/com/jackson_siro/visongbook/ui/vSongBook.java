package com.jackson_siro.visongbook.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.jackson_siro.visongbook.R;

public class vSongBook extends AppCompatActivity {

    private static final String SONG_ID = "key.EXTRA_OBJ_ID";

    public static void passingIntent(Activity activity, String postid, String request){
        SharedPreferences prefget = PreferenceManager.getDefaultSharedPreferences(activity);
        if ("ViewSong".equals(request)) {
            Intent intent = new Intent(activity, EePostView.class);
            intent.putExtra(SONG_ID, postid);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.animation_bottom_top, R.anim.animation_blank);
        }

        /*if ("ViewSong".equals(request)) {
            if (prefget.getString("app_song_presentation", "slides") == "sliders")
            {
                Intent intent = new Intent(activity, EePostSlider.class);
                intent.putExtra(SONG_ID, postid);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.animation_bottom_top, R.anim.animation_blank);
            }

            else if (prefget.getString("app_song_presentation", "slides") == "scroll")
            {
                Intent intent = new Intent(activity, EePostSlider.class);
                intent.putExtra(SONG_ID, postid);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.animation_bottom_top, R.anim.animation_blank);
            }
        }*/
    }
}
