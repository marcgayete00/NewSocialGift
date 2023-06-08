package com.example.newsocialgift.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.newsocialgift.MessagesModel;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newsocialgift.R;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private List<MessagesModel> mData;
    private OnItemClickListener mListener;

    // Interfaz para el listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public MessagesAdapter(List<MessagesModel> data, OnItemClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MessagesModel item = mData.get(position);
        holder.bind(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.profileImage);
            mTextView = itemView.findViewById(R.id.profileName);
        }

        public void bind(MessagesModel item) {
            Glide.with(this.itemView.getContext())
                    .load(item.getImageURL())
                    .circleCrop()
                    .into(mImageView);
            mTextView.setText(item.getName());
        }
    }
}