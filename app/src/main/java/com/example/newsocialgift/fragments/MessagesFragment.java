package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
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
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.MessagesModel;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.example.newsocialgift.adapters.MessagesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesFragment  extends Fragment implements MessagesAdapter.OnItemClickListener {
    private static final String ARG_ICON = "ARG_ICON";
    private RecyclerView mRecyclerView;
    private MessagesAdapter mAdapter;
    private EditText searchBar;
    private Button cancelButton;
    private Button searchButton;
    private List<MessagesModel> mData = new ArrayList<>();

    private interface UserCallback {
        void onSuccess(User[] users);
        void onError(String error);
    }

    public static MessagesFragment newInstance(@DrawableRes int iconId) {
        MessagesFragment frg = new MessagesFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        searchBar = view.findViewById(R.id.search);
        cancelButton = view.findViewById(R.id.cancelButton);
        searchButton = view.findViewById(R.id.searchButton);

        loadUsers(new UserCallback() {
            @Override
            public void onSuccess(User[] users) {
                for (int i = 0; i < users.length; i++) {
                    mRecyclerView = view.findViewById(R.id.recyclerView);
                    mData.add(new MessagesModel(users[i].getId(), users[i].getImage(), users[i].getUsername()));
                    mAdapter = new MessagesAdapter(mData, MessagesFragment.this);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onError(String error) {
                // Mostrar mensaje de error
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
                //Esconder teclado
                // Obtiene el servicio de entrada de método
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.clear();

                SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
                String token = preferences.getString("token", "");

                String text = searchBar.getText().toString();

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
                                        mData.add(new MessagesModel(id, image, name));
                                    }
                                    mAdapter.notifyDataSetChanged();
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

    private void loadUsers(UserCallback callback) {
        SharedPreferences preferences = getActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonArrayRequest jsonArrayRequest = null;

        //Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //URL de la API para hacer el login
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/messages/users";

        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    User[] users = new User[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String id = response.getJSONObject(i).getString("id");
                            String name = response.getJSONObject(i).getString("name");
                            String lastname = response.getJSONObject(i).getString("last_name");
                            String email = response.getJSONObject(i).getString("email");
                            String image = response.getJSONObject(i).getString("image");
                            users[i] = new User(id, name, lastname, email, image);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    callback.onSuccess(users);
                    System.out.println("Todo Fue bien");
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
                headers.put("Authorization", "Bearer " + token);
                headers.put("accept", "application/json");
                return headers;
            }
        };

        // Agregar la solicitud a la cola de peticiones
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onItemClick(int position) {
        FragmentManager chatFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction chatFragmentTransaction = chatFragmentManager.beginTransaction();
        Bundle userArguments = new Bundle();
        userArguments.putString("userID", mData.get(position).getUserID());
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(userArguments);
        chatFragmentTransaction.replace(R.id.container, chatFragment);
        chatFragmentTransaction.commit();
    }
}
