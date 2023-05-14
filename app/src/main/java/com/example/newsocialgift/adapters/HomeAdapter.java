package com.example.newsocialgift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newsocialgift.HomeModel;
import com.example.newsocialgift.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<HomeModel> mData;

    public HomeAdapter(List<HomeModel> data) {
        mData = data;
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
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
        private ImageButton mButton;
        private TextView mTextView2;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.myImage);
            mTextView = itemView.findViewById(R.id.profileName);
            mButton = itemView.findViewById(R.id.moreButton);
            mTextView2 = itemView.findViewById(R.id.wishlistName);
        }

        public void bind(HomeModel item) {
            mImageView.setImageResource(item.getImageResource());
            mTextView.setText(item.getText());
            mButton.setImageResource(item.getButtonImageResource());
            mTextView2.setText(item.getRectangleWithText());
        }
    }
}