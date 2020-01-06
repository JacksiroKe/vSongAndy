package com.jackson_siro.visongbook.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jackson_siro.visongbook.models.PostModel;

import java.util.List;

import com.jackson_siro.visongbook.retrofitconfig.BaseUrlConfig;
import com.jackson_siro.visongbook.R;

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

        String details, alias;
        try
        {
            if (Song.categoryname.isEmpty()) details = "";
            else details = Song.categoryname + "; ";
        }
        catch (Exception ex)
        {
            details = "";
        }

        try
        {
            String[] songconts = TextUtils.split(Song.content, "\n\n");
            if (Song.content.contains("CHORUS"))
                details = details + (songconts.length - 1) + " Verse" + (songconts.length == 1 ? "" : "s") + ", Has Chorus";
            else details = details + songconts.length + " Verse" + (songconts.length == 1 ? "" : "s") + ", No Chorus";
        }
        catch (Exception ex)
        {
            if (details.isEmpty()) view.song_details.setVisibility(View.GONE);
        }

        try
        {
            if (Song.alias.isEmpty()) alias = "";
            else alias = "\n" + Song.alias;
        }
        catch (Exception ex)
        {
            alias = "";
        }

        view.song_title.setText(Song.number + "# " + Song.title + alias);
        view.song_content.setText(Song.content);
        view.song_details.setText(details);

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
        public TextView song_title, song_content, song_details;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.material_ripple);
            song_title = itemView.findViewById(R.id.song_title);
            song_content = itemView.findViewById(R.id.song_content);
            song_details = itemView.findViewById(R.id.song_details);
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

    public interface OnLoadMoreListener{
        void onLoadMore(int page);
    }


}
