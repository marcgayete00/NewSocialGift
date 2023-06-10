package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class AddGiftFragment extends Fragment {

    private EditText etProductUrl;
    private EditText etPriority;
    private Button btnAddGift;

    private String URL = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_gift, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();
        etProductUrl = view.findViewById(R.id.et_product_url);
        etPriority = view.findViewById(R.id.et_priority);
        btnAddGift = view.findViewById(R.id.btn_add_gift);
        btnAddGift.setOnClickListener(v -> addGift());

        return view;
    }

    private void addGift() {
        String productUrl = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products/";
        productUrl = productUrl.concat(etProductUrl.getText().toString());
        String priority = etPriority.getText().toString();
        Bundle arguments = getArguments();
        System.out.println(productUrl);
        System.out.println(priority);
        String wishlistID = null;
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String TOKEN = preferences.getString("token", "");
        if (arguments != null) {
            wishlistID = arguments.getString("wishlistID");
        }
        System.out.println(wishlistID);
        // Crear el objeto JSON con los datos del regalo
        JSONObject giftData = new JSONObject();
        try {
            giftData.put("wishlist_id", wishlistID);
            giftData.put("product_url", productUrl);
            giftData.put("priority", priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(giftData);
        // Realizar la solicitud POST a la API para agregar el regalo
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, giftData,
                response -> {
                    Toast.makeText(requireContext(), "Gift created successfully", Toast.LENGTH_SHORT).show();

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
                headers.put("accept", "application/json");
                return headers;
            }
        };

        RequestQueue mQueue = Volley.newRequestQueue(requireContext());
        mQueue.add(request);
    }
}
