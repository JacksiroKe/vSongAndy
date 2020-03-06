package com.jackson_siro.visongbook.ui;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.jackson_siro.visongbook.R;

public class vSongBook extends AppCompatActivity {

    private static final String SONG_ID = "key.EXTRA_OBJ_ID", SERMON_ID = "key.EXTRA_OBJ_ID";

    public static void passingIntent(Activity activity, String postid, String request){
        if ("ViewSong".equals(request)) {
            Intent intent = new Intent(activity, EePostView.class);
            intent.putExtra(SONG_ID, postid);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.animation_bottom_top, R.anim.animation_blank);
        }

        else if ("ViewNote".equals(request)) {
            Intent intent = new Intent(activity, EeNoteView.class);
            intent.putExtra(SONG_ID, postid);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.animation_bottom_top, R.anim.animation_blank);
        }

        else if ("ViewSermon".equals(request)) {
            Intent intent = new Intent(activity, EeSermonView.class);
            intent.putExtra(SERMON_ID, postid);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.animation_bottom_top, R.anim.animation_blank);
        }

    }
}
