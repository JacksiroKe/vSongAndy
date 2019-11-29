package com.jackson_siro.visongbook.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.*;
import com.jackson_siro.visongbook.models.callback.*;
import com.jackson_siro.visongbook.retrofitconfig.API;
import com.jackson_siro.visongbook.retrofitconfig.CallJson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CcSongsLoad extends AppCompatActivity {
    private Call<CallbackPostsLists> callbackSongsCall;
    private SQLiteHelper SQLiteHelper = new SQLiteHelper(this);

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imgShow;
    private TextView txtShow, downloadTask, downloadPercent;
    private ProgressBar downloadProgress;

    private int songcount =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cc_songs_load);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        imgShow = findViewById(R.id.imgshow);
        txtShow = findViewById(R.id.txtshow);
        downloadTask = findViewById(R.id.download_task);
        downloadPercent = findViewById(R.id.download_percent);
        downloadProgress = findViewById(R.id.download_progress);

        txtShow.setText("Getting ready ...");

        requestAction();
    }

    private void requestAction(){
        swipeProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData();
            }
        }, 1000);
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipeRefreshLayout.setRefreshing(show);
            return;
        }
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(show);
            }
        });
    }

    private void requestData() {
        API api = CallJson.callJson();
        callbackSongsCall = api.PostsSelect(prefget.getString("app_selected_books", "1"));
        callbackSongsCall.enqueue(new Callback<CallbackPostsLists>() {
            @Override
            public void onResponse(Call<CallbackPostsLists> call, Response<CallbackPostsLists> response) {
                CallbackPostsLists callbackSongs = response.body();
                if (callbackSongs != null) {
                    swipeProgress(false);
                    displayApiResult(callbackSongs.data);
                }
            }

            @Override
            public void onFailure(Call<CallbackPostsLists> call, Throwable t) {
                if (!call.isCanceled()) ;
            }
        });
    }

    private void displayApiResult(final List<PostModel> songs) {
        swipeRefreshLayout.setEnabled(false);
        songcount = songs.size();
        downloadProgress.setMax(songcount);

        /*if (prefget.getBoolean("app_songs_reload", false)) {
            new AddtoDBTask().execute(songs);
        }*/

        new AddtoDBTask().execute(songs);
    }

    class AddtoDBTask extends AsyncTask<List<PostModel>, Integer, String> {
        @Override
        protected String doInBackground(List<PostModel>... lists) {
            List<PostModel> songs = lists[0];
            for (int count = 0; count < songs.size(); count ++) {
                prefedit.putBoolean("app_songs_loaded", true);
                prefedit.putBoolean("app_songs_reload", false);
                prefedit.commit();

                publishProgress(count);
                PostModel song = new PostModel();
                song.postid = songs.get(count).postid;
                song.bookid = songs.get(count).bookid;
                song.categoryid = songs.get(count).categoryid;
                song.basetype = songs.get(count).basetype;
                song.number = songs.get(count).number;
                song.alias = songs.get(count).alias;
                song.title = songs.get(count).title;
                song.tags = songs.get(count).tags;
                song.content = songs.get(count).content;
                song.created = songs.get(count).created;
                song.p_what = songs.get(count).p_what;
                song.p_when = songs.get(count).p_when;
                song.p_where = songs.get(count).p_where;
                song.netthumbs = songs.get(count).netthumbs;
                song.views = songs.get(count).views;
                song.acount = songs.get(count).acount;
                song.userid = songs.get(count).userid;
                SQLiteHelper.addSong(song);
                Log.d("MyAdapter", song.title + " added");
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            //downloadProgress.setVisibility(View.GONE);
            txtShow.setText(String.format("Thanks for your patience"));
            downloadTask.setText(String.format("We're done setting up!"));
            finishLoading();
        }
        @Override
        protected void onPreExecute() {
            txtShow.setText(String.format("We're glad you are patient enough to watch this"));
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            txtShow.setVisibility(View.GONE);
            int cursong = (values[0] + 1);
            downloadPercent.setText(((int) ((cursong / (float) songcount) * 100)) + " %");
            downloadTask.setText(String.format("Loading songs ..."));
            downloadProgress.setProgress(cursong);
        }
    }

    public void finishLoading(){
        startActivity(new Intent(CcSongsLoad.this, DdMainView.class));
        finish();
    }
}
