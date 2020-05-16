package com.jackson_siro.visongbook.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackson_siro.visongbook.R;

import com.jackson_siro.visongbook.adapters.ListsSongsAdapter;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.PostModel;
import com.jackson_siro.visongbook.MyApplication;
import com.jackson_siro.visongbook.ui.*;

import java.util.List;

public class DdHomeFrag1 extends Fragment {

    private View searchView;
    private ListsSongsAdapter listAdapter;
    private RecyclerView recyclerView;

    private List<PostModel> songlist;
    private static SQLiteHelper db = new SQLiteHelper(MyApplication.getAppContext());

    private SearchView mSearchView;
    private LinearLayout noContentView;
    private Button BtnLearnMore;

    public DdHomeFrag1() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = searchView.findViewById(R.id.search_recycler_view);
        noContentView = searchView.findViewById(R.id.no_content_view);
        mSearchView = searchView.findViewById(R.id.input_search);
        BtnLearnMore = searchView.findViewById(R.id.btn_learn_more);

        GetSongsList();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

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

                    new SearchList().execute(newText);
                }
                return false;
            }
        });

        return searchView;
    }

    private void GetSongsList()
    {
        songlist = db.GetSongsList(1);
        listAdapter = new ListsSongsAdapter(songlist, getContext());
        recyclerView.setAdapter(listAdapter);

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

    private class SearchList extends AsyncTask<String, String, List<PostModel>> {

        private List<PostModel> searchResult;

        @Override
        protected List<PostModel> doInBackground(String... params) {
            searchResult = db.searchForSongs(params[0]);
            return searchResult;
        }

        @Override
        protected void onPostExecute(List<PostModel> searchResult) {
            listAdapter = new ListsSongsAdapter(searchResult, getContext());
            recyclerView.setAdapter(listAdapter);
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