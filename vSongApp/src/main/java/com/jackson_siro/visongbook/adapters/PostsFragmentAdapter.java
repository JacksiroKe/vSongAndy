package com.jackson_siro.visongbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.PostModel;

import java.util.ArrayList;
import java.util.List;

public class PostsFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PostModel> postsall = new ArrayList<>();
    private ArrayList<String> imgPath;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, PostModel PostModel);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public PostsFragmentAdapter(Context context, List<PostModel> postsall){
        this.context = context;
        this.postsall = postsall;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_posts, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            final PostModel PostModel = postsall.get(position);
            //Glide.with(context).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+PostSinglex.image).into(viewHolder.imgBig);
            viewHolder.textCategory.setText(PostModel.title);
            viewHolder.material_ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(view, PostModel);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return postsall.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setPosts(List<PostModel> posts){
        this.postsall = posts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgBig;
        public TextView textCategory;
        public MaterialRippleLayout material_ripple;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBig = itemView.findViewById(R.id.imgBig);
            textCategory = itemView.findViewById(R.id.textCategory);
            material_ripple = itemView.findViewById(R.id.material_ripple);
        }
    }
}
