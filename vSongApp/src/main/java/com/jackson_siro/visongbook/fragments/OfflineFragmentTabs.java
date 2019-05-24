package com.jackson_siro.visongbook.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.adapters.*;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.*;
import com.jackson_siro.visongbook.ui.*;

import java.util.List;

public class OfflineFragmentTabs extends Fragment {
    private View view;
    private ListsSongsAdapter listAdapter;
    private RecyclerView itemsRecyclerView;

    private SQLiteHelper db = new SQLiteHelper(MyApplication.getAppContext());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String request = getArguments().getString("songbookid");
        view = inflater.inflate(R.layout.posts_fragment_tabs, container, false);
        itemsRecyclerView = view.findViewById(R.id.posts_recycler_view);

        loadItemsData(Integer.parseInt(request));
        return view;
    }

    private void loadItemsData(final int songbookid) {
        itemsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        itemsRecyclerView.setLayoutManager(layoutManager);

        List<PostModel> songslist = db.getSongList(songbookid);
        listAdapter = new ListsSongsAdapter(songslist, getContext());
        itemsRecyclerView.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new ListsSongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostModel postModel) {
                vSongBook.passingIntent(getActivity(), postModel.songid, "DdPostView");
            }
        });
    }

}
