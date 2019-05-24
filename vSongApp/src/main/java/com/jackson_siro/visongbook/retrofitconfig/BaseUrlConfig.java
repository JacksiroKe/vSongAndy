package com.jackson_siro.visongbook.retrofitconfig;

import java.io.IOException;

public class BaseUrlConfig {
    public static final String BaseJsonOffline = "https://192.168.43.233/appsmata/appsmata/"; // change this url with your base file
    public static final String BaseJsonOnline = "https://raw.githubusercontent.com/AppSmata/AppSmata/master/"; // change this url with your base file
    public static final String BaseUrl = "https://appsmata.kenyanexamsrevisions.co.ke/vsongbook/"; // change this url with your base url
    //public static final String BaseUrl = "https://192.168.43.233/vsongbook/"; // change this url with your base url

    public static final String AppToken = "as-api/insert_token_from_app.php"; // change this url with your base url http://192.168.0.101/com.jackson_siro.vsongbook-posts
    public static final int RequestLoadMore = 10;
    public static final String SqlDbPath = "data/data/com.jackson_siro.vsongbook.android/posts/databases"; //don't change this
    public static final String GooglePlayStoreUrl = "https://play.google.com/store/apps/details?id=";   //don't change this

    //signup,Signin and comment user
    public static final String BaseUrl_IMAGE = "as-api/images_users/";  //don't change this
    public static final String UserSignup = "as-api/user-signup.php";   //don't change this
    public static final String UserSignin = "as-api/user-signin.php";   //don't change this
    public static final String UPDATE = "as-api/users/update.php"; //don't change this
    public static final String UPDATE_IMAGE_USERS = "as-api/users/images_users/images.php";    //don't change this
    public static final String USER_COMMENT = "as-api/users/comment.php"; //don't change this

    public BaseUrlConfig() throws IOException {
    }
}
