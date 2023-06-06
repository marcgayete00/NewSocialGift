package com.example.newsocialgift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsocialgift.FriendItem;
import com.example.newsocialgift.R;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<FriendItem> friendItemList;

    public FriendAdapter(List<FriendItem> friendItemList) {
        this.friendItemList = friendItemList;
    }

    public void setFriendItemList(List<FriendItem> friendItemList) {
        this.friendItemList = friendItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        FriendItem friendItem = friendItemList.get(position);
        holder.bind(friendItem);
    }

    @Override
    public int getItemCount() {
        return friendItemList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textViewName;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewFriend);
            textViewName = itemView.findViewById(R.id.textViewFriendName);
        }

        public void bind(FriendItem friendItem) {
            // Aquí puedes asignar los datos del amigo a las vistas correspondientes
            // Por ejemplo:
            textViewName.setText(friendItem.getName());
            // Puedes usar una biblioteca de carga de imágenes como Picasso o Glide para cargar la imagen
            // en el ImageView imageView.
        }
    }
}
