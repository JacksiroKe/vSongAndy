package com.jackson_siro.visongbook.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.PostModel;

import java.util.ArrayList;
import java.util.List;

public class ListsAnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<PostModel> thisPost;
    public OnItemClickListener onItemClickListener;
    public OnLoadMoreListener onLoadMoreListener;
    public boolean loading;
    private int VIEW_PROGRES = 0;
    private int VIEW_ITEM = 1;

    public interface OnItemClickListener{
        void onItemClick(View view, PostModel thisPost);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ListsAnswersAdapter(Activity activity, List<PostModel> thisPost){
        this.activity = activity;
        this.thisPost = thisPost;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_answers, parent, false);
            viewHolder = new MyViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_data, parent, false);
            viewHolder = new MyViewHolder(view);
        }
        return viewHolder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PostModel listItem = thisPost.get(position);
        MyViewHolder view = (MyViewHolder) holder;
        String details = listItem.what  + " " + listItem.when + " " + listItem.who;

        view.post_netthumbs.setText(listItem.netthumbs);
        view.post_details.setText(Html.fromHtml(details, Html.FROM_HTML_MODE_COMPACT));
        view.post_content.setText(Html.fromHtml(listItem.content, Html.FROM_HTML_MODE_COMPACT));

        view.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (onItemClickListener != null){
                onItemClickListener.onItemClick(view, listItem);
            }
            }
        });
        Log.d("MyAdapter", "position: " + position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MaterialRippleLayout layout_parent;
        public TextView post_netthumbs, post_details, post_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.material_ripple);
            post_netthumbs = itemView.findViewById(R.id.post_netthumbs);
            post_details = itemView.findViewById(R.id.post_details);
            post_content = itemView.findViewById(R.id.post_content);
        }
    }

    @Override
    public int getItemCount() {
        return thisPost.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return this.thisPost.get(position) != null ? VIEW_ITEM : VIEW_PROGRES;
    }

    public void resetData(){
        this.thisPost = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void insertData(List<PostModel> thisPost){
        setLoaded();
        int itemStart = getItemCount();
        int itemCount = thisPost.size();
        this.thisPost.addAll(thisPost);
        notifyItemRangeInserted(itemStart, itemCount);
    }

    public void setLoaded(){
        loading = false;
        for (int pl = 0; pl<getItemCount(); pl++){
            if (thisPost.get(pl) == null){
                thisPost.remove(pl);
                notifyItemRemoved(pl);
            }
        }
    }

    public void setLoading(){
        if (getItemCount() != 0){
            this.thisPost.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore(int page);
    }


}
