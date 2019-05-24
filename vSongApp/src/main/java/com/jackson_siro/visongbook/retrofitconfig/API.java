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

    String AppChecker = "vsongbook.json";
    String PostsLists = "as-api/posts-lists.php";
    String PostsSelect = "as-api/posts-select.php";
    String PostsSingle = "as-api/posts-single.php";
    String PostsAnswers = "as-api/posts-answers.php";
    String PostsSearch = "as-api/posts-search.php";
    String PostsSlider = "as-api/posts-slider.php";
    String PostsByCategory = "as-api/posts-by-category.php";
    String CategoriesAll = "as-api/categories.php";
    String BooksSelect = "as-api/book-select.php";
    String Feedback = "as-api/feedback.php";
    String ShowComment = "as-api/comment-by-id.php";
    String CountComment = "as-api/comment-submit.php";
    String BackgroundDrawer = "as-api/bg-drawer.php";
    String UserSignin = "as-api/user-signin.php";
    String UserSignup = "as-api/user-signup.php";

    String AccessKeyString = "?accesskey=";
    String AccessKeyValue = "AppSmataKey"; // change accesskey with you want, this accesskey must same with your accesskey in admin panel

    @Headers({CACHE, AGENT}) @GET(AppChecker) Call<CallbackApp> AppChecker();

    @Headers({CACHE, AGENT}) @GET(BooksSelect) Call<CallbackCategory> BooksSelect();

    @FormUrlEncoded
    @POST(UserSignin)
    Call<CallbackUser> UserSignin(
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST(UserSignup)
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
    @GET(PostsLists)
    Call<CallbackPostsLists> PostsLists(
            @Query("total") int total,
            @Query("start") int start,
            @Query("sort") String sort
    );

    @Headers({CACHE, AGENT})
    @GET(PostsSelect)
    Call<CallbackPostsLists> PostsSelect(
            @Query("books") String books
    );

    @Headers({CACHE, AGENT})
    @GET(PostsSingle+AccessKeyString+AccessKeyValue)
    Call<CallbackPostSingle> PostsSingle(
            @Query("postid") int postid
    );

    @Headers({CACHE, AGENT})
    @GET(PostsAnswers+AccessKeyString+AccessKeyValue)
    Call<CallbackPostsLists> PostsAnswers(
            @Query("postid") int postid,
            @Query("total") int total,
            @Query("page") int page
    );

    @Headers({CACHE, AGENT})
    @GET(PostsSearch)
    Call<CallbackPostsSearch> PostsSearch(
            @Query("keyword") String keyword
    );

    @Headers({CACHE, AGENT})
    @GET(PostsSlider+AccessKeyString+AccessKeyValue)
    Call<CallbackPostsSlider> PostsSlider();

    @Headers({CACHE, AGENT})
    @GET(PostsByCategory+AccessKeyString+AccessKeyValue)
    Call<CallbackPostsByCategory> getPostsCategory(
            @Query("category") int categoryid
    );

    @Headers({CACHE, AGENT})
    @GET(CategoriesAll+AccessKeyString+AccessKeyValue)
    Call<CallbackCategory> getCategory();

    @Headers({CACHE, AGENT})
    @GET(ShowComment+AccessKeyString+AccessKeyValue)
    Call<CallbackShowComment> getShowComment(
            @Query("postid") int postid
    );

    @Headers({CACHE, AGENT})
    @GET(CountComment+AccessKeyString+AccessKeyValue)
    Call<CallbackCountComment> getCountComment(
            @Query("postid") long postid
    );

    @Headers({CACHE, AGENT})
    @GET(BackgroundDrawer+AccessKeyString+AccessKeyValue)
    Call<CallbackBackgroundDrawer> getImageDrawer(

    );

    @FormUrlEncoded
    @POST(Feedback)
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
