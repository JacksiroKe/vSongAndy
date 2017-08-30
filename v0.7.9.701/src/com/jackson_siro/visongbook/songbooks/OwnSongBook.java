package com.jackson_siro.visongbook.songbooks;

import java.util.Locale;

import com.jackson_siro.visongbook.*;
import com.jackson_siro.visongbook.tools.*;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("deprecation")
public class OwnSongBook extends AppFunctions implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.own_songbook);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.own_song_book, menu);
		return true;
	}

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newsong:
                startActivity(new Intent(this, OwnSongNew.class));
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

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int position) {
			Bundle data = new Bundle();
			switch(position){
			
			case 0:
				OwnFavList fragment1 = new OwnFavList();				
				data.putInt("current_page", position+1);
				fragment1.setArguments(data);
				return fragment1;
				
			case 1:
				OwnSongList fragment2 = new OwnSongList();				
				data.putInt("current_page", position+1);
				fragment2.setArguments(data);
				return fragment2;
		
			case 2:
				OwnUpdateList fragment3 = new OwnUpdateList();				
				data.putInt("current_page", position+1);
				fragment3.setArguments(data);
				return fragment3;
			}
			return null;
		}
		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.fav_song_book).toUpperCase(l);
			case 1:
				return getString(R.string.own_song_book).toUpperCase(l);
			case 2:
				return getString(R.string.editted_songs).toUpperCase(l);
			}
			return null;
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
			View rootView = inflater.inflate(R.layout.own_songbook_frag, container, false);
			return rootView;
		}
	}

}
