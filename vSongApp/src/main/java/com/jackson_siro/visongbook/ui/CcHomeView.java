package com.jackson_siro.visongbook.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.jackson_siro.visongbook.data.*;
import com.jackson_siro.visongbook.fragments.*;

import com.bumptech.glide.Glide;
import com.jackson_siro.visongbook.adapters.*;
import com.jackson_siro.visongbook.components.*;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.SearchModel;

import java.util.List;

public class CcHomeView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Intent intent;

    NavigationView navigationView;
    DrawerLayout drawer;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private final String TAG = "BlankFragment";

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private FloatingSearchView mSearchView;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;

    private boolean mIsDarkSearchTheme = false;

    private String mLastQuery = "";
    private String mGender = "";
    private String mFullname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cc_home_view);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();
        mGender = prefget.getInt("user_gender", 1) == 1 ? "Bro. " : "Sis. ";
        mFullname = prefget.getString("user_firstname", "") + " " + prefget.getString("user_lastname", "");

        mSearchView = findViewById(R.id.search_view);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        viewPager = findViewById(R.id.mainViewPager);
        setupViewPager();

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);

        de.hdodenhof.circleimageview.CircleImageView profileImageView = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        Glide.with(this).load(R.drawable.usericon).into(profileImageView);

        navigationView.setNavigationItemSelectedListener(this);

        checkUpdates();
        checkDonation();
        checkDatabase();
        displayProfile();
        setupFloatingSearch();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OfflineFragment(), "My Collection");
        adapter.addFragment(new OnlineFragment(), "Online");
        viewPager.setAdapter(adapter);
    }

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();
                    SQLiteSearch.findSuggestions(CcHomeView.this, newQuery, 5, FIND_SUGGESTION_SIMULATED_DELAY,
                            new SQLiteSearch.OnFindSuggestionsListener() {
                                    @Override
                                    public void onResults(List<SearchModel> results) {
                            mSearchView.swapSuggestions(results);
                            mSearchView.hideProgress();
                        }
                    });
                }
                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                SearchModel searchModel = (SearchModel) searchSuggestion;
                try {
                    mSearchView.clearSearchFocus();
                    vSongBook.passingIntent(CcHomeView.this, searchModel.getID(), "DdPostView");
                }
                catch (Exception e){ }
                Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                mSearchView.clearSearchFocus();
                //mSearchView.clearSuggestions();

                /*SQLiteSearch.findColors(CcHomeView.this, query,
                new SQLiteSearch.OnFindSearchesListener() {

                    @Override
                    public void onResults(List<SearchWrapper> results) {
                        //mSearchResultsAdapter.swapData(results);
                    }

                });*/
                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(SQLiteSearch.getHistory(CcHomeView.this, 3));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
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

                    //just print action
                    Toast.makeText(CcHomeView.this.getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                Log.d(TAG, "onHomeClicked()");
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
                Log.d(TAG, "onClearSearchClicked()");
            }
        });
    }

    public void checkUpdates() {
        String version = "";
        int versionCode = 0;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            versionCode = pInfo.versionCode;
            Log.d("MyApp", "Version Name : "+version + "\n Version Code : "+versionCode);
        }catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("MyApp", "PackageManager Catch : "+e.toString());
        }

        if (prefget.getString("app_version_current", "") != version) {
            Long reminder_time = System.currentTimeMillis() - prefget.getLong("app_update_reminder", 0);
            if (reminder_time > 18000) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Update your vSongBook!");
                builder.setMessage("Hello " + mGender + mFullname + "! Enjoy improved functionality and user inteface with the new " +
                        "vSongBook update (" + prefget.getString("app_version_size", "") + "B) released on " +
                        prefget.getString("app_version_updated", "") + ". Please go ahead and update!"
                );
                builder.setNegativeButton("Remind me Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        prefedit.putLong("app_update_reminder", System.currentTimeMillis()).apply();
                    }
                });
                builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.jackson_siro.visongbook")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.jackson_siro.visongbook")));
                        }
                    }
                });
                builder.show();
            }
        }
    }

    public void checkDonation(){
        if (!prefget.getBoolean("app_user_donated", false)){
            Long reminder_time = System.currentTimeMillis() - prefget.getLong("app_donation_reminder", 0);
            if (reminder_time > 18000) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("vSongBook Needs Your Support");
                builder.setMessage("Hello " + mGender + mFullname + "! vSongBook is proudly non-profit, non-corporate and non-compromised. " +
                        "A lot of users like you help us stand up for a free vSongBook for all. We now rely on donations to carry out our " +
                        "mission to give everyone able to use our app the freedom to sing anywhere anytime. Any amount of money will be " +
                        "highly appreciated by our team of developers. Will you give today?"
                );
                builder.setNegativeButton("Remind me Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        prefedit.putLong("app_donation_reminder", System.currentTimeMillis()).apply();
                    }
                });
                builder.setNeutralButton("Leave me out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        prefedit.putBoolean("app_user_donated", true).apply();
                    }
                });
                builder.setPositiveButton("Yes am in", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(CcHomeView.this, FfDonate.class));
                    }
                });
                builder.show();
            }
        }
    }
    public void checkDatabase() {
        if (!prefget.getBoolean("app_books_loaded", false))
            startActivity(new Intent(CcHomeView.this, BbBooksLoad.class));
        if (prefget.getBoolean("app_books_reload", false))
            startActivity(new Intent(CcHomeView.this, BbBooksLoad.class));
        else if (prefget.getBoolean("app_books_loaded", false) && !prefget.getBoolean("app_songs_loaded", false))
            startActivity(new Intent(CcHomeView.this, BbSongsLoad.class));
    }

    private void displayProfile() {
        View userControl = navigationView.getHeaderView(0);
        TextView profile_text1 = userControl.findViewById(R.id.profile_text1);
        TextView profile_text2 = userControl.findViewById(R.id.profile_text2);
        LinearLayout user_view = userControl.findViewById(R.id.user_view);

        profile_text1.setText(String.format(mGender + mFullname));
        profile_text2.setText(String.format(
                prefget.getString("user_church", "-") + " Church\n" +
                prefget.getString("user_city", "-") + " " + prefget.getString("user_country_ccode", ""))
        );

        user_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(CcHomeView.this, UserProfile.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem){
            /*case R.id.sliding_list_example:
                showFragment(new SearchFragment());
                return true;
            case R.id.sliding_search_bar_example:
                showFragment(new SearchViewFragment());
                return true;
            case R.id.scrolling_search_bar_example:
                showFragment(new ScrollingSearchFragment());
                return true;*/

            case R.id.nav_updates:

                break;

            case R.id.nav_signout:
                signoutUser();
                break;

            case R.id.nav_songbooks:
                prefedit.putBoolean("app_books_reload", true);
                prefedit.putBoolean("app_songs_reload", false);
                startActivity(new Intent(CcHomeView.this, BbSongsLoad.class));
                break;

            case R.id.nav_donate:
                startActivity(new Intent(CcHomeView.this, FfDonate.class));
                break;

            case R.id.app_settings:
                startActivity(new Intent(CcHomeView.this, FfSettings.class));
                break;

            case R.id.app_updates:
                startActivity(new Intent(CcHomeView.this, FfUpdates.class));
                break;

            case R.id.app_feedback:
                startActivity(new Intent(CcHomeView.this, GgHelpdesk.class));
                break;

            case R.id.app_about:
                startActivity(new Intent(CcHomeView.this, GgAboutapp.class));
                break;

            default:

                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signoutUser(){
        //prefedit.putBoolean("app_books_loaded", false).apply();
        //prefedit.putBoolean("app_songs_loaded", false).apply();
        prefedit.putBoolean("app_user_signedin", false).apply();

        prefget.edit().remove("user_userid").apply();
        prefget.edit().remove("user_handle").apply();
        prefget.edit().remove("user_uniqueid").apply();
        prefget.edit().remove("user_email").apply();
        prefget.edit().remove("user_level").apply();
        prefget.edit().remove("user_created").apply();
        prefget.edit().remove("user_signedin").apply();
        prefget.edit().remove("user_avatarblobid").apply();
        prefget.edit().remove("user_points").apply();
        prefget.edit().remove("user_wallposts").apply();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        startActivity(new Intent(CcHomeView.this, AppStart.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.setSearchFocused(true)) {
            mSearchView.clearSuggestions();
            mSearchView.clearQuery();
            Log.d(TAG, "onClearSearchClicked()");
        } else if (mSearchView.setSearchFocused(false)) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else super.onBackPressed();
        }
    }

}
