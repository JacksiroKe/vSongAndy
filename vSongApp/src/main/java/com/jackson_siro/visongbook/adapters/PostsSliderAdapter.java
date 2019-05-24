package com.jackson_siro.visongbook.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.List;

import com.jackson_siro.visongbook.models.PostsSlider;
import com.jackson_siro.visongbook.R;

public class PostsSliderAdapter extends PagerAdapter{

    private Activity activity;
    private List<PostsSlider> postsList;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, PostsSlider posts);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public PostsSliderAdapter(Activity activity, List<PostsSlider> postsList) {
        this.activity = activity;
        this.postsList = postsList;
    }

    @Override
    public int getCount() {
        return postsList.size();
    }

    public PostsSlider getItem(int position){
        return postsList.get(position);
    }

    public void setItem(List<PostsSlider> posts){
        this.postsList = posts;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final PostsSlider posts = postsList.get(position);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_posts_image, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.image);
        MaterialRippleLayout mtr = (MaterialRippleLayout) view.findViewById(R.id.lyt_parent);
        //Glide.with(activity).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+posts.image).into(img);
        mtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view, posts);
                }
            }
        });
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
