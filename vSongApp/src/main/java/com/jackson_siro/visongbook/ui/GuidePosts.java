package com.jackson_siro.visongbook.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import com.jackson_siro.visongbook.shared.SharedStore;
import com.jackson_siro.visongbook.R;

public class GuidePosts extends AppCompatActivity{

    private SharedStore sharedStore;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;
    private TypedArray image;
    private ImageView[] dots_image;
    private String title_array_posts[], description_array_posts[];
    private Button next, skip;
    private ViewPager viewPager;
    private GuideAdapterSetting gdnset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_posts);
        storeToken();
        next = (Button) findViewById(R.id.btn_next);
        skip = (Button) findViewById(R.id.btn_skip);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        linearLayout = (LinearLayout) findViewById(R.id.layout_dots);
        title_array_posts = getResources().getStringArray(R.array.title_array);
        image = getResources().obtainTypedArray(R.array.image_array);
        description_array_posts = getResources().getStringArray(R.array.description_array);
        addDots(0);
        gdnset = new GuideAdapterSetting();
        viewPager.setAdapter(gdnset);
        viewPager.addOnPageChangeListener(viewPagerChange);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cur_dots = getGuideItem(+1);
                if (cur_dots < title_array_posts.length){
                    viewPager.setCurrentItem(cur_dots);
                }else{
                    finish();
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private int getGuideItem(int i){
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            if (position == title_array_posts.length - 1){
                next.setText(getString(R.string.finish_guide_posts));
                skip.setVisibility(View.GONE);
            }else{
                next.setText(getString(R.string.next_guide_posts));
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void storeToken() {
//        WHEN YOU WANT USE LOGIN AND STORE TOKEN TO DB, YOU CAN USE SHAREDPREFERENCES SUCH AS BELOW
//        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
//        final String tokens = preferences.getString(getString(R.string.FCM_TOKEN), "");
        FirebaseInstanceId.getInstance().getToken();
    }

    public void addDots(int dots){
        dots_image = new ImageView[title_array_posts.length];
        linearLayout.removeAllViews();
        for (int i = 0; i<title_array_posts.length; i++){
            dots_image[i] = new ImageView(this);
            int width_height = 20;
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            param.setMargins(13,13,13,13);
            dots_image[i].setLayoutParams(param);
            dots_image[i].setImageResource(R.drawable.dots_slider);
            linearLayout.addView(dots_image[i]);
        }
        if (dots_image.length > 0 ){
            dots_image[dots].setBackgroundResource(R.drawable.dots_slider);
        }
    }
    public class GuideAdapterSetting extends PagerAdapter{

        public GuideAdapterSetting() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.post_guide_posts, null);
            ((TextView) view.findViewById(R.id.title)).setText(title_array_posts[position]);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(image.getResourceId(position, -1));
            ((TextView) view.findViewById(R.id.description)).setText(description_array_posts[position]);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return title_array_posts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
