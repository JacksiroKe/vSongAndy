package com.jackson_siro.visongbook.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.adapters.*;
import com.jackson_siro.visongbook.models.*;
import com.jackson_siro.visongbook.models.Callback.*;
import com.jackson_siro.visongbook.retrofitconfig.*;
import com.jackson_siro.visongbook.ui.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineFragmentTabs extends Fragment {
    private String request;
    private View view;
    private ListsQuestionsAdapter recentAdapter;
    private Call<CallbackPostsLists> callbackPostsCall;
    private RecyclerView itemsRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        request = getArguments().getString("api_request");
        view = inflater.inflate(R.layout.posts_fragment_tabs, container, false);
        itemsRecyclerView = view.findViewById(R.id.posts_recycler_view);

        switch (request) {
            case "hot":
                loadItemsData("hot");
                break;

            case "thumbs":
                loadItemsData("thumbs");
                break;

            case "answers":
                loadItemsData("answers");
                break;

            case "views":
                loadItemsData("views");
                break;

            default:
                loadItemsData("created");
                break;
        }

        return view;
    }

    private void loadItemsData(final String sort) {
        itemsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        itemsRecyclerView.setLayoutManager(layoutManager);

        recentAdapter = new ListsQuestionsAdapter(new ArrayList<PostModel>(), getContext());

        recentAdapter.setOnItemClickListener(new ListsQuestionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostModel postModel) {
                vSongBook.passingIntent(getActivity(), postModel.songid, "DdPostView");
            }
        });

        itemsRecyclerView.setAdapter(recentAdapter);
        //if (CheckNetwork.isConnectCheck(getContext())) requestAction(0, sort);
        requestAction(0, sort);
    }

    private void displayApiResult(final List<PostModel> posts) {
        recentAdapter.insertData(posts);
        if (posts.size() == 0);
    }

    private void requestAction(final int start, final String sort){
        recentAdapter.setLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData(start, sort);
            }
        }, 1000);
    }

    private void requestData(final int start, final String sort) {
        API api = CallJson.callJson();
        //callbackPostsCall = api.PostListing(BaseUrlConfig.RequestLoadMore, start, sort);
        callbackPostsCall = api.PostsLists(0, start, sort);
        callbackPostsCall.enqueue(new Callback<CallbackPostsLists>() {
            @Override
            public void onResponse(Call<CallbackPostsLists> call, Response<CallbackPostsLists> response) {
                CallbackPostsLists callbackPostsLists = response.body();
                if (callbackPostsLists != null){
                    displayApiResult(callbackPostsLists.data);
                }
            }

            @Override
            public void onFailure(Call<CallbackPostsLists> call, Throwable t) {
                if (!call.isCanceled());
            }
        });
    }

}
