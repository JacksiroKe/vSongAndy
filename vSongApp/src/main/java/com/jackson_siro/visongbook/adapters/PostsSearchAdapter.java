package com.jackson_siro.visongbook.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.List;

import com.jackson_siro.visongbook.models.PostsSearch;
import com.jackson_siro.visongbook.R;

public class PostsSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<PostsSearch> postsSearches;
    public OnItemClickListener onItemClickListener;
    private boolean loading;
    public interface OnItemClickListener{
        void onItemClick(View view, PostsSearch postsSearch, int i);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public PostsSearchAdapter(Activity activity, List<PostsSearch> postsSearches) {
        this.activity = activity;
        this.postsSearches = postsSearches;
    }

    public class SViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgsubCategory;
        public TextView textsubCategory;
        public MaterialRippleLayout lyt_parent;

        public SViewHolder(View itemView) {
            super(itemView);

            imgsubCategory = (ImageView) itemView.findViewById(R.id.imgsubCategory);
            textsubCategory = (TextView) itemView.findViewById(R.id.textsubCategory);
            lyt_parent = (MaterialRippleLayout) itemView.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sub_category, parent, false);
        return new SViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PostsSearch postsSearch = postsSearches.get(position);
        SViewHolder sViewHolder = (SViewHolder) holder;
        //Picasso.with(activity).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+ postsSearch.image).into(sViewHolder.imgsubCategory);
        sViewHolder.textsubCategory.setText(postsSearch.title);
        sViewHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view, postsSearch, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsSearches.size();
    }


    public void resetListData(){
        this.postsSearches = new ArrayList<>();
        notifyDataSetChanged();
    }
}

