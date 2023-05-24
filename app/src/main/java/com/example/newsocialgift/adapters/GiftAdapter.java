package com.example.newsocialgift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsocialgift.R;
import com.example.newsocialgift.GiftItem;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {
    private List<GiftItem> items;

    public GiftAdapter(List<GiftItem> items) {
        this.items = items;
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
        holder.wishlistImage.setImageResource(item.getImageResId());
        holder.giftName.setText(item.getGiftName());
        holder.checkBox.setChecked(item.isChecked());
    }

    @Override
    public int getItemCount() {
        return items.size();
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
