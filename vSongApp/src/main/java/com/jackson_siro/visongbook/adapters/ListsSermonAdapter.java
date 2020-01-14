package com.jackson_siro.visongbook.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.*;

import java.util.List;

public class ListsSermonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SermonModel> Sermons;
    public OnItemClickListener onItemClickListener;
    public OnLoadMoreListener onLoadMoreListener;
    public boolean loading;
    private int VIEW_PROGRES = 0;
    private int VIEW_ITEM = 1;

    private final Context context;
    public interface OnItemClickListener{
        void onItemClick(View view, SermonModel Sermons);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ListsSermonAdapter(List<SermonModel> Sermons, Context context){
        this.Sermons = Sermons;
        this.context = context;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_sermons, parent, false);
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
        final SermonModel Sermon = Sermons.get(position);
        MyViewHolder view = (MyViewHolder) holder;

        view.sermon_title.setText(Sermon.title );
        view.sermon_content.setText(Sermon.content);
        view.sermon_details.setText(Sermon.preacher + ", " + Sermon.place);
        view.sermon_preached.setText(Sermon.created);

        view.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view, Sermon);
                }
            }
        });
        Log.d("MyAdapter", "position: " + position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MaterialRippleLayout layout_parent;
        public TextView sermon_title, sermon_content, sermon_details, sermon_preached;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.material_ripple);
            sermon_title = itemView.findViewById(R.id.sermon_title);
            sermon_content = itemView.findViewById(R.id.sermon_content);
            sermon_details = itemView.findViewById(R.id.sermon_details);
            sermon_preached = itemView.findViewById(R.id.sermon_preached);
        }
    }

    @Override
    public int getItemCount() {
        return Sermons.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return this.Sermons.get(position) != null ? VIEW_ITEM : VIEW_PROGRES;
    }

    public void insertData(List<SermonModel> Sermons){
        setLoaded();
        int itemStart = getItemCount();
        int itemCount = Sermons.size();
        this.Sermons.addAll(Sermons);
        notifyItemRangeInserted(itemStart, itemCount);
    }

    public void setLoaded(){
        loading = false;
        for (int pl = 0; pl<getItemCount(); pl++){
            if (Sermons.get(pl) == null){
                Sermons.remove(pl);
                notifyItemRemoved(pl);
            }
        }
    }

    public void setLoading(){
        if (getItemCount() != 0){
            this.Sermons.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore(int page);
    }


}
