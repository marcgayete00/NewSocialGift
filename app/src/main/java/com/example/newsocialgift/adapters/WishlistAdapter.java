package com.example.newsocialgift.adapters;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.newsocialgift.R;
import com.example.newsocialgift.GiftItem;
import com.example.newsocialgift.fragments.EditGiftFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private List<GiftItem> items;

    private Context context;

    private ViewHolder holderG;

    private String TOKEN;


    public WishlistAdapter(List<GiftItem> items, Context context, String token) {
        this.TOKEN = token;
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gift, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holderG = holder;
        GiftItem item = items.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getImageResId())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.wishlistImage);
        holder.giftName.setText(item.getGiftName());
        holder.checkBox.setChecked(item.isChecked());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Actualizar el estado de reserva del regalo en el objeto GiftItem
            item.setChecked(isChecked);

            // Llamar a la función para reservar o cancelar la reserva del regalo según el estado del checkbox
            if (isChecked) {
                reserveGift(item.getGiftId());
            } else {
                cancelReservation(item.getGiftId());
            }
        });
        holder.editButton.setOnClickListener(v -> {
            // Obtener el regalo seleccionado
            GiftItem gift = items.get(position);

            // Abrir el fragmento de edición de regalo y pasar los datos del regalo
            Fragment editGiftFragment = new EditGiftFragment();
            Bundle args = new Bundle();
            args.putString("giftId", gift.getGiftId());
            editGiftFragment.setArguments(args);

            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, editGiftFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });
        holder.deletebutton.setOnClickListener(v -> {
            items.remove(position);
            notifyDataSetChanged();
            deleteGiftFromAPI(item.getGiftId());
        });
    }

    private void reserveGift(String giftId) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + giftId + "/book";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    // La reserva del regalo fue exitosa, maneja la respuesta aquí
                    Toast.makeText(holderG.itemView.getContext(), "Gift reserved successfully", Toast.LENGTH_SHORT).show();
                    // Realiza cualquier otra acción necesaria después de reservar el regalo
                },
                error -> {
                    // Error al realizar la solicitud de reserva, maneja el error aquí
                    Toast.makeText(holderG.itemView.getContext(), "Failed to reserve gift", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }
        };

        RequestQueue mQueue = Volley.newRequestQueue(holderG.itemView.getContext());
        mQueue.add(request);
    }

    private void cancelReservation(String giftId) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + giftId + "/book";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    // La cancelación de la reserva del regalo fue exitosa, maneja la respuesta aquí
                    Toast.makeText(holderG.itemView.getContext(), "Gift reservation cancelled successfully", Toast.LENGTH_SHORT).show();
                    // Realiza cualquier otra acción necesaria después de cancelar la reserva del regalo
                },
                error -> {
                    // Error al realizar la solicitud de cancelación de reserva, maneja el error aquí
                    Toast.makeText(holderG.itemView.getContext(), "Failed to cancel gift reservation", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }
        };

        RequestQueue mQueue = Volley.newRequestQueue(holderG.itemView.getContext());
        mQueue.add(request);
    }

    private void deleteGiftFromAPI(String giftId) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + giftId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    // La solicitud de eliminación fue exitosa, maneja la respuesta aquí
                    Toast.makeText(holderG.itemView.getContext(), "Gift deleted successfully", Toast.LENGTH_SHORT).show();
                    // Realiza cualquier otra acción necesaria después de eliminar el regalo
                },
                error -> {
                    // Error al realizar la solicitud, maneja el error aquí
                    Toast.makeText(holderG.itemView.getContext(), "Failed to delete gift", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }
        };

        RequestQueue mQueue = Volley.newRequestQueue(holderG.itemView.getContext());
        mQueue.add(request);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView wishlistImage;
        public TextView giftName;

        public Button editButton;
        public Button deletebutton;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            wishlistImage = itemView.findViewById(R.id.wishlistImage);
            giftName = itemView.findViewById(R.id.giftName);
            editButton = itemView.findViewById(R.id.editGiftButton);
            deletebutton = itemView.findViewById(R.id.deleteGiftButton);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
