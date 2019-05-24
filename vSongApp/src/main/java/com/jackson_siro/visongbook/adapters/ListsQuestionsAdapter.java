package com.jackson_siro.visongbook.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jackson_siro.visongbook.models.PostModel;

import java.util.ArrayList;
import java.util.List;

import com.jackson_siro.visongbook.retrofitconfig.BaseUrlConfig;
import com.jackson_siro.visongbook.R;

public class ListsQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PostModel> Questions;
    public OnItemClickListener onItemClickListener;
    public OnLoadMoreListener onLoadMoreListener;
    public boolean loading;
    private int VIEW_PROGRES = 0;
    private int VIEW_ITEM = 1;

    private final Context context;
    public interface OnItemClickListener{
        void onItemClick(View view, PostModel Questions);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ListsQuestionsAdapter(List<PostModel> Questions, Context context){
        this.Questions = Questions;
        this.context = context;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_posts, parent, false);
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
        final PostModel Song = Questions.get(position);
        MyViewHolder view = (MyViewHolder) holder;
        String details = Song.what  + " " + Song.when + " " + Song.where + " " + Song.who;

        view.post_title.setText(Song.title);
        view.post_netthumbs.setText(Song.netthumbs);
        view.post_acount.setText(Song.acount);
        view.post_details.setText(Html.fromHtml(details, Html.FROM_HTML_MODE_COMPACT));

        /*if (!Song.tags.isEmpty()) {
            String[] MyTags = TextUtils.split(Song.tags, ",");
            for (int i = 0; i < MyTags.length; i++) {
                TextView tagView = new TextView(context);F
                tagView.setText(MyTags[i]);
                tagView.setTextColor(context.getResources().getColor(R.color.white_color));
                tagView.setBackground(context.getResources().getDrawable(R.drawable.custom_tag_view));
                tagView.setPadding(20, 5, 20, 5);
                tagView.setTextSize(15);
                view.post_tags.addView(tagView);
            }
        }*/

        view.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view, Song);
                }
            }
        });
        Log.d("MyAdapter", "position: " + position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MaterialRippleLayout layout_parent;
        public TextView post_title, post_netthumbs, post_acount, post_details, post_tags;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.material_ripple);
            post_title = itemView.findViewById(R.id.post_title);
            post_netthumbs = itemView.findViewById(R.id.post_netthumbs);
            post_acount = itemView.findViewById(R.id.post_acount);
            post_details = itemView.findViewById(R.id.post_details);
            post_tags = itemView.findViewById(R.id.post_tags);
        }
    }

    @Override
    public int getItemCount() {
        return Questions.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return this.Questions.get(position) != null ? VIEW_ITEM : VIEW_PROGRES;
    }

    public void resetData(){
        this.Questions = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void insertData(List<PostModel> Questions){
        setLoaded();
        int itemStart = getItemCount();
        int itemCount = Questions.size();
        this.Questions.addAll(Questions);
        notifyItemRangeInserted(itemStart, itemCount);
    }

    public void setLoaded(){
        loading = false;
        for (int pl = 0; pl<getItemCount(); pl++){
            if (Questions.get(pl) == null){
                Questions.remove(pl);
                notifyItemRemoved(pl);
            }
        }
    }

    public void setLoading(){
        if (getItemCount() != 0){
            this.Questions.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    private void lastPostsView(RecyclerView recyclerView){
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastPosts = layoutManager.findLastVisibleItemPosition();
                    if (!loading && lastPosts == getItemCount() - 1 && onLoadMoreListener != null){
                        if (onLoadMoreListener != null){
                            int get = getItemCount() / BaseUrlConfig.RequestLoadMore;
                            onLoadMoreListener.onLoadMore(get);
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore(int page);
    }


}
