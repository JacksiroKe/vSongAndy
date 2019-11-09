package com.jackson_siro.visongbook.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Handler
import androidx.preference.PreferenceManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.jackson_siro.visongbook.R
import com.jackson_siro.visongbook.data.SQLiteHelper
import com.jackson_siro.visongbook.models.*
import com.jackson_siro.visongbook.models.callback.*
import com.jackson_siro.visongbook.retrofitconfig.CallJson

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CcSongsLoad : AppCompatActivity() {
    private var callbackSongsCall: Call<CallbackPostsLists>? = null
    private val SQLiteHelper = SQLiteHelper(this)

    private var prefget: SharedPreferences? = null
    private var prefedit: SharedPreferences.Editor? = null

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var imgShow: ImageView? = null
    private var txtShow: TextView? = null
    private var downloadTask: TextView? = null
    private var downloadPercent: TextView? = null
    private var downloadProgress: ProgressBar? = null

    private var songcount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cc_songs_load)

        prefget = PreferenceManager.getDefaultSharedPreferences(this)
        prefedit = prefget!!.edit()

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        imgShow = findViewById(R.id.imgshow)
        txtShow = findViewById(R.id.txtshow)
        downloadTask = findViewById(R.id.download_task)
        downloadPercent = findViewById(R.id.download_percent)
        downloadProgress = findViewById(R.id.download_progress)

        txtShow!!.text = "Getting ready ..."

        requestAction()
    }

    private fun requestAction() {
        swipeProgress(true)
        Handler().postDelayed({ requestData() }, 1000)
    }

    private fun swipeProgress(show: Boolean) {
        if (!show) {
            swipeRefreshLayout!!.isRefreshing = show
            return
        }
        swipeRefreshLayout!!.post { swipeRefreshLayout!!.isRefreshing = show }
    }

    private fun requestData() {
        val api = CallJson.callJson()
        callbackSongsCall = api.PostsSelect(prefget!!.getString("app_selected_books", "1"))
        callbackSongsCall!!.enqueue(object : Callback<CallbackPostsLists> {
            override fun onResponse(call: Call<CallbackPostsLists>, response: Response<CallbackPostsLists>) {
                val callbackSongs = response.body()
                if (callbackSongs != null) {
                    swipeProgress(false)
                    displayApiResult(callbackSongs.data)
                }
            }

            override fun onFailure(call: Call<CallbackPostsLists>, t: Throwable) {
                if (!call.isCanceled);
            }
        })
    }

    private fun displayApiResult(songs: List<PostModel>) {
        swipeRefreshLayout!!.isEnabled = false
        songcount = songs.size
        downloadProgress!!.max = songcount

        /*if (prefget.getBoolean("app_songs_reload", false)) {
            new AddtoDBTask().execute(songs);
        }*/

        AddtoDBTask().execute(songs)
    }

    inner class AddtoDBTask : AsyncTask<List<PostModel>, Int, String>() {
        override fun doInBackground(vararg lists: List<PostModel>): String {
            val songs = lists[0]
            for (count in songs.indices) {
                prefedit!!.putBoolean("app_songs_loaded", true)
                prefedit!!.putBoolean("app_songs_reload", false)
                prefedit!!.commit()

                publishProgress(count)
                val song = PostModel()
                song.postid = songs[count].postid
                song.bookid = songs[count].bookid
                song.categoryid = songs[count].categoryid
                song.basetype = songs[count].basetype
                song.number = songs[count].number
                song.alias = songs[count].alias
                song.title = songs[count].title
                song.tags = songs[count].tags
                song.content = songs[count].content
                song.created = songs[count].created
                song.p_what = songs[count].p_what
                song.p_when = songs[count].p_when
                song.p_where = songs[count].p_where
                song.netthumbs = songs[count].netthumbs
                song.views = songs[count].views
                song.acount = songs[count].acount
                song.userid = songs[count].userid
                SQLiteHelper.addSong(song)
                Log.d("MyAdapter", song.title + " added")
            }
            return "Task Completed."
        }

        override fun onPostExecute(result: String) {
            //downloadProgress.setVisibility(View.GONE);
            txtShow!!.text = String.format("Thanks for your patience")
            downloadTask!!.text = String.format("We're done setting up!")
            finishLoading()
        }

        override fun onPreExecute() {
            txtShow!!.text = String.format("We're glad you are patient enough to watch this")
        }

        override fun onProgressUpdate(vararg values: Int?) {
            txtShow!!.visibility = View.GONE
            val cursong = values[0]?.plus(1)
            downloadPercent!!.text = (cursong!! / songcount.toFloat() * 100).toInt().toString() + " %"
            downloadTask!!.text = String.format("Loading songs ...")
            if (cursong != null) {
                downloadProgress!!.progress = cursong
            }
        }
    }

    fun finishLoading() {
        startActivity(Intent(this@CcSongsLoad, DdMainView::class.java))
        finish()
    }
}
