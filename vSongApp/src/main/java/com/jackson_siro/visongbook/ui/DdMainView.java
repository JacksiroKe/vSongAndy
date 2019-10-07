package com.jackson_siro.visongbook.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.adapters.HomeFragmentAdapter;
import com.squareup.picasso.Picasso;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DdMainView extends AppCompatActivity implements TabLayout.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private int tabUnselectedIcon[] = { R.drawable.ic_fab_search, R.drawable.ic_fab_online, R.drawable.ic_books, R.drawable.ic_notes };
    private int tabSelectedIcon[] = { R.drawable.ic_fab_search, R.drawable.ic_fab_online, R.drawable.ic_books, R.drawable.ic_notes };
    private int tabTitles[] = { R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4 };

    Intent intent;

    NavigationView navigationView;
    DrawerLayout drawer;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private DrawerLayout mDrawerLayout;
    private TextView mTabTitle;

    private String mGender = "";
    private String mFullname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_main_view);
        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();
        mGender = prefget.getString("user_sex", "1") == "1" ? "Bro. " : "Sis. ";
        mFullname = prefget.getString("user_firstname", "") + " " + prefget.getString("user_lastname", "");

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mTabTitle = findViewById(R.id.tab_title);

        //Setup viewpager and HomeFragmentAdapter
        ViewPager viewPager = findViewById(R.id.vp);
        HomeFragmentAdapter fragmentAdapter = new HomeFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);

        TabLayout tabLayout = findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        tabLayout.getTabAt(0).setIcon(tabSelectedIcon[0]);
        for (int i = 1; i < 4; i++) {
            tabLayout.getTabAt(i).setIcon(tabUnselectedIcon[i]);
        }

        tabLayout.addOnTabSelectedListener(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        ImageView app_image = findViewById(R.id.app_image);
        Picasso.get().load(R.drawable.appicon).transform(new CropCircleTransformation()).into(app_image);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);

        de.hdodenhof.circleimageview.CircleImageView profileImageView = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        Glide.with(this).load(R.drawable.usericon).into(profileImageView);

        navigationView.setNavigationItemSelectedListener(this);

        checkDatabase();
        checkDonation();
        displayProfile();
    }

    private void displayProfile() {
        View userControl = navigationView.getHeaderView(0);
        LinearLayout user_profile = userControl.findViewById(R.id.user_profile);
        TextView full_name = userControl.findViewById(R.id.full_name);
        TextView mobile_phone = userControl.findViewById(R.id.mobile_phone);
        TextView profile_text = userControl.findViewById(R.id.profile_text);

        full_name.setText(String.format(mGender + mFullname));
        mobile_phone.setText(prefget.getString("user_mobile", "-"));
        profile_text.setText(String.format(
                prefget.getString("user_church", "-") + " Church\n" +
                        prefget.getString("user_city", "-") + " " + prefget.getString("user_country_ccode", ""))
        );

        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(DdMainView.this, UserProfile.class));
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        tab.setIcon(tabSelectedIcon[position]);
        mTabTitle.setText(tabTitles[position]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        tab.setIcon(tabUnselectedIcon[position]);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void openDrawer(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
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
                        startActivity(new Intent(DdMainView.this, FfDonate.class));
                    }
                });
                builder.show();
            }
        }
    }

    public void checkDatabase() {
        if (!prefget.getBoolean("app_books_loaded", false))
            startActivity(new Intent(DdMainView.this, CcBooksLoad.class));
        if (prefget.getBoolean("app_books_reload", false))
            startActivity(new Intent(DdMainView.this, CcBooksLoad.class));
        else if (prefget.getBoolean("app_books_loaded", false) && !prefget.getBoolean("app_songs_loaded", false))
            startActivity(new Intent(DdMainView.this, CcSongsLoad.class));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem){
            case R.id.nav_updates:

                break;

            case R.id.nav_signout:
                signoutUser();
                break;

            case R.id.nav_songbooks:
                prefedit.putBoolean("app_books_reload", true);
                prefedit.putBoolean("app_songs_reload", false);
                startActivity(new Intent(DdMainView.this, CcSongsLoad.class));
                break;

            case R.id.nav_donate:
                startActivity(new Intent(DdMainView.this, FfDonate.class));
                break;

            case R.id.app_settings:
                startActivity(new Intent(DdMainView.this, FfSettings.class));
                break;

            case R.id.app_updates:
                startActivity(new Intent(DdMainView.this, FfUpdates.class));
                break;

            case R.id.app_feedback:
                startActivity(new Intent(DdMainView.this, GgHelpdesk.class));
                break;

            case R.id.app_about:
                startActivity(new Intent(DdMainView.this, GgAboutapp.class));
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
        startActivity(new Intent(DdMainView.this, AppStart.class));
        finish();
    }

}
