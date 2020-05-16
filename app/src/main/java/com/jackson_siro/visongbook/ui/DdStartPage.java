package com.jackson_siro.visongbook.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.adapters.*;
import com.jackson_siro.visongbook.data.SQLiteSearch;
import com.jackson_siro.visongbook.models.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class DdStartPage extends AppCompatActivity {

    GridView homeGrid;
    private String mLastQuery = "";
    private final String TAG = "SearchingSongs";
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private FloatingSearchView searchView;
    private boolean mIsDarkSearchTheme = false;
    Integer[] homeIcons = new Integer[] { R.drawable.ic_edit };
    String[] homeActions = new String[] { "1"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_start_page);
        homeGrid = findViewById(R.id.home_grid);
        searchView = findViewById(R.id.search_view);
        
        homeInit();
        setupFloatingSearch();
    }

    private void setupFloatingSearch() {
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.clearSuggestions();
                } else {
                    searchView.showProgress();
                    SQLiteSearch.findSuggestions(DdStartPage.this, newQuery, 5, FIND_SUGGESTION_SIMULATED_DELAY,
                            new SQLiteSearch.OnFindSuggestionsListener() {
                                @Override
                                public void onResults(List<SearchModel> results) {
                                    searchView.swapSuggestions(results);
                                    searchView.hideProgress();
                                }
                            });
                }
                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                SearchModel searchModel = (SearchModel) searchSuggestion;
                try {
                    searchView.clearSearchFocus();
                    //vSongBook.passingIntent(DdStartPage.this, searchModel.getID(), "DdPostView");
                }
                catch (Exception e){ }
                Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                searchView.clearSearchFocus();
                //searchView.clearSuggestions();

                /*SQLiteSearch.findColors(DdStartPage.this, query,
                new SQLiteSearch.OnFindSearchesListener() {

                    @Override
                    public void onResults(List<SearchWrapper> results) {
                        //mSearchResultsAdapter.swapData(results);
                    }

                });*/
                Log.d(TAG, "onSearchAction()");
            }
        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                //show suggestions when search bar gains focus (typically history suggestions)
                searchView.swapSuggestions(SQLiteSearch.getHistory(DdStartPage.this, 3));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                searchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //searchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });

        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.action_change_colors) {

                    //mIsDarkSearchTheme = true;

                    searchView.setBackgroundColor(Color.parseColor("#787878"));
                    searchView.setViewTextColor(Color.parseColor("#e9e9e9"));
                    searchView.setHintTextColor(Color.parseColor("#e9e9e9"));
                    searchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
                    searchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
                    searchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                    searchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
                    searchView.setDividerColor(Color.parseColor("#BEBEBE"));
                    searchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                } else {

                    //just print action
                    Toast.makeText(DdStartPage.this.getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        searchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                Log.d(TAG, "onHomeClicked()");
            }
        });

        searchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
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
                        searchView.getQuery(),
                        "<font color=\"" + textPrimary + "\">" + searchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        searchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                //mSearchResultsList.setTranslationY(newHeight);
            }
        });

        searchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                searchView.clearSuggestions();
                Log.d(TAG, "onClearSearchClicked()");
            }
        });
    }

    private void homeInit()
    {
        List<Integer> listIcons = new ArrayList<Integer>();
        List<String> listActions = new ArrayList<String>();

        listIcons.add(R.drawable.ic_edit);
        listIcons.add(R.drawable.ic_edit);
        listIcons.add(R.drawable.ic_books);
        listIcons.add(android.R.drawable.ic_dialog_info);
        listIcons.add(R.drawable.ic_books);
        listIcons.add(R.drawable.ic_books);
        listIcons.add(android.R.drawable.ic_menu_preferences);
        listIcons.add(R.drawable.ic_preferences);
        listIcons.add(R.drawable.ic_favorite_black);
        listIcons.add(android.R.drawable.ic_menu_call);
        listIcons.add(R.drawable.ic_money);
        listIcons.add(android.R.drawable.ic_dialog_info);

        listActions.add(getString(R.string.new_song));
        listActions.add(getString(R.string.new_sermon));
        listActions.add(getString(R.string.my_songbooks));
        listActions.add(getString(R.string.how_it_works));
        listActions.add(getString(R.string.songs_pad));
        listActions.add(getString(R.string.sermon_pad));
        listActions.add(getString(R.string.manage_songbooks));
        listActions.add(getString(R.string.app_settings));
        listActions.add(getString(R.string.favorites));
        listActions.add(getString(R.string.help_desk));
        listActions.add(getString(R.string.app_donate));
        listActions.add(getString(R.string.about_app));

        homeIcons = listIcons.toArray(new Integer[listIcons.size()]);
        homeActions = listActions.toArray(new String[listActions.size()]);

        homeGrid.setAdapter(new HomeGridAdapter(this, homeIcons, homeActions));

    }

    @Override
    public void onBackPressed() {
        if (searchView.setSearchFocused(true)) {
            searchView.clearSuggestions();
            searchView.clearQuery();
            Log.d(TAG, "onClearSearchClicked()");
        } else super.onBackPressed();
    }

}
