package com.example.newsocialgift.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private static List<User> friendItemList;

    private String userFriends;

    private String userID;


    public FriendAdapter(List<User> friendItemList, String userFriends, String userID) {
        this.userID = userID;
        this.userFriends = userFriends;
        this.friendItemList = friendItemList;
    }

    public void setFriendItemList(List<User> friendItemList) {
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
        User user = friendItemList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageViewFriend);
        holder.textViewName.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return friendItemList.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewFriend;

        private final TextView textViewName;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFriend = itemView.findViewById(R.id.imageViewFriend);
            textViewName = itemView.findViewById(R.id.textViewFriendName);
            Button buttondeleteFriend = itemView.findViewById(R.id.buttonDeleteFriend);
            if(!userID.equals(userFriends)){
                buttondeleteFriend.setVisibility(View.INVISIBLE);
            }
            buttondeleteFriend.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = friendItemList.get(position);
                    deleteFriend(user.getId());
                }
            });
        }

        public void bind(User user) {
            // Aquí puedes asignar los datos del amigo a las vistas correspondientes
            textViewName.setText(user.getUsername());
            // Puedes usar una biblioteca de carga de imágenes como Picasso o Glide para cargar la imagen
            // en el ImageView imageView.
        }

        private void deleteFriend(String friendId) {
            String deleteUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends/" + friendId;

            RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                    response -> {
                        // La solicitud DELETE se completó con éxito
                        // Actualiza la lista de amigos eliminando el amigo correspondiente
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            friendItemList.remove(position);
                            notifyItemRemoved(position);
                        }
                    },
                    error -> {
                        // Error al procesar la solicitud DELETE
                        Log.e("DeleteFriend", "Error al eliminar el amigo: " + error.getMessage());
                        Toast.makeText(itemView.getContext(), "Error al eliminar el amigo", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("accept", "application/json");
                    headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIxLCJlbWFpbCI6ImFkbWluc0BnbWFpbC5jb20iLCJpYXQiOjE2ODYwNjY3ODN9.jFHgr1Y91KsUbgoDQm0RoEDLVEue1riCIclhcyn4V70");
                    return headers;
                }
            };

            requestQueue.add(stringRequest);
        }
    }
}
