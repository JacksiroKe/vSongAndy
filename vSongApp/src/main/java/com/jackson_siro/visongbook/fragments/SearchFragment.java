package com.jackson_siro.visongbook.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackson_siro.visongbook.R;

import com.jackson_siro.visongbook.adapters.ListsSongsAdapter;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.PostModel;
import com.jackson_siro.visongbook.MyApplication;
import com.jackson_siro.visongbook.GgTutorial;
import com.jackson_siro.visongbook.ui.vSongBook;

import java.util.List;

public class SearchFragment extends Fragment {

    private View searchView;
    private ListsSongsAdapter listAdapter;
    private RecyclerView recyclerView;

    private static SQLiteHelper db = new SQLiteHelper(MyApplication.getAppContext());

    private String mLastQuery = "";
    private final String TAG = "BlankFragment";

    private boolean mIsDarkSearchTheme = false;

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private EditText mSearchView;
    private List<PostModel> resultList;
    private LinearLayout noResultView;
    private Button BtnLearnMore;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = searchView.findViewById(R.id.search_recycler_view);
        noResultView = searchView.findViewById(R.id.no_result_view);
        mSearchView = searchView.findViewById(R.id.input_search);
        BtnLearnMore = searchView.findViewById(R.id.btn_learn_more);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        BtnLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), GgTutorial.class));
            }
        });

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mSearchView.getText().length() > 2)
                {
                    try
                    {
                        try
                        {
                            if (resultList.size() > 1) resultList.clear();
                        }
                        catch (Exception ex) {}

                        recyclerView.setVisibility(View.VISIBLE);
                        noResultView.setVisibility(View.GONE);

                        resultList = db.searchForSongs(mSearchView.getText().toString().trim());
                        listAdapter = new ListsSongsAdapter(resultList, getContext());
                        recyclerView.setAdapter(listAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

                        listAdapter.setOnItemClickListener(new ListsSongsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, PostModel postModel) {
                                vSongBook.passingIntent(getActivity(), postModel.songid.toString(), "ViewSong");
                            }
                        });
                    }
                    catch (Exception ex) { }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView.setVisibility(View.GONE);
        noResultView.setVisibility(View.VISIBLE);
        return searchView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}