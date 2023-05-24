package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.newsocialgift.Gift;
import com.example.newsocialgift.HomeModel;
import com.example.newsocialgift.Product;
import com.example.newsocialgift.RecyclerViewItemDecoration;
import com.example.newsocialgift.User;
import com.example.newsocialgift.GiftItem;
import com.example.newsocialgift.Wishlist;
import com.example.newsocialgift.activities.EditProfile;
import com.example.newsocialgift.R;
import com.example.newsocialgift.adapters.HomeAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String ARG_ICON = "ARG_ICON";
    private Button addWishlistButton;
    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private List<HomeModel> mData = new ArrayList<>();

    private interface UserCallback {
        void onSuccess(User[] friends);
        void onError(String error);
    }

    private interface WishlistCallback {
        void onSuccess(Wishlist[] wishlists, int friendIndex);
        void onError(String error);
    }

    private interface ProductCallback {
        void onSuccess(Product product, int wishlistIndex);
        void onError(String error);
    }

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

        // TODO: Crear funcions per carregar les dades dels amics, les seves wishlists i els regals de cada una
        // TODO: Les tres funcions a fer han d'esperar a que la seva petició GET acabi abans de continuar amb el codi
        //loadFriends();
        loadFriends(new UserCallback() {
            @Override
            public void onSuccess(User[] friends) {
                for (int i = 0; i < friends.length; i++) {
                    loadWishlists(friends[i].getId(), i, new WishlistCallback() {
                        @Override
                        public void onSuccess(Wishlist[] wishlists, int friendIndex) {
                            for (int j = 0; j < wishlists.length; j++) {
                                for (int n = 0; n < wishlists[j].getGifts().length; n++) {
                                    // FIXME: Hi ha un error amb l'inici de la petició GET. El que passa es que el product URL és NULL
                                    loadProduct(wishlists[j].getGifts()[n].getProductUrl(), j, new ProductCallback() {
                                        @Override
                                        public void onSuccess(Product product, int wishlistIndex) {
                                            //
                                        }

                                        @Override
                                        public void onError(String error) {
                                            // Maneja el error aquí
                                            Log.d("Error", error);
                                        }
                                    });
                                }

                                List<GiftItem> giftItems = loadGifts();
                                mData.add(new HomeModel(friends[friendIndex].getImage(), friends[friendIndex].getUsername(), R.drawable.ic_more, wishlists[j].getWishlistName(), wishlists[j].getWishlistDescription(), giftItems));
                                mAdapter = new HomeAdapter(mData);

                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            // Maneja el error aquí
                            Log.d("Error", error);
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                // Maneja el error aquí
                Log.d("Error", error);
            }
        });

        /*mData.add(new HomeModel(R.drawable.ic_profile, "Profile Name", R.drawable.ic_more, "Wishlist Name", "Wishlist Description", giftItems));
        mData.add(new HomeModel(R.drawable.ic_profile, "Profile Name", R.drawable.ic_more, "Wishlist Name", "Wishlist Description", giftItems));
        mData.add(new HomeModel(R.drawable.ic_profile, "Profile Name", R.drawable.ic_more, "Wishlist Name", "Wishlist Description", giftItems));*/

        /*mAdapter = new HomeAdapter(mData);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);*/

        addWishlistButton = view.findViewById(R.id.addWishlistButton);

        addWishlistButton.setOnClickListener(v -> {
            FragmentManager addWishlistFragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction addWishlistFragmentTransaction = addWishlistFragmentManager.beginTransaction();
            AddWishListFragment addWishListFragment = new AddWishListFragment();
            addWishlistFragmentTransaction.replace(R.id.container, addWishListFragment);
            addWishlistFragmentTransaction.commit();
        });

        return view;
    }

    // TODO: Adaptar aquesta funció perquè carregui els regals de la api per cada wishlist
    //  IDEA: Aquesta funció possiblement es canvii després i mostri totes les dades de cop. Es faria la crida
    //   dins de onSuccess de la funció loadProduct. Se li hauria de passar la informació a mostrar
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

    private void loadFriends(UserCallback callback) {
        SharedPreferences preferences = getActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonArrayRequest jsonArrayRequest = null;

        //Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //URL de la API para hacer el login
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends";
        System.out.println("URL "+url);

        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    User[] friends = new User[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String id = response.getJSONObject(i).getString("id");
                            String name = response.getJSONObject(i).getString("name");
                            String lastname = response.getJSONObject(i).getString("last_name");
                            String email = response.getJSONObject(i).getString("email");
                            String image = response.getJSONObject(i).getString("image");
                            friends[i] = new User(id, name, lastname, email, image);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    callback.onSuccess(friends);
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

    private void loadWishlists(String friendId, int friendIndex, WishlistCallback callback) {
        SharedPreferences preferences = getActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonArrayRequest jsonArrayRequest = null;

        //Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //URL de la API para hacer el login
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + friendId + "/wishlists";
        System.out.println("URL "+url);

        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Wishlist[] wishlists = new Wishlist[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String id = response.getJSONObject(i).getString("id");
                            String name = response.getJSONObject(i).getString("name");
                            String description = response.getJSONObject(i).getString("description");
                            int userId = response.getJSONObject(i).getInt("user_id");
                            // TODO: El product_url és NULL. S'ha de veure perquè
                            //  NOTE: Sembla con si la funció fromJson no estigués funcionant
                            Gson gson = new Gson();
                            String creationDate = response.getJSONObject(i).getString("creation_date");
                            String endDate = response.getJSONObject(i).getString("end_date");
                            Gift[] gifts = gson.fromJson(response.getJSONObject(i).getJSONArray("gifts").toString(), Gift[].class);
                            wishlists[i] = new Wishlist(id, name, description, userId, creationDate, endDate, gifts);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    callback.onSuccess(wishlists, friendIndex);
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

    private void loadProduct(String productUrl, int wishlistIndex, ProductCallback callback) {
        SharedPreferences preferences = getActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        JsonObjectRequest jsonObjectRequest = null;

        //Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //URL de la API para hacer el login
        System.out.println("URL " + productUrl);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, productUrl, null,
                response -> {
                    Product product = null;
                    //Product product = new Wishlist[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String id = response.getString("id");
                            String name = response.getString("name");
                            String description = response.getString("description");
                            String link = response.getString("link");
                            String photo = response.getString("photo");
                            float price = Float.parseFloat(response.getString("price"));
                            //Pasar un array de la clase Gift de Json a Java con Gson
                            Gson gson = new Gson();
                            String[] ids = gson.fromJson(response.getJSONArray("ids").toString(), String[].class);
                            product = new Product(id, name, description, link, photo, price, ids);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    callback.onSuccess(product, wishlistIndex);
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
        requestQueue.add(jsonObjectRequest);
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
