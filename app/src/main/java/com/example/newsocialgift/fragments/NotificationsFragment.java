package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.R;
import com.example.newsocialgift.adapters.NotificationAdapter;
import com.example.newsocialgift.adapters.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsFragment  extends Fragment {
    private static final String ARG_ICON = "ARG_ICON";
    private TextView title;



    public static NotificationsFragment newInstance(@DrawableRes int iconId) {
        NotificationsFragment frg = new NotificationsFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        List<String> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> images = new ArrayList<>();

        //Vincular con adapter NotificationAdapter
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        // Configurar el adaptador y el LinearLayoutManager
        NotificationAdapter adapter = new NotificationAdapter(ids, names, images);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        title = view.findViewById(R.id.title);

        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonArrayRequest jsonArrayRequest = null;

        // Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        // URL de la API para hacer la búsqueda
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends/requests";

        // Crear el objeto JSON con los datos del usuario
        JSONArray jsonArray = new JSONArray();
        // Crear la petición GET con Volley
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Separar los datos y mostrarlos
                        System.out.println(response);

                        try{
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                ids.add(jsonObject.getString("id"));
                                names.add(jsonObject.getString("name"));
                                images.add(jsonObject.getString("image"));
                                System.out.println(jsonObject.getString("name"));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // La petición falló
                        // Mostrar un toast
                        System.out.println(error);
                        Toast.makeText(requireContext(), "Error en la petición", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Añadir la petición a la cola
        requestQueue.add(jsonArrayRequest);


        return view;
    }

}
