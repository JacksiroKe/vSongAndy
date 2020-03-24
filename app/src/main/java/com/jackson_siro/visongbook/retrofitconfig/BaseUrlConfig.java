package com.jackson_siro.visongbook.retrofitconfig;

import java.io.IOException;

public class BaseUrlConfig {
    public static final String BaseJsonOffline = "https://192.168.43.233/appsmata/appsmata/"; // change this url with your base file
    public static final String BaseJsonOnline = "https://raw.githubusercontent.com/AppSmata/AppSmata/master/"; // change this url with your base file
    //public static final String BaseUrl = "https://sing.appsmata.com/"; // change this url with your base url
    public static final String BaseUrl = "https://192.168.43.16/vsongweb/"; // change this url with your base url

    public static final String AppToken = "as-api/insert_token_from_app.php"; // change this url with your base url http://192.168.0.101/com.jackson_siro.vsongbook-posts
    public static final int RequestLoadMore = 10;
    public static final String SqlDbPath = "data/data/com.jackson_siro.vsongbook.android/posts/databases"; //don't change this
    public static final String GooglePlayStoreUrl = "https://play.google.com/store/apps/details?id=";   //don't change this

    public static final String AppChecker = "vsongbook.json";
    public static final String PostsLists = "as-api/posts-lists.php";
    public static final String PostsSelect = "as-api/posts-select.php";
    public static final String PostsSingle = "as-api/posts-single.php";
    public static final String PostsAnswers = "as-api/posts-answers.php";
    public static final String PostsSearch = "as-api/posts-search.php";
    public static final String PostsSlider = "as-api/posts-slider.php";
    public static final String PostsByCategory = "as-api/posts-by-category.php";
    public static final String CategoriesAll = "as-api/categories.php";
    public static final String BooksSelect = "as-api/book-select.php";
    public static final String Feedback = "as-api/feedback.php";
    public static final String ShowComment = "as-api/comment-by-id.php";
    public static final String CountComment = "as-api/comment-submit.php";
    public static final String BackgroundDrawer = "as-api/bg-drawer.php";
    public static final String UserSignin = "as-api/user-signin.php";
    public static final String UserSignup = "as-api/user-signup.php";
    public static final String UserLastseen = "as-api/user-lastseen.php";


    public BaseUrlConfig() throws IOException {
    }
}
