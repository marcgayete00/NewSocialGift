package com.example.newsocialgift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsocialgift.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private List<String> listaElementos;
    private List<String> listaElementos2;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView profileImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            profileImage = itemView.findViewById(R.id.image_view);
        }
    }

    public SearchAdapter(List<String> listaElementos, List<String> listaElementos2) {
        this.listaElementos = listaElementos;
        this.listaElementos2 = listaElementos2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(listaElementos.get(position));
        Glide.with(holder.itemView.getContext())
                .load(listaElementos2.get(position))
                .circleCrop()
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return listaElementos.size();
    }
}