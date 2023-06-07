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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.newsocialgift.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<String> ids;
    private List<String> names;
    private List<String> images;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profileImage;
        public LinearLayout linearLayout;

        public Button accept;
        public Button reject;

        public MyViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.image_view);
            linearLayout = itemView.findViewById(R.id.layout_notification);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);

        }
    }

    public NotificationAdapter( List <String> ids, List<String> names, List<String> images) {
        this.ids = ids;
        this.names = names;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.username.setText(names.get(position));
        Glide.with(holder.itemView.getContext())
                .load(images.get(position))
                .circleCrop()
                .into(holder.profileImage);


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(v, position, 0);
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(v, position, 1);
            }
        });

    }

    public void request(View v, int position, int action){
        SharedPreferences preferences = v.getContext().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        System.out.println(token);

        JsonObjectRequest jsonObjectRequest = null;

        // Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());

        // URL de la API para hacer la búsqueda
        System.out.println(ids.get(position));
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends/"+ids.get(position);

        // Crear el objeto JSON con los datos del usuario
        JSONObject jsonObject = new JSONObject();
        // Crear la petición GET con Volley

        int requestMethod = 0;
        switch (action) {
            case 0:
                requestMethod = Request.Method.PUT;
                break;
            case 1:
                requestMethod = Request.Method.DELETE;
                break;
        }

        jsonObjectRequest = new JsonObjectRequest(requestMethod, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        //borrar elemento del recycler
                        ids.remove(position);
                        names.remove(position);
                        images.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // La petición falló
                        // Mostrar un toast
                        System.out.println(error);
                        Toast.makeText(v.getContext(), "Error en la petición", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put( "Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Añadir la petición a la cola
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}