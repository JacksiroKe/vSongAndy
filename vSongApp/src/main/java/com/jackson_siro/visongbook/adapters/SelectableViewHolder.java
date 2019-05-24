package com.jackson_siro.visongbook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.SelectableBook;

public class SelectableViewHolder extends RecyclerView.ViewHolder {

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    RelativeLayout cardView;
    SelectableBook mItem;
    ImageView checked;
    TextView title, slug, number, songs;
    OnItemSelectedListener itemSelectedListener;

    public SelectableViewHolder(View view, OnItemSelectedListener listener) {
        super(view);
        itemSelectedListener = listener;
        cardView = view.findViewById(R.id.cardViewLayout);
        title = view.findViewById(R.id.title);
        slug = view.findViewById(R.id.slug);
        songs = view.findViewById(R.id.songs);
        number = view.findViewById(R.id.number);
        checked = view.findViewById(R.id.checked);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItem.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(mItem);
            }
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            cardView.setBackgroundResource(R.drawable.custom_selected);
            checked.setImageResource(R.drawable.ic_done);
            title.setTextColor(cardView.getContext().getResources().getColor(R.color.white_color));
            slug.setTextColor(cardView.getContext().getResources().getColor(R.color.white_color));
            songs.setTextColor(cardView.getContext().getResources().getColor(R.color.white_color));
        } else {
            cardView.setBackgroundResource(R.drawable.custom_edit_text1);
            checked.setImageResource(R.drawable.ic_check_circle_dark);
            title.setTextColor(cardView.getContext().getResources().getColor(R.color.black));
            slug.setTextColor(cardView.getContext().getResources().getColor(R.color.black));
            songs.setTextColor(cardView.getContext().getResources().getColor(R.color.black));
        }
        mItem.setSelected(value);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(SelectableBook item);
    }

}