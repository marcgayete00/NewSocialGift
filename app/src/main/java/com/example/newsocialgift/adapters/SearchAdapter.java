package com.example.newsocialgift.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsocialgift.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private List<String> listaElementos;
    private List<String> listaElementos2;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profileImage;
        public LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.image_view);
            linearLayout = itemView.findViewById(R.id.layout);
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
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.username.setText(listaElementos.get(position));
        Glide.with(holder.itemView.getContext())
                .load(listaElementos2.get(position))
                .circleCrop()
                .into(holder.profileImage);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

        /*holder.usernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return listaElementos.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}