package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditGiftFragment extends Fragment {
    private String giftId;
    private TextView wishlistID;

    private TextView product_url;

    private TextView priority;

    private Button btnsave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento y configurar los elementos de interfaz de usuario
        View view = inflater.inflate(R.layout.fragment_edit_gift, container, false);
        // Obtener el ID del regalo seleccionado desde los argumentos
        wishlistID = view.findViewById(R.id.et_wishlistid);
        product_url = view.findViewById(R.id.et_product_url);
        priority = view.findViewById(R.id.et_priority);
        btnsave = view.findViewById(R.id.btn_save_changes);
        Bundle args = getArguments();
        if (args != null) {
            giftId = args.getString("giftId");
        }
        btnsave.setOnClickListener(v -> {
            updateGift(giftId, wishlistID.getText().toString(), product_url.getText().toString(), priority.getText().toString());
        });
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();


        // Obtener los datos actuales de la wishlist y mostrarlos en los campos de texto
        getGiftDetails();

        return view;
    }

    private void getGiftDetails(){
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + giftId;
        System.out.println(url);
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String TOKEN = preferences.getString("token", "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        wishlistID.setText(response.getString("wishlist_id"));
                        product_url.setText(response.getString("product_url"));
                        priority.setText(response.getString("priority"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Error handling
                    System.out.println("Something went wrong!");
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Agregar los encabezados a la solicitud
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                headers.put("accept", "application/json");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        // Agregar la solicitud a la cola de peticiones
        requestQueue.add(jsonObjectRequest);
    }

    private void updateGift(String giftId, String wishlistId, String productUrl, String priority) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + giftId;
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String TOKEN = preferences.getString("token", "");
        String userJson = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        String userID = user.getId();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("wishlist_id", wishlistId);
            requestBody.put("product_url", productUrl);
            requestBody.put("priority", priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(requestBody);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // La solicitud de actualización fue exitosa, maneja la respuesta aquí
                        Toast.makeText(getContext(), "Gift updated successfully", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle wishlistArguments = new Bundle();
                        wishlistArguments.putString("wishlistID", wishlistId);
                        wishlistArguments.putString("userID", userID);
                        WishListFragment wishListFragment = new WishListFragment();
                        wishListFragment.setArguments(wishlistArguments);
                        fragmentTransaction.replace(R.id.container, wishListFragment);
                        fragmentTransaction.commit();                  }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error al realizar la solicitud de actualización, maneja el error aquí
                        Toast.makeText(getContext(), "Failed to update gift", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }
        };

        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        mQueue.add(request);
    }

}
