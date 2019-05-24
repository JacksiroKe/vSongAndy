package com.jackson_siro.visongbook.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.PostModel;
import com.jackson_siro.visongbook.retrofitconfig.BaseUrlConfig;

import java.util.ArrayList;
import java.util.List;

public class ListsSongsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PostModel> Songs;
    public OnItemClickListener onItemClickListener;
    public OnLoadMoreListener onLoadMoreListener;
    public boolean loading;
    private int VIEW_PROGRES = 0;
    private int VIEW_ITEM = 1;

    private final Context context;
    public interface OnItemClickListener{
        void onItemClick(View view, PostModel Songs);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ListsSongsAdapter(List<PostModel> Songs, Context context){
        this.Songs = Songs;
        this.context = context;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_songs, parent, false);
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
        final PostModel Song = Songs.get(position);
        MyViewHolder view = (MyViewHolder) holder;

        view.post_title.setText(Song.number + "# " + Song.title);
        view.post_details.setText(Song.content);

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
        public TextView post_title, post_details;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.material_ripple);
            post_title = itemView.findViewById(R.id.post_title);
            post_details = itemView.findViewById(R.id.post_details);
        }
    }

    @Override
    public int getItemCount() {
        return Songs.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return this.Songs.get(position) != null ? VIEW_ITEM : VIEW_PROGRES;
    }

    public void resetData(){
        this.Songs = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void insertData(List<PostModel> Songs){
        setLoaded();
        int itemStart = getItemCount();
        int itemCount = Songs.size();
        this.Songs.addAll(Songs);
        notifyItemRangeInserted(itemStart, itemCount);
    }

    public void setLoaded(){
        loading = false;
        for (int pl = 0; pl<getItemCount(); pl++){
            if (Songs.get(pl) == null){
                Songs.remove(pl);
                notifyItemRemoved(pl);
            }
        }
    }

    public void setLoading(){
        if (getItemCount() != 0){
            this.Songs.add(null);
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
