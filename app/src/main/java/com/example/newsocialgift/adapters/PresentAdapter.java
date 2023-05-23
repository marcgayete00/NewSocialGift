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
import com.example.newsocialgift.PresentItem;

import java.util.List;

public class PresentAdapter extends RecyclerView.Adapter<PresentAdapter.ViewHolder> {
    private List<PresentItem> items;

    public PresentAdapter(List<PresentItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_presents_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PresentItem item = items.get(position);
        holder.wishlistImage.setImageResource(item.getImageResId());
        holder.changeMyName.setText(item.getChangeName());
        holder.checkBox.setChecked(item.isChecked());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView wishlistImage;
        public TextView changeMyName;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            wishlistImage = itemView.findViewById(R.id.wishlistImage);
            changeMyName = itemView.findViewById(R.id.changeMyName);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}

    //TODO: mTextView2.setText(item.getRectangleWithText());
