package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.newsocialgift.ChatModel;
import com.example.newsocialgift.Gift;
import com.example.newsocialgift.Message;
import com.example.newsocialgift.MessageLayoutManager;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.example.newsocialgift.Wishlist;
import com.example.newsocialgift.adapters.ChatAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment  extends Fragment {
    private static final String ARG_ICON = "ARG_ICON";
    private ImageView profileImage;
    private TextView profileName;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private List<ChatModel> mData = new ArrayList<>();

    private interface MessagesCallback {
        void onSuccess(Message[] messages);
        void onError(String error);
    }

    public static ChatFragment newInstance(@DrawableRes int iconId) {
        ChatFragment frg = new ChatFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String userJson = preferences.getString("user", "");
        // Convertir la cadena JSON en un objeto User utilizando Gson
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        String userID = user.getId();

        Bundle arguments = getArguments();
        String otherUserID = arguments.getString("userID");

        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        loadUser(otherUserID);
        loadMessages(otherUserID, new MessagesCallback() {
            @Override
            public void onSuccess(Message[] messages) {
                for (Message message : messages) {
                    mData.add(new ChatModel(message.getSender(), message.getMessage()));
                }
                mAdapter = new ChatAdapter(mData, userID);
                mRecyclerView.setLayoutManager(new MessageLayoutManager(getContext(), userID, mRecyclerView));
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });

        /*mData.add(new ChatModel(otherUserID, "Hola"));
        mData.add(new ChatModel(userID, "Como estas?"));
        mData.add(new ChatModel(userID, "Bien y tu?"));
        mData.add(new ChatModel(otherUserID, "Bien y tu?"));
        mData.add(new ChatModel(userID, "Bien"));
        mAdapter = new ChatAdapter(mData, userID);
        mRecyclerView.setLayoutManager(new MessageLayoutManager(getContext(), userID, mRecyclerView));
        mRecyclerView.setAdapter(mAdapter);*/

        return view;
    }

    private void loadUser(String otherUserID) {
        SharedPreferences preferences = getActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");


        JsonObjectRequest jsonObjectRequest = null;

        //Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        System.out.println("Userid "+otherUserID);   // TODO: Això s'haurà de treure. Només és per fer proves
        //URL de la API para hacer el login
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + otherUserID;
        System.out.println("URL "+url); // TODO: Això s'haurà de treure. Només és per fer proves

        //Hacer la petición GET
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        System.out.println("Todo Fue bien");
                        //Obtener el nombre y el apellido del usuario
                        profileName.setText(response.getString("name"));
                        Glide.with(this.profileImage.getContext())
                                .load(response.getString("image"))
                                .circleCrop()
                                .into(profileImage);
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
                headers.put("Authorization", "Bearer " + token);
                headers.put("accept", "application/json");
                return headers;
            }
        };

        // Agregar la solicitud a la cola de peticiones
        requestQueue.add(jsonObjectRequest);
    }

    private void loadMessages(String userID, MessagesCallback callback) {
        SharedPreferences preferences = getActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonArrayRequest jsonArrayRequest = null;

        //Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //URL de la API para hacer el login
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/messages/" + userID;
        System.out.println("URL "+url); // TODO: Això s'haurà de treure. Només és per fer proves

        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Message[] messages = new Message[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String message = response.getJSONObject(i).getString("content");
                            String sender = response.getJSONObject(i).getString("user_id_send");
                            messages[i] = new Message(message, sender);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    callback.onSuccess(messages);
                    System.out.println("Todo Fue bien");
                    System.out.println(response);   // TODO: Això s'haurà de treure. Només és per fer proves
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
}
