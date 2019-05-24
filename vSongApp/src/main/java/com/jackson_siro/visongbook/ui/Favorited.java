package com.jackson_siro.visongbook.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.retrofitconfig.BaseUrlConfig;
import com.jackson_siro.visongbook.R;

public class Favorited extends AppCompatActivity {

    private TextView titleDetailsPosts;
    private WebView webView;
    private ImageView imgDetailNew;
    private Context context;
    private SQLiteHelper SQLiteHelper;
    private String storeTitle, storeCode, storeCategory, storeImage, storeContent, created;
    int storeId;
    private MenuItem menuItem;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Fragment fragment;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_favorite_view);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        titleDetailsPosts = (TextView) findViewById(R.id.titleDetailsPosts);
        webView = (WebView) findViewById(R.id.descriptionFavorites);
        imgDetailNew = (ImageView) findViewById(R.id.imageFavorites);
        SQLiteHelper = new SQLiteHelper(this);
        toolbarSet();
        Intent getValue = getIntent();

        storeId = getValue.getIntExtra("postid", 0);
        storeTitle = getValue.getStringExtra("title");
        storeCode = getValue.getStringExtra("code");
        storeCategory = getValue.getStringExtra("category");
        storeImage = getValue.getStringExtra("image");
        storeContent = getValue.getStringExtra("content");
        String html = "<style>img{display: inline; height: auto; max-width: 100%;}</style>";

        titleDetailsPosts.setText(storeTitle);
        Picasso.with(context).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+storeImage).into(imgDetailNew);
        webView.loadDataWithBaseURL(null, html+storeContent, "text/html", "UTF-8", null);

    }

    private void toolbarSet() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
    }

    public void shareItem(String url, final String text) {
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_TEXT, text);
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
                overridePendingTransition(R.anim.animation_blank, R.anim.animation_left_right);
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }

    private Uri getLocalBitmapUri(Bitmap bitmap) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.posts_favorite_view, menu);
        menuItem = menu.findItem(R.id.deleteOne);
        if (SQLiteHelper.isDataExist(storeId)){
            menuItem.setIcon(R.drawable.ic_favorite_black_48dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_variable = item.getItemId();
        if (item_variable == android.R.id.home){
            onBackPressed();
        }else if (item_variable == R.id.deleteOne){
            if (SQLiteHelper.isDataExist(storeId)){
                SQLiteHelper.deleteData(storeId);
                menuItem.setIcon(R.drawable.ic_favorite_border_black_24dp);
                onBackPressed();
                finish();
            }else{
                //databaseHelpers.addData(storeId, storeTitle, storeCode, storeCategory, storeImage, storeContent, "");
                menuItem.setIcon(R.drawable.ic_favorite_black_48dp);
            }
        }else if (item_variable == R.id.share){
            String url = prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+storeImage;
            shareItem(url, "Hi friend, I love this posts, if you interest with this posts you can get more posts with download our app on play store "
                    +getString(R.string.google_play_store)+getPackageName());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_blank, R.anim.animation_left_right);
    }
}
