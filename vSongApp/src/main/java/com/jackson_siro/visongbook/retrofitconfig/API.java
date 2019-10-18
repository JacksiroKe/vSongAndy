package com.jackson_siro.visongbook.retrofitconfig;

import com.jackson_siro.visongbook.models.Callback.*;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {
    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "User-Agent: vSongBook";

    String AccessKeyString = "?accesskey=";
    String AccessKeyValue = "AppSmataKey"; // change accesskey with you want, this accesskey must same with your accesskey in admin panel

    @Headers({CACHE, AGENT}) @GET(BaseUrlConfig.AppChecker) Call<CallbackApp> AppChecker();

    @Headers({CACHE, AGENT}) @GET(BaseUrlConfig.BooksSelect) Call<CallbackCategory> BooksSelect();

    @FormUrlEncoded
    @POST(BaseUrlConfig.UserLastseen)
    Call<CallbackUser> UserLastseen(
            @Field("userid") String userid
    );

    @FormUrlEncoded
    @POST(BaseUrlConfig.UserSignin)
    Call<CallbackUser> UserSignin(
            @Field("mobile") String mobile
    );

    /*@Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.UserSignin)
    Call<CallbackUser> UserSignin(
            @Query("mobile") String mobile
    );*/

    @FormUrlEncoded
    @POST(BaseUrlConfig.UserSignup)
    Call<CallbackUser> UserSignup(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("country") String country,
            @Field("mobile") String mobile,
            @Field("gender") String gender,
            @Field("city") String city,
            @Field("church") String church
    );

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.PostsLists)
    Call<CallbackPostsLists> PostsLists(
            @Query("total") int total,
            @Query("start") int start,
            @Query("sort") String sort
    );

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.PostsSelect)
    Call<CallbackPostsLists> PostsSelect(
            @Query("books") String books
    );

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.PostsSingle + AccessKeyString + AccessKeyValue)
    Call<CallbackPostSingle> PostsSingle(
            @Query("postid") int postid
    );

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.PostsAnswers + AccessKeyString + AccessKeyValue)
    Call<CallbackPostsLists> PostsAnswers(
            @Query("postid") int postid,
            @Query("total") int total,
            @Query("page") int page
    );

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.PostsSearch)
    Call<CallbackPostsSearch> PostsSearch(
            @Query("keyword") String keyword
    );

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.PostsSlider + AccessKeyString + AccessKeyValue)
    Call<CallbackPostsSlider> PostsSlider();

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.PostsByCategory + AccessKeyString + AccessKeyValue)
    Call<CallbackPostsByCategory> getPostsCategory(
            @Query("category") int categoryid
    );

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.CategoriesAll + AccessKeyString + AccessKeyValue)
    Call<CallbackCategory> getCategory();

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.ShowComment + AccessKeyString + AccessKeyValue)
    Call<CallbackShowComment> getShowComment(
            @Query("postid") int postid
    );

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.CountComment + AccessKeyString + AccessKeyValue)
    Call<CallbackCountComment> getCountComment(
            @Query("postid") long postid
    );

    @Headers({CACHE, AGENT})
    @GET(BaseUrlConfig.BackgroundDrawer + AccessKeyString + AccessKeyValue)
    Call<CallbackBackgroundDrawer> getImageDrawer(

    );

    @FormUrlEncoded
    @POST(BaseUrlConfig.Feedback)
    Call<FeedbackModal> feedBack(
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("gender") String gender,
            @Field("city") String city,
            @Field("country") String country,
            @Field("txt_feed") String txt_feed

    );
}
