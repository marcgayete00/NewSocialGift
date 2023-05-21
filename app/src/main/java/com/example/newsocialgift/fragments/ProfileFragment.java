package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.newsocialgift.GridItem;
import com.example.newsocialgift.Main;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.example.newsocialgift.activities.EditProfile;
import com.example.newsocialgift.activities.Login;
import com.example.newsocialgift.adapters.GridAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment  extends Fragment {

    private static final String ARG_ICON = "ARG_ICON";
    private RecyclerView recyclerView;
    private Button editProfile;
    private GridAdapter adapter;
    private TextView userName;
    private TextView userLastName;
    private ImageView profileImage;
    private TextView numberOfWishlists;
    private Button wishlistsButton;
    private TextView numberOfFriends;
    private Button friendsButton;

    public static ProfileFragment newInstance(@DrawableRes int iconId) {
        ProfileFragment frg = new ProfileFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();
        userName = view.findViewById(R.id.username);
        userLastName = view.findViewById(R.id.userlastname);
        profileImage = view.findViewById(R.id.profileImage);
        numberOfWishlists = view.findViewById(R.id.wishlistsNumber);
        wishlistsButton = view.findViewById(R.id.wishlistsButton);
        numberOfFriends = view.findViewById(R.id.friendsNumber);
        friendsButton = view.findViewById(R.id.friendsButton);
        editProfile = view.findViewById(R.id.editProfileButton);

        recyclerView = view.findViewById(R.id.recyclerView);

        List<GridItem> items = new ArrayList<>();
        items.add(new GridItem("Item 1", Color.RED));
        items.add(new GridItem("Item 2", Color.BLUE));
        items.add(new GridItem("Item 3", Color.GREEN));
        items.add(new GridItem("Item 4", Color.RED));
        items.add(new GridItem("Item 5", Color.BLUE));
        items.add(new GridItem("Item 6", Color.GREEN));
        adapter = new GridAdapter(items);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);

        getFriendsNumber();
        getWishlistsNumber();

        //Obtener token del SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String userJson = preferences.getString("user", "");
        // Convertir la cadena JSON en un objeto User utilizando Gson
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);

        userName.setText(user.getUsername());
        userLastName.setText(user.getLastName());
        //Cargar la imagen del usuario en el imageview
        Glide.with(this)
                .load(user.getImage())
                .circleCrop()
                .into(profileImage);

        wishlistsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                WishListFragment wishListFragment = new WishListFragment();
                fragmentTransaction.replace(R.id.container, wishListFragment);
                fragmentTransaction.commit();
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FriendsFragment friendsFragment = new FriendsFragment();
                fragmentTransaction.replace(R.id.container, friendsFragment);
                fragmentTransaction.commit();
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopup();
            }
        });

        return view;
    }

    private void getWishlistsNumber() {
        //Obtener token del SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String userJson = preferences.getString("user", "");
        // Convertir la cadena JSON en un objeto User utilizando Gson
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        String id = user.getId();

        //Crear una nueva cola de solicitudes
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        //Crear una nueva solicitud HTTP GET
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + id + "/wishlists",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        numberOfWishlists.setText(String.valueOf(response.length()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getActivity(), "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            //Agregar el token a los headers de la solicitud
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        //Agregar la solicitud a la cola de solicitudes
        queue.add(request);
    }

    private void getFriendsNumber() {
        //Obtener token del SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String userJson = preferences.getString("user", "");
        // Convertir la cadena JSON en un objeto User utilizando Gson
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        String id = user.getId();

        //Crear una nueva cola de solicitudes
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        //Crear una nueva solicitud HTTP GET
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + id + "/friends",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        numberOfFriends.setText(String.valueOf(response.length()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getActivity(), "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            //Agregar el token a los headers de la solicitud
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        //Agregar la solicitud a la cola de solicitudes
        queue.add(request);
    }

    private void mostrarPopup() {
        // Inflar el diseño del pop-up
        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_popup, null);

        // Obtener el botón submitURL del layout del popup
        Button editProfileOption = popupView.findViewById(R.id.editProfileButton);
        Button disconnectOption = popupView.findViewById(R.id.disconnectButton);
        Button cancelOption = popupView.findViewById(R.id.cancelButton);

        // Crear el pop-up
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(getActivity())
                .setView(popupView);

        // Mostrar el pop-up
        AlertDialog popup = popupBuilder.create();
        popup.show();

        editProfileOption.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfile.class);
            startActivity(intent);
            popup.dismiss();
        });

        disconnectOption.setOnClickListener(v -> {
            SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            popup.dismiss();
        });

        cancelOption.setOnClickListener(v -> {
            popup.dismiss();
        });
    }
}
