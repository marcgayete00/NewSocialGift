package com.example.newsocialgift.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.newsocialgift.Main;
import com.example.newsocialgift.R;
import com.example.newsocialgift.activities.Login;
import com.example.newsocialgift.fragments.SearchFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private List<String> listaElementos;
    private List<String> listaElementos2;
    private List<String> ids;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profileImage;
        public LinearLayout linearLayout;

        public Button addFriend;

        public MyViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.image_view);
            linearLayout = itemView.findViewById(R.id.layout);
            addFriend = itemView.findViewById(R.id.addfriend);
        }
    }

    public SearchAdapter(List<String>ids, List<String> listaElementos, List<String> listaElementos2) {
        this.listaElementos = listaElementos;
        this.listaElementos2 = listaElementos2;
        this.ids = ids;
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

        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = v.getContext().getSharedPreferences("SocialGift", MODE_PRIVATE);
                String token = preferences.getString("token", "");
                JsonObjectRequest jsonObjectRequest = null;

                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());

                String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends/"+ids.get(position);

                JSONObject jsonObject = new JSONObject();

                jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(v.getContext(), "Friend request sent", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // La petición falló
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + token); // Agregar el token al encabezado
                        return headers;
                    }
                };

                requestQueue.add(jsonObjectRequest);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listaElementos.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}