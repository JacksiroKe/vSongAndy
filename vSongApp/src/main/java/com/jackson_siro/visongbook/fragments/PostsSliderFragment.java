package com.jackson_siro.visongbook.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.jackson_siro.visongbook.adapters.PostsSliderAdapter;
import com.jackson_siro.visongbook.models.Callback.CallbackPostsLists;
import com.jackson_siro.visongbook.models.Callback.CallbackPostsSlider;
import com.jackson_siro.visongbook.models.PostsSlider;
import com.jackson_siro.visongbook.retrofitconfig.API;
import com.jackson_siro.visongbook.retrofitconfig.CallJson;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.setting.ToolsUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsSliderFragment extends Fragment{

    private View root_view;
    private View root_recycler;
    private Call<CallbackPostsLists> callbackPostsCall;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private ImageButton btn_next, btn_prev;
    private Handler handler = new Handler();
    private Runnable runnable = null;
    private PostsSliderAdapter postsSliderAdapter;
    private TextView title, date_posts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_slider, null);
        component();
        requestData();
        return root_view;
    }

    private void component() {
        root_recycler = (CardView) root_view.findViewById(R.id.lyt_cart);
        viewPager = (ViewPager) root_view.findViewById(R.id.pager);
        layout_dots = (LinearLayout) root_view.findViewById(R.id.layout_dots);
        btn_next = (ImageButton) root_view.findViewById(R.id.bt_next);
        btn_prev = (ImageButton) root_view.findViewById(R.id.bt_previous);
        title = (TextView) root_view.findViewById(R.id.featured_posts_title);
        date_posts = (TextView) root_view.findViewById(R.id.date_posts);
        postsSliderAdapter = new PostsSliderAdapter(getActivity(), new ArrayList<PostsSlider>());

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });
    }

    private void displayFromServer(List<PostsSlider> postsSliders){
        postsSliderAdapter.setItem(postsSliders);
        viewPager.setAdapter(postsSliderAdapter);
        final ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.height = ToolsUtils.getFeaturedPostsImageHeight(getActivity());
        viewPager.setLayoutParams(layoutParams);

        viewPager.setCurrentItem(0);
        title.setText(postsSliderAdapter.getItem(0).title);
        date_posts.setText(postsSliderAdapter.getItem(0).created);
        dotsAdd(layout_dots, postsSliderAdapter.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PostsSlider change_posts = postsSliderAdapter.getItem(position);
                title.setText(change_posts.title);
                date_posts.setText(change_posts.created);
                dotsAdd(layout_dots, postsSliderAdapter.getCount(), position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        autoSlider(postsSliderAdapter.getCount());
        postsSliderAdapter.setOnItemClickListener(new PostsSliderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostsSlider posts) {
                //vSongBook.navigateParent(getActivity(), posts.postid, false);
                Log.d("You click", posts.postid.toString());
            }
        });
        root_recycler.setVisibility(View.VISIBLE);

    }

    private void requestData() {
        API api = CallJson.callJson();
        api.PostsSlider().enqueue(new Callback<CallbackPostsSlider>() {
            @Override
            public void onResponse(Call<CallbackPostsSlider> call, Response<CallbackPostsSlider> response) {
                CallbackPostsSlider callbackPosts = response.body();
                if (callbackPosts != null){
                    displayFromServer(callbackPosts.data);
                }
            }

            @Override
            public void onFailure(Call<CallbackPostsSlider> call, Throwable t) {

            }
        });
    }

    public void autoSlider(final int slider){
        runnable = new Runnable() {
            @Override
            public void run() {
                int position = viewPager.getCurrentItem();
                position = position + 1;
                if (position >= slider) position = 0;
                viewPager.setCurrentItem(position);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void next(){
        int position = viewPager.getCurrentItem();
        position = position + 1;
        if (position >= postsSliderAdapter.getCount()) position = 0;
        viewPager.setCurrentItem(position);
    }

    private void prev(){
        int position = viewPager.getCurrentItem();
        position = position - 1;
        if (position < 0) position = postsSliderAdapter.getCount();
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void dotsAdd(LinearLayout layout_dots, int size, int current){
        ImageView[] dots_img = new ImageView[size];
        layout_dots.removeAllViews();
        for (int a = 0; a < dots_img.length; a++){
            dots_img[a] = new ImageView(getActivity());
            int w_h_dots = 16;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(w_h_dots, w_h_dots));
            layoutParams.setMargins(9,9,9,9);
            dots_img[a].setLayoutParams(layoutParams);
            dots_img[a].setImageResource(R.drawable.dots_shape);
            dots_img[a].setColorFilter(ContextCompat.getColor(getActivity(), R.color.black_color));
            layout_dots.addView(dots_img[a]);
        }
        if (dots_img.length > 1){
            dots_img[current].setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        }
    }
}
