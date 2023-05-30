package com.example.newsocialgift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsocialgift.HomeModel;
import com.example.newsocialgift.R;
import com.example.newsocialgift.GiftItem;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements GiftAdapter.GiftListener {
    private List<HomeModel> mData;
    private GiftAdapter.GiftListener mListener;

    public HomeAdapter(List<HomeModel> data, GiftAdapter.GiftListener listener) {
        mData = data;
        mListener = listener;
    }

    @Override
    public void onGiftChecked(boolean isChecked, int position, int homeAdapterPosition) {
        // Redirige el evento al HomeFragment
        if (mListener != null) {
            mListener.onGiftChecked(isChecked, position, homeAdapterPosition);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeModel item = mData.get(position);
        holder.bind(item);

        // Configurar el RecyclerView interno
        List<GiftItem> giftItems = item.getGiftItems();
        GiftAdapter giftAdapter = new GiftAdapter(giftItems, this, position);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.mRecyclerView.setAdapter(giftAdapter);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public HomeModel getItem(int homeAdapterPosition) {
        return mData.get(homeAdapterPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
        private ImageButton mButton;
        private TextView mWishlistName;
        private TextView mWislistDescription;
        private RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.profileImage);
            mTextView = itemView.findViewById(R.id.profileName);
            mButton = itemView.findViewById(R.id.moreButton);
            mWishlistName = itemView.findViewById(R.id.wishlistName);
            mWislistDescription = itemView.findViewById(R.id.wishlistDescription);
            mRecyclerView = itemView.findViewById(R.id.recyclerView);
        }

        public void bind(HomeModel item) {
            //mImageView.setImageResource(item.getImageResource());
            //Picasso.get().load(item.getImageResource()).into(mImageView);
            Glide.with(this.itemView.getContext())
                    .load(item.getImageResource())
                    .circleCrop()
                    .into(mImageView);
            mTextView.setText(item.getText());
            mButton.setImageResource(item.getButtonImageResource());
            mWishlistName.setText(item.getWishlistName());
            mWislistDescription.setText(item.getWishlistDescription());
        }
    }
}
