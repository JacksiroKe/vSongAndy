package com.jackson_siro.visongbook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import com.jackson_siro.visongbook.adapters.PostsSearchAdapter;
import com.jackson_siro.visongbook.models.Callback.CallbackPostsSearch;
import com.jackson_siro.visongbook.models.PostsSearch;
import com.jackson_siro.visongbook.retrofitconfig.API;
import com.jackson_siro.visongbook.retrofitconfig.CallJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.jackson_siro.visongbook.R;

public class SearchView extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private ImageButton bt_clear;
    private RecyclerView recyclerView;
    private View root_view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PostsSearchAdapter adapter;
    private Call<CallbackPostsSearch> callbackPostsSearchCall;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        toolbarSet();
        componentSet();
    }

    private void toolbarSet() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Search");
    }

    private void componentSet(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        root_view = findViewById(android.R.id.content);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new PostsSearchAdapter(this, new ArrayList<PostsSearch>());
        recyclerView.setAdapter(adapter);
        requestData("");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.resetListData();
                requestData("");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //showNoItem(true);
    }

    private void requestData(String keyword){
        API api = CallJson.callJson();
        callbackPostsSearchCall = api.PostsSearch(keyword);
        callbackPostsSearchCall.enqueue(new Callback<CallbackPostsSearch>() {
            @Override
            public void onResponse(Call<CallbackPostsSearch> call, Response<CallbackPostsSearch> response) {
                CallbackPostsSearch cl = response.body();
                if (cl != null);
                    adapter = new PostsSearchAdapter(SearchView.this, cl.data);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new PostsSearchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, PostsSearch postsSearch, int i) {
                            //PostSinglex.navigateParent(SearchView.this, postsSearch.postid, false);

                        }
                });
            }

            @Override
            public void onFailure(Call<CallbackPostsSearch> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.posts_search, menu);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.et_search).getActionView();
        searchView.setQueryHint("Search . . .");
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                requestData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (newText.isEmpty() && newText.toString() != null){
                    Toast.makeText(getApplicationContext(), getString(R.string.search_delete), Toast.LENGTH_LONG).show();
                    adapter.resetListData();
                }else{
                    requestData(newText);
                }
                return false;
            }

        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_variable = item.getItemId();
        if (item_variable == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
