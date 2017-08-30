package com.jackson_siro.visongbook;

import com.jackson_siro.visongbook.songbooks.*;
import com.jackson_siro.visongbook.tools.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.TabListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("deprecation")
public class CcSongBook extends AppFunctions implements TabListener {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    SharedPreferences vSettings;
    SharedPreferences.Editor localEditor;
    
    public SongBookSQLite db = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_book);
        vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
    	localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    	
    	if (isInternetOn()) {
	        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_update_online", true)) {
	        	new UserProfileUpload().execute();
	  		} else new UserProfileDownload().execute();
	    }
    	
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(2);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(this.mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
        
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

        changeStatusBarColor();        
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_is_paid", false))
	        HaveYouPaidMe();
        
	}

    @Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}
	
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    	String[] songbooks = TextUtils.split(vSettings.getString("as_vsb_sbcodes", "NA"), ",");
    	String[] songbookno = TextUtils.split(vSettings.getString("as_vsb_sbnos", "NA"), ",");
    	
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public int getCount() {
			return songbooks.length-1;
		}

		@Override
		public CharSequence getPageTitle(int position) {			
			return songbooks[position];
		}

		public Fragment getItem(int position) {
			Bundle data = new Bundle();
			data.putInt("songbook", Integer.parseInt(songbookno[position]));
			SongList sblist = new SongList();				
			sblist.setArguments(data);
			return sblist;
		}
		
	}
    
    public static class PlaceholderFragment extends Fragment {
		
		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.song_book_frag, container, false);
			return rootView;
			
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: 
                onSearchRequested();
                return true;
            case R.id.goonline:
                startActivity(new Intent(this, HhOnline.class));
                return true;
            case R.id.moremenu:
                startActivity(new Intent(this, OwnSongBook.class));
                return true;
            case R.id.about:
                startActivity(new Intent(this, EeAbout.class));
                return true;
            case R.id.tutorial:
            	startActivity(new Intent(this, BbDemo.class));
                return true;
            case R.id.helpdesk:
                startActivity(new Intent(this, EeHelpDesk.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            default:
                return false;
        }
    }

}
