package com.jackson_siro.visongbook;
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
 
public class BbDemo extends AppCompatActivity {
 
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    //private PrefManager prefManager;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
 
        setContentView(R.layout.bb_demo);
 
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        layouts = new int[]{
                R.layout.bb_demo_slide1,
                R.layout.bb_demo_slide2,
                R.layout.bb_demo_slide3,
                R.layout.bb_demo_slide4};

        addBottomDots(0);

        changeStatusBarColor();
 
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
 
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingNow();
            }
        });
 
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                	SingNow();
                }
            }
        });
    }
 
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
 
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
 
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
 
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }
 
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
    
    public void SingNow()   {
    	SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    	localEditor.putBoolean("as_vsb_show_tutorial", true);
		localEditor.commit();
			
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_logged_in_user", false)) {
    		localEditor.putBoolean("as_vsb_logged_in_user", false);
		    localEditor.commit();		    
		    startActivity(new Intent(BbDemo.this, BbLogin.class));
  		} else {		
  			if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_finished_loading", false)) {
  				startActivity(new Intent(this, CcFirstLoad.class));
  			} else {
  		    	startActivity(new Intent(this, CcSongBook.class));
  			}
  		}
	    finish();
    }
    
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
 
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
 
            // changing the next button text 'NEXT' / 'GOT IT'
            //tutorial_one_title
            switch(position){
			
				case 0:
					setTitle(R.string.tutorial_activity_title);
					btnNext.setText(getString(R.string.button_next));
	                btnSkip.setVisibility(View.VISIBLE);
					return;
					
				case 1:
					setTitle(R.string.tutorial_two_title);
					btnNext.setText(getString(R.string.button_next));
	                btnSkip.setVisibility(View.VISIBLE);
					return;
					
				case 2:
					setTitle(R.string.tutorial_three_title);
					btnNext.setText(getString(R.string.button_next));
	                btnSkip.setVisibility(View.VISIBLE);
					return;
					
				case 3:
					setTitle(R.string.tutorial_four_title);
					btnNext.setText(getString(R.string.button_finish));
	                btnSkip.setVisibility(View.GONE);
					return;
			
			}
			return;
        }
 
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
 
        }
 
        @Override
        public void onPageScrollStateChanged(int arg0) {
 
        }
    };
 
    @SuppressLint("NewApi")
	private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
 
        public MyViewPagerAdapter() {
        }
 
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
 
            return view;
        }
 
        @Override
        public int getCount() {
            return layouts.length;
        }
 
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
 
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}