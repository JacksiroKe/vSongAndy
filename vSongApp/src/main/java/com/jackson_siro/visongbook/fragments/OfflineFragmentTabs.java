package com.jackson_siro.visongbook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackson_siro.visongbook.MyApplication;
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
        itemsRecyclerView.addItemDecoration(new DividerItemDecoration(itemsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        listAdapter.setOnItemClickListener(new ListsSongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostModel postModel) {
                vSongBook.passingIntent(getActivity(), postModel.songid, "EePostView");
            }
        });
    }

}
