package com.jackson_siro.visongbook.ui;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.Callback.CallbackPostsLists;
import com.jackson_siro.visongbook.models.PostModel;
import com.jackson_siro.visongbook.retrofitconfig.API;
import com.jackson_siro.visongbook.retrofitconfig.CallJson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BbSongsLoadBackup extends AppCompatActivity {
    private Call<CallbackPostsLists> callbackSongsCall;
    private SQLiteHelper SQLiteHelper = new SQLiteHelper(this);
    private final Handler handler = new Handler();

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout download_layout;
    private TextView coolText, downloadSize, downloadPercent;
    private ProgressBar downloadProgress;

    private int progressStatus = 0;
    int songcount =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bb_songs_load);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        download_layout = findViewById(R.id.download_layout);
        coolText = findViewById(R.id.cool_text);
        downloadSize = findViewById(R.id.download_size);
        downloadPercent = findViewById(R.id.download_percent);
        downloadProgress = findViewById(R.id.download_progress);

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
        callbackSongsCall = api.PostsSelect("1");
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
        swipeRefreshLayout.setVisibility(View.GONE);
        download_layout.setVisibility(View.VISIBLE);
        songcount = songs.size();

        new AddtoDBTask().execute(songs);

        /*for (int i = 0; i < songcount; i ++) {
            //int progress = 100 * (i / songcount);
            //downloadProgress.setProgress(progress);
            //downloadPercent.setText(progress + " %");
            //downloadSize.setText((progress + 1) + " of " + songcount);

            PostModel song = new PostModel();
            song.postid = songs.get(i).postid;
            song.bookid = songs.get(i).bookid;
            song.categoryid = songs.get(i).categoryid;
            song.basetype = songs.get(i).basetype;
            song.number = songs.get(i).number;
            song.alias = songs.get(i).alias;
            song.title = songs.get(i).title;
            song.tags = songs.get(i).tags;
            song.content = songs.get(i).content;
            song.created = songs.get(i).created;
            song.what = songs.get(i).what;
            song.when = songs.get(i).when;
            song.when = songs.get(i).when;
            song.when = songs.get(i).when;
            song.when = songs.get(i).when;
            song.where = songs.get(i).where;
            song.netthumbs = songs.get(i).netthumbs;
            song.views = songs.get(i).views;
            song.acount = songs.get(i).acount;
            song.userid = songs.get(i).userid;
            //SQLiteHelper.addSong(song);
            Log.d("MyAdapter", song.title + " added");
        }*/
    }

    private class AddtoDBTask extends AsyncTask<List<PostModel>, Void, String> {

        @Override
        protected String doInBackground(List<PostModel>... lists) {
            List<PostModel> songs = lists[0];
            for (int i = 0; i < songcount; i ++) {
                int progress = 100 * (i / songcount);
                downloadProgress.setProgress(progress);
                downloadPercent.setText(progress + " %");
                downloadSize.setText((progress + 1) + " of " + songcount);

                PostModel song = new PostModel();
                song.postid = songs.get(i).postid;
                song.bookid = songs.get(i).bookid;
                song.categoryid = songs.get(i).categoryid;
                song.basetype = songs.get(i).basetype;
                song.number = songs.get(i).number;
                song.alias = songs.get(i).alias;
                song.title = songs.get(i).title;
                song.tags = songs.get(i).tags;
                song.content = songs.get(i).content;
                song.created = songs.get(i).created;
                song.what = songs.get(i).what;
                song.when = songs.get(i).when;
                song.when = songs.get(i).when;
                song.when = songs.get(i).when;
                song.when = songs.get(i).when;
                song.where = songs.get(i).where;
                song.netthumbs = songs.get(i).netthumbs;
                song.views = songs.get(i).views;
                song.acount = songs.get(i).acount;
                song.userid = songs.get(i).userid;
                //SQLiteHelper.addSong(song);
                Log.d("MyAdapter", song.title + " added");
            }
            return "Executed!";
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

        @Override
        protected void onPostExecute(String result) {

        }

    }
    /*private class AddtoDBTaskx extends AsyncTask<String, Void, String> {
        int progress = 100 * (i / songcount);
        //downloadProgress.setProgress(progress);
        //downloadPercent.setText(progress + " %");
        //downloadSize.setText((progress + 1) + " of " + songcount);
        protected void onPreExecute(){
            downloadProgress.setVisibility(ProgressBar.VISIBLE);
        }

        protected String doInBackground(String... params){

            return "Executed";
        }

        protected void onProgressUpdate(String... values){
            downloadProgress.setProgress(values[0]);
        }

        protected void onPostExecute(Bitmap result){
            downloadProgress.setVisibility(ProgressBar.INVISIBLE);
        }
    }*/

}
