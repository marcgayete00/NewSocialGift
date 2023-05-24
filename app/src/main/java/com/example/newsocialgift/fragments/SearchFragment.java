package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.Main;
import com.example.newsocialgift.R;
import com.example.newsocialgift.activities.EditProfile;
import com.example.newsocialgift.activities.Login;
import com.example.newsocialgift.adapters.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment  extends Fragment {
    private static final String ARG_ICON = "ARG_ICON";
    private EditText search;

    private Button searchButton;
    private Button cancel;

    public static SearchFragment newInstance(@DrawableRes int iconId) {
        SearchFragment frg = new SearchFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        search = view.findViewById(R.id.search);
        searchButton = view.findViewById(R.id.search_button);
        cancel = view.findViewById(R.id.cancel_button);


        List<String> names = new ArrayList<>();
        List<String> images = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        SearchAdapter sAdapter = new SearchAdapter(names, images);
        sAdapter.setOnItemClickListener(position -> {
            FragmentManager searchFragmentManager = getActivity().getSupportFragmentManager();
            Bundle arguments = new Bundle();
            arguments.putString("id", ids.get(position));
            FragmentTransaction searchFragmentTransaction = searchFragmentManager.beginTransaction();
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(arguments);
            searchFragmentTransaction.replace(R.id.container, profileFragment);
            searchFragmentTransaction.commit();
        });
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                //Esconder teclado
                // Obtiene el servicio de entrada de método
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                names.clear();
                images.clear();
                sAdapter.notifyDataSetChanged();

                SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
                String token = preferences.getString("token", "");

                String text = search.getText().toString();

                JsonArrayRequest jsonArrayRequest = null;

                // Crear la cola de peticiones
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

                // URL de la API para hacer la búsqueda
                String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/search?s=" + text;

                // Crear el objeto JSON con los datos del usuario
                JSONArray jsonArray = new JSONArray();
                // Crear la petición GET con Volley
                jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, jsonArray,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    // Separar los datos y mostrarlos
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        String id = jsonObject.getString("id");
                                        String name = jsonObject.getString("name");
                                        String image = jsonObject.getString("image");
                                        ids.add(id);
                                        names.add(name);
                                        images.add(image);
                                    }
                                    sAdapter.notifyDataSetChanged();
                                    // Aquí puedes realizar cualquier otra operación con los datos

                                } catch (JSONException e) {
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
            }
        });


        return view;
    }

}
