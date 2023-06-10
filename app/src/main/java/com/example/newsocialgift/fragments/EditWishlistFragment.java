package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditWishlistFragment extends Fragment {

    private EditText etName;
    private EditText etDescription;
    private EditText etEndDate;
    private Button btnSaveChanges;

    private String WishlistID;

    private String URL = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/";

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_wishlist, container, false);

        etName = view.findViewById(R.id.et_name);
        etDescription = view.findViewById(R.id.et_description);
        etEndDate = view.findViewById(R.id.et_end_date);
        btnSaveChanges = view.findViewById(R.id.btn_save_changes);

        Bundle arguments = getArguments();
        if (arguments != null) {
            WishlistID = arguments.getString("wishlistID");

        }

        // Obtener los datos actuales de la wishlist y mostrarlos en los campos de texto
        getWishlistDetails();

        // Manejar el evento de clic en el botón "Save Changes"
        btnSaveChanges.setOnClickListener(v -> saveChanges());

        return view;
    }

    private void getWishlistDetails() {
        String url = URL + "wishlists/" + WishlistID;
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String TOKEN = preferences.getString("token", "");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String name = response.getString("name");
                        String description = response.getString("description");
                        String endDate = response.getString("end_date");

                        etName.setText(name);
                        etDescription.setText(description);
                        etEndDate.setText(endDate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Failed to get wishlist details", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void saveChanges() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String TOKEN = preferences.getString("token", "");
        System.out.println(name);
        System.out.println(description);
        System.out.println(endDate);
        // Construir el objeto JSON con los datos actualizados de la wishlist
        JSONObject wishlistData = new JSONObject();
        try {
            wishlistData.put("name", name);
            wishlistData.put("description", description);
            wishlistData.put("end_date", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Realizar la llamada a la API para actualizar la wishlist
        String url = URL + "wishlists/" + WishlistID;
        System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, wishlistData,
                response -> {
                    Toast.makeText(getContext(), "Wishlist updated successfully", Toast.LENGTH_SHORT).show();
                    // Realizar cualquier otra acción necesaria después de guardar los cambios
                    // Por ejemplo, puedes volver al fragmento anterior o actualizar la interfaz de usuario
                },
                error -> {
                    Toast.makeText(getContext(), "Failed to update wishlist", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + TOKEN);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}