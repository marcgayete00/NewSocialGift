package com.example.newsocialgift.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.newsocialgift.R;
import com.example.newsocialgift.GiftItem;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {
    public interface GiftListener {
        void onGiftChecked(boolean isChecked, int position);
    }
    private List<GiftItem> items;
    private GiftListener mListener;

    public GiftAdapter(List<GiftItem> items, GiftListener listener) {
        this.items = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_gifts_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiftItem item = items.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getImageResId())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.wishlistImage);
        holder.giftName.setText(item.getGiftName());
        holder.checkBox.setChecked(item.isChecked());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mListener != null) {
                mListener.onGiftChecked(isChecked, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setGiftItemList(List<GiftItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView wishlistImage;
        public TextView giftName;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            wishlistImage = itemView.findViewById(R.id.wishlistImage);
            giftName = itemView.findViewById(R.id.giftName);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
