package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.newsocialgift.HomeModel;
import com.example.newsocialgift.RecyclerViewItemDecoration;
import com.example.newsocialgift.User;
import com.example.newsocialgift.GiftItem;
import com.example.newsocialgift.activities.EditProfile;
import com.example.newsocialgift.R;
import com.example.newsocialgift.adapters.HomeAdapter;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String ARG_ICON = "ARG_ICON";
    private Button myButton;
    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private List<HomeModel> mData = new ArrayList<>();
    private User[] friend;

    public static HomeFragment newInstance(@DrawableRes int iconId) {
        HomeFragment frg = new HomeFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        //Guarda en el sharedPreferences una classe amb les dades de l'usuari que ha iniciat sessió
        createUserClass();

        mRecyclerView = view.findViewById(R.id.recyclerView);

        int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.item_space);
        RecyclerViewItemDecoration itemDecoration = new RecyclerViewItemDecoration(spaceInPixels);
        mRecyclerView.addItemDecoration(itemDecoration);

        loadFriends();
        List<GiftItem> giftItems = loadGifts();
        // TODO: Crear funcions per carregar les dades dels amics i les seves wishlists
        // TODO: Canviar R.drawable.ic_profile per la imatge de perfil dels amics
        // TODO: Canviar Profile Name per el nom dels amics
        // TODO: Canviar Wishlist Name i Wishlist Description per les dades de les wishlists dels amics
        mData.add(new HomeModel(R.drawable.ic_profile, "Profile Name", R.drawable.ic_more, "Wishlist Name", "Wishlist Description", giftItems));
        mData.add(new HomeModel(R.drawable.ic_profile, "Profile Name", R.drawable.ic_more, "Wishlist Name", "Wishlist Description", giftItems));
        mData.add(new HomeModel(R.drawable.ic_profile, "Profile Name", R.drawable.ic_more, "Wishlist Name", "Wishlist Description", giftItems));

        mAdapter = new HomeAdapter(mData);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        myButton = view.findViewById(R.id.myButton);

        myButton.setOnClickListener(v -> {
            // TODO: Canviar per la pantalla per afegir wishlists
            Intent intent = new Intent(getActivity(), EditProfile.class);
            startActivity(intent);
        });

        return view;
    }

    // TODO: Adaptar aquesta funció perquè carregui els regals de la api per cada wishlist
    // TODO: Aquesta funció hauria de rebre un paràmetre per saber de quina wishlist ha de carregar els regals
    private List<GiftItem> loadGifts() {
        List<GiftItem> giftItems = new ArrayList<>();
        // TODO: Canviar R.drawable.ic_profile per la imatge del regal
        // TODO: Canviar Gift Name per el nom del regal
        // TODO: Canviar el camp checked per una variable booleana
        giftItems.add(new GiftItem(R.drawable.ic_profile, "Gift Name", false));
        giftItems.add(new GiftItem(R.drawable.ic_profile, "Gift Name", false));
        giftItems.add(new GiftItem(R.drawable.ic_profile, "Gift Name", false));
        return giftItems;
    }

    // TODO: Aquesta funció ha de carregar les dades dels amics de la api
    private void loadFriends() {
        SharedPreferences preferences = getActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonArrayRequest jsonArrayRequest = null;

        //Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //URL de la API para hacer el login
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends";
        System.out.println("URL "+url);

        // TODO: Fer la petició GET a la api i obtenir les dades de tots els amics. Les dades s'han de guardar en un array de la classe User
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    friend = new User[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String id = response.getJSONObject(i).getString("id");
                            String name = response.getJSONObject(i).getString("name");
                            String lastname = response.getJSONObject(i).getString("last_name");
                            String email = response.getJSONObject(i).getString("email");
                            String image = response.getJSONObject(i).getString("image");
                            friend[i] = new User(id, name, lastname, email, image);
                            System.out.println(friend[i].getId() + "\n" + friend[i].getUsername() + "\n" + friend[i].getLastName() + "\n" + friend[i].getEmail() + "\n" + friend[i].getImage());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Todo Fue bien");
                    System.out.println(response);
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

    private void createUserClass() {
        //Obtener token del shared preferences
        SharedPreferences preferences = getActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        //Hacer un decode del token para obtener el id
        DecodedJWT decodedJWT = JWT.decode(token);
        String payload = decodedJWT.getPayload();

        String decodedPayload = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decodedPayload = new String(Base64.getDecoder().decode(payload));
        }
        String aux[] = decodedPayload.split(",");
        String id = aux[0].split(":")[1];
        //Hacer una peticion get pasandole el id
        JsonObjectRequest jsonObjectRequest = null;

        //Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        System.out.println("Userid "+id);
        //URL de la API para hacer el login
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + id;
        System.out.println("URL "+url);

        //Hacer la petición GET
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        System.out.println("Todo Fue bien");
                        //Obtener el nombre y el apellido del usuario
                        String name = response.getString("name");
                        String lastname = response.getString("last_name");
                        String email = response.getString("email");
                        String image = response.getString("image");
                        //Crear un objeto usuario con los datos obtenidos
                        User user = new User(id,name, lastname, email, image);
                        //Guardar el usuario en el shared preferences
                        Gson gson = new Gson();
                        String userJson = gson.toJson(user);

                        // Guardar la cadena JSON en las preferencias compartidas
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user", userJson);
                        editor.apply();

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
}
