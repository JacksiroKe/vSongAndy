package com.jackson_siro.visongbook;

import java.text.SimpleDateFormat;

import com.jackson_siro.visongbook.ownbooks.*;
import com.jackson_siro.visongbook.songbooks.*;

import android.R.string;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CcSongBook extends ActionBarActivity implements TabListener {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sb_book);
        
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_is_paid", false))
        {            
        	HaveYouPaidMe();            
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

        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("js_vsb_rate_me", false))
        {            
            rateMePlease();            
        }
        
    }
    
    public void HaveYouPaidMe() {
    	SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        long used_time_l, rem_time_l, expires_l;
        int used_time_i, rem_time_i, expired_i;
        String expiry_txt = "from now";

        used_time_l = System.currentTimeMillis() - vSettings.getLong("js_vsb_first_data", 0);
        rem_time_l = vSettings.getLong("js_vsb_expire_data", 0) - System.currentTimeMillis();
        expires_l = System.currentTimeMillis() - vSettings.getLong("js_vsb_expire_data", 0);

        used_time_i = (int)(used_time_l / 1000);
        rem_time_i = (int)(rem_time_l / 1000);
        expired_i = (int)(expires_l / 1000);

        if (rem_time_i < 60) expiry_txt = rem_time_i + " seconds from Now";
        else if (rem_time_i < 3600) expiry_txt = rem_time_i / 60 + " minutes from now";
        else if (rem_time_i < 86400) expiry_txt = rem_time_i / 3600 + " hours from now";
        else if (rem_time_i < 440000) expiry_txt = rem_time_i / 86400 + " days from now";
        YouMustJustPay(expiry_txt);
    }

    public void YouMustJustPay(String rem_time) {
		AlertDialog.Builder builder = new AlertDialog.Builder(CcSongBook.this);
    	builder.setTitle("God bless you!");
        builder.setMessage("vSongBook is in its Evaluation (Trial) "
         + "Mode and will expire " + rem_time + ". Please click on the "
         + "Settings option and go to APPLICATION MODE to learn how to Upgrade to "
         + "Premium Mode! It will cost you Kshs. 250 to do that.");
        
        builder.setPositiveButton("Okay, Got It", new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//YouRatedMeYes();
				
			}
		});
        
        builder.show();
	
	  }
    public void rateMePlease() {
    	 SharedPreferences vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		

         long used_time_l, rate_time_l;
         int used_time_i, rate_time_i;

         used_time_l = System.currentTimeMillis() - vSettings.getLong("js_vsb_first_data", 0);
         rate_time_l = vSettings.getLong("js_vsb_rated_me_not", 0) - System.currentTimeMillis();
         
         used_time_i = (int)(used_time_l / 1000);
         rate_time_i = (int)(rate_time_l / 1000);
         if (used_time_i > 18000)
         {            
        	 AlertDialog.Builder builder = new AlertDialog.Builder(CcSongBook.this);
		    	builder.setTitle(R.string.just_a_min);
		        builder.setMessage(R.string.rate_me_please);
		        builder.setNegativeButton(R.string.rate_later, new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						YouRatedMeNot();	
					}
				});
		        
		        builder.setPositiveButton(R.string.rate_now, new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						YouRatedMeYes();
						Toast.makeText(getApplicationContext(), R.string.blessed, Toast.LENGTH_LONG).show();						
					}
				});
		        
		        builder.show();
         }
	  }
	
	public void YouRatedMeNot(){
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    localEditor.putBoolean("js_vsb_rate_me", false);
	    localEditor.putLong("js_vsb_rated_me_not", System.currentTimeMillis());
	    localEditor.commit();
     }
	
	public void YouRatedMeYes(){
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    localEditor.putBoolean("js_vsb_rate_me", true);
	    localEditor.putLong("js_vsb_rated_me_not", System.currentTimeMillis());
	    localEditor.commit();	    
	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.jackson_siro.visongbook")));
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
	
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: 
                onSearchRequested();
                return true;
            case R.id.favours:
                startActivity(new Intent(this, Favourites.class));
                return true;
            case R.id.notepad:
                startActivity(new Intent(this, OwnSongList.class));
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
    
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int position) {
			Bundle data = new Bundle();
			switch(position){
			
			case 0:
				SongBook1 songbook1 = new SongBook1();				
				data.putInt("current_page", position+1);
				songbook1.setArguments(data);
				return songbook1;
				
			case 1:
				SongBook2 songbook2 = new SongBook2();
				data.putInt("current_page", position+1);
				songbook2.setArguments(data);
				return songbook2;	

			case 2:
				SongBook3 songbook3 = new SongBook3();				
				data.putInt("current_page", position+1);
				songbook3.setArguments(data);
				return songbook3;
				
			case 3:
				SongBook4 songbook4 = new SongBook4();
				data.putInt("current_page", position+1);
				songbook4.setArguments(data);
				return songbook4;	

			case 4:
				SongBook5 songbook5 = new SongBook5();				
				data.putInt("current_page", position+1);
				songbook5.setArguments(data);
				return songbook5;
				
			case 5:
				SongBook6 songbook6 = new SongBook6();
				data.putInt("current_page", position+1);
				songbook6.setArguments(data);
				return songbook6;
				
			case 6:
				SongBook7 songbook7 = new SongBook7();
				data.putInt("current_page", position+1);
				songbook7.setArguments(data);
				return songbook7;	

			case 7:
				SongBook8 songbook8 = new SongBook8();
				data.putInt("current_page", position+1);
				songbook8.setArguments(data);
				return songbook8;	
			
			case 8:
				SongBook9 songbook9 = new SongBook9();
				data.putInt("current_page", position+1);
				songbook9.setArguments(data);
				return songbook9;	
			
			}
		
		return null;
		}

		
		@Override
		public int getCount() {
			return 9;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			case 2:
				return getString(R.string.title_section3);
			case 3:
				return getString(R.string.title_section4);
			case 4:
				return getString(R.string.title_section5);
			case 5:
				return getString(R.string.title_section6);
			case 6:
				return getString(R.string.title_section7);
			case 7:
				return getString(R.string.title_section8);
			case 8:
				return getString(R.string.title_section9);
			
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
			View rootView = inflater.inflate(R.layout.sb_fragment, container, false);
			return rootView;
			
		}
	}


}
