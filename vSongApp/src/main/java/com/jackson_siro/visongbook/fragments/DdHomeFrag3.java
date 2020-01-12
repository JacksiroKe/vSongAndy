package com.jackson_siro.visongbook.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jackson_siro.visongbook.R;

import com.jackson_siro.visongbook.adapters.ListsSongsAdapter;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.PostModel;
import com.jackson_siro.visongbook.MyApplication;
import com.jackson_siro.visongbook.ui.*;

import java.util.List;

public class DdHomeFrag3 extends Fragment {

    private View searchView;
    private ListsSongsAdapter listAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static SQLiteHelper db = new SQLiteHelper(MyApplication.getAppContext());

    private List<PostModel> favourites;
    private SearchView mSearchView;
    private LinearLayout mContentView, noContentView;
    private Button BtnLearnMore;

    public DdHomeFrag3() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchView = inflater.inflate(R.layout.fragment_general, container, false);
        recyclerView = searchView.findViewById(R.id.search_recycler_view);
        mContentView = searchView.findViewById(R.id.content_view);
        noContentView = searchView.findViewById(R.id.no_content_view);
        mSearchView = searchView.findViewById(R.id.input_search);
        BtnLearnMore = searchView.findViewById(R.id.btn_learn_more);

        mSearchView.setQueryHint("Search Favourites");
        swipeRefreshLayout = searchView.findViewById(R.id.swipe_refresh_layout);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mContentView.setVisibility(View.VISIBLE);
        noContentView.setVisibility(View.GONE);

        getSongList();
        BtnLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), GgTutorial.class));
            }
        });

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.onActionViewExpanded();
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1)
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    noContentView.setVisibility(View.GONE);

                    new LoadList().execute(newText);
                }
                return false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                waitLoadingList();
            }
        });

        return searchView;
    }

    private void waitLoadingList(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new LoadList().execute("");
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
    private void getSongList()
    {
        favourites = db.GetFavourites("");
        listAdapter = new ListsSongsAdapter(favourites, getContext());
        recyclerView.setAdapter(listAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        listAdapter.setOnItemClickListener(new ListsSongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostModel postModel) {
                vSongBook.passingIntent(getActivity(), postModel.songid.toString(), "ViewSong");
            }
        });

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class LoadList extends AsyncTask<String, String, List<PostModel>> {

        private List<PostModel> searchResult;

        @Override
        protected List<PostModel> doInBackground(String... params) {
            searchResult = db.GetFavourites(params[0]);
            return searchResult;
        }

        @Override
        protected void onPostExecute(List<PostModel> searchResult) {
            listAdapter = new ListsSongsAdapter(searchResult, getContext());
            recyclerView.setAdapter(listAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

            listAdapter.setOnItemClickListener(new ListsSongsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, PostModel postModel) {
                    vSongBook.passingIntent(getActivity(), postModel.songid.toString(), "ViewSong");
                }
            });
        }

        @Override
        protected void onPreExecute() { }

        @Override
        protected void onProgressUpdate(String... text) { }
    }
}