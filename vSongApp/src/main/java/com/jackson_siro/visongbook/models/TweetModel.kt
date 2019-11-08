package com.jackson_siro.visongbook.models

import android.util.Log

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class TweetModel(val name: String, val screenName: String, val tweetText: String, private val mTweetTime: String, val profileImageUrl: String, val profileBackgroundImageUrl: String, val retweets: String, val favorites: String) {

    val tweetTime: String
        get() {
            val date: Date
            val curDate = Date()

            var time: Long
            val format = SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy")
            val formattedDate = StringBuilder()

            try {
                date = format.parse(mTweetTime)
                time = (curDate.time - date.time) / 1000

                Log.v(TweetModel::class.java.simpleName, "time= $time")

                if (time < 60) {
                    formattedDate
                            .append("< 1min")
                } else if (time < 3600) {
                    time = time / 60
                    formattedDate
                            .append(time)
                            .append("m")
                } else if (time < 86400) {
                    time = time / 3600
                    formattedDate
                            .append(time)
                            .append("h")
                } else {
                    time = time / 86400
                    formattedDate
                            .append(time)
                            .append("d")
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return formattedDate.toString()
        }

    override fun toString(): String {
        return "TweetModel{" +
                "mName='" + name + '\''.toString() +
                ", mScreenName='" + screenName + '\''.toString() +
                ", mTweetText='" + tweetText + '\''.toString() +
                ", mTweetTime='" + tweetTime + '\''.toString() +
                ", mRetweets=" + retweets +
                ", mFavorites=" + favorites +
                '}'.toString()
    }
}
