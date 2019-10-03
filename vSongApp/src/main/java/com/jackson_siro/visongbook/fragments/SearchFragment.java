package com.jackson_siro.visongbook.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.jackson_siro.visongbook.R;

import com.jackson_siro.visongbook.adapters.ListsSongsAdapter;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.data.SQLiteSearch;
import com.jackson_siro.visongbook.models.PostModel;
import com.jackson_siro.visongbook.ui.MyApplication;
import com.jackson_siro.visongbook.ui.vSongBook;
import com.jackson_siro.visongbook.models.SearchModel;

import java.util.List;

public class SearchFragment extends Fragment {

    private View searchView;
    private ListsSongsAdapter listAdapter;
    private RecyclerView recyclerView;

    private SQLiteHelper db = new SQLiteHelper(MyApplication.getAppContext());

    private String mLastQuery = "";
    private final String TAG = "BlankFragment";

    private boolean mIsDarkSearchTheme = false;

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private FloatingSearchView mSearchView;

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
        mSearchView = searchView.findViewById(R.id.search_view);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<PostModel> songslist = db.getSongList(1);
        listAdapter = new ListsSongsAdapter(songslist, getContext());
        recyclerView.setAdapter(listAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        listAdapter.setOnItemClickListener(new ListsSongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostModel postModel) {
                vSongBook.passingIntent(getActivity(), postModel.songid, "EePostView");
            }
        });
        setupFloatingSearch();
        return searchView;
    }

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();
                    SQLiteSearch.findSuggestions(getContext(), newQuery, 5, FIND_SUGGESTION_SIMULATED_DELAY,
                            new SQLiteSearch.OnFindSuggestionsListener() {
                                @Override
                                public void onResults(List<SearchModel> results) {
                                    mSearchView.swapSuggestions(results);
                                    mSearchView.hideProgress();
                                }
                            });
                }
                //Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                SearchModel searchModel = (SearchModel) searchSuggestion;
                try {
                    mSearchView.clearSearchFocus();
                    vSongBook.passingIntent(getActivity(), searchModel.getID(), "DdPostView");
                }
                catch (Exception e){ }
                //Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                mSearchView.clearSearchFocus();
                //mSearchView.clearSuggestions();

                /*SQLiteSearch.findColors(getContext(), query,
                new SQLiteSearch.OnFindSearchesListener() {

                    @Override
                    public void onResults(List<SearchWrapper> results) {
                        //mSearchResultsAdapter.swapData(results);
                    }

                });*/
                //Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(SQLiteSearch.getHistory(getContext(), 3));

                //Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.action_change_colors) {

                    mIsDarkSearchTheme = true;

                    mSearchView.setBackgroundColor(Color.parseColor("#787878"));
                    mSearchView.setViewTextColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setHintTextColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setDividerColor(Color.parseColor("#BEBEBE"));
                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                } else {
                    Toast.makeText(getContext().getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                //Log.d(TAG, "onHomeClicked()");
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                SearchModel SearchModel = (SearchModel) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";
                String textPrimary = "#FF2500";

                //int textPrimary = ResourcesCompat.getColor(getResources().getColor(R.color.colorPrimary));

                if (SearchModel.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_history_black_24dp, null));
                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_search_black_24dp, null));
                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = SearchModel.getBody().replaceFirst(
                        mSearchView.getQuery(),
                        "<font color=\"" + textPrimary + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                //mSearchResultsList.setTranslationY(newHeight);
            }
        });

        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                mSearchView.clearSuggestions();
                //Log.d(TAG, "onClearSearchClicked()");
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}