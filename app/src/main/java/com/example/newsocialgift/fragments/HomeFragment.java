package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

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
import com.example.newsocialgift.R;
import com.example.newsocialgift.adapters.GiftAdapter;
import com.example.newsocialgift.adapters.HomeAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements GiftAdapter.GiftListener {
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
        void onSuccess(Product product, int wishlistIndex, int giftIndex, List<GiftItem> giftItems);
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

        loadFriends(new UserCallback() {
            @Override
            public void onSuccess(User[] friends) {
                for (int i = 0; i < friends.length; i++) {
                    loadWishlists(friends[i].getId(), i, new WishlistCallback() {
                        @Override
                        public void onSuccess(Wishlist[] wishlists, int friendIndex) {
                            for (int j = 0; j < wishlists.length; j++) {
                                List<GiftItem> giftItems = new ArrayList<>();
                                for (int n = 0; n < wishlists[j].getGifts().length; n++) {
                                    loadProduct(wishlists[j].getGifts()[n].getProductUrl(), j, n, giftItems, new ProductCallback() {
                                        @Override
                                        public void onSuccess(Product product, int wishlistIndex, int giftIndex, List<GiftItem> giftItems) {
                                            addProductItem(product, giftItems, wishlists[wishlistIndex].getGifts()[giftIndex].isBooked(), wishlists[wishlistIndex].getGifts()[giftIndex].getId());
                                            System.out.println(giftItems.get(0).getGiftName());
                                            if (giftIndex == wishlists[wishlistIndex].getGifts().length - 1) {
                                                mData.add(new HomeModel(friends[friendIndex].getImage(), friends[friendIndex].getUsername(), R.drawable.ic_more, wishlists[wishlistIndex].getWishlistName(), wishlists[wishlistIndex].getWishlistDescription(), giftItems));
                                                mAdapter = new HomeAdapter(mData, HomeFragment.this);
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
                                if (wishlists[j].getGifts().length == 0) {
                                    mData.add(new HomeModel(friends[friendIndex].getImage(), friends[friendIndex].getUsername(), R.drawable.ic_more, wishlists[j].getWishlistName(), wishlists[j].getWishlistDescription(), giftItems));
                                    mAdapter = new HomeAdapter(mData, HomeFragment.this);
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    mRecyclerView.setAdapter(mAdapter);
                                }
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

    @Override
    public void onGiftChecked(boolean isChecked, int position, int homeAdapterPosition) {
        HomeModel homeModel = mAdapter.getItem(homeAdapterPosition);
        List<GiftItem> giftItems = homeModel.getGiftItems();
        GiftItem giftItem = giftItems.get(position);
        String giftId = giftItem.getGiftId();
        System.out.println(giftId); // TODO: Això s'haurà de treure. Només es per fer proves
        if (isChecked) {
            // TODO: Fer funció per fer la petició POST a la api per fer la reserva del regal
        } else {
            // TODO: Fer funció per fer la petició DELETE a la api per desfer la reserva del regal
        }
    }

    private void addProductItem(Product product, List<GiftItem> giftItems, int isBookedNumber, String giftId) {
        boolean isBooked = (isBookedNumber != 0);
        giftItems.add(new GiftItem(product.getProductImage(), product.getProductName(), isBooked, giftId));
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
                            String creationDate = response.getJSONObject(i).getString("creation_date");
                            String endDate = response.getJSONObject(i).getString("end_date");
                            JSONArray giftsJson = response.getJSONObject(i).getJSONArray("gifts");
                            Gift[] gifts = new Gift[giftsJson.length()];
                            for (int n = 0; n < giftsJson.length(); n++) {
                                try {
                                    String giftId = giftsJson.getJSONObject(n).getString("id");
                                    String wishlistId = giftsJson.getJSONObject(n).getString("wishlist_id");
                                    String productURL = giftsJson.getJSONObject(n).getString("product_url");
                                    int priority = giftsJson.getJSONObject(n).getInt("priority");
                                    int booked = giftsJson.getJSONObject(n).getInt("booked");
                                    gifts[n] = new Gift(giftId, wishlistId, productURL, priority, booked);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
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

    private void loadProduct(String productUrl, int wishlistIndex, int giftIndex, List<GiftItem> giftItems, ProductCallback callback) {
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
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String id = response.getString("id");
                            String name = response.getString("name");
                            String description = response.getString("description");
                            String link = response.getString("link");
                            String photo = response.getString("photo");
                            float price = Float.parseFloat(response.getString("price"));
                            Gson gson = new Gson();
                            int isActive = response.getInt("is_active");
                            String[] ids = gson.fromJson(response.getJSONArray("categoryIds").toString(), String[].class);
                            product = new Product(id, name, description, link, photo, price, isActive, ids);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    callback.onSuccess(product, wishlistIndex, giftIndex, giftItems);
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
