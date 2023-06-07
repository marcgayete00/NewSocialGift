package com.example.newsocialgift.fragments;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddGiftFragment extends Fragment {

    private EditText etProductUrl;
    private EditText etPriority;
    private Button btnAddGift;

    private String URL = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts";

    private String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTI1LCJlbWFpbCI6ImFkbWluNkBnbWFpbC5jb20iLCJpYXQiOjE2ODI1MjQ0NDd9.a-RQGEZwgvYJJfbI0yYcIV_0pESm1fTcvwlwjljCJjU";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_gift, container, false);

        etProductUrl = view.findViewById(R.id.et_product_url);
        etPriority = view.findViewById(R.id.et_priority);
        btnAddGift = view.findViewById(R.id.btn_add_gift);

        btnAddGift.setOnClickListener(v -> addGift());

        return view;
    }

    private void addGift() {
        String productUrl = etProductUrl.getText().toString();
        int priority = Integer.parseInt(etPriority.getText().toString());
        Bundle arguments = getArguments();

        String wishlistID = null;
        if (arguments != null) {
            wishlistID = arguments.getString("wishlistID");
        }
        // Crear el objeto JSON con los datos del regalo
        JSONObject giftData = new JSONObject();
        try {
            giftData.put("wishlist_id", wishlistID);  // Aquí debes obtener el ID de la wishlist actual
            giftData.put("product_url", productUrl);
            giftData.put("priority", priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Realizar la solicitud POST a la API para agregar el regalo
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, giftData,
                response -> {
                    // Regresar al fragmento anterior
                    requireActivity().getSupportFragmentManager().popBackStack();
                },
                error -> {
                    // Manejar el error de la solicitud
                    Toast.makeText(requireContext(), "Error al agregar el regalo", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Agregar la solicitud a la cola de solicitudes Volley
        Volley.newRequestQueue(requireContext()).add(request);
    }
}
