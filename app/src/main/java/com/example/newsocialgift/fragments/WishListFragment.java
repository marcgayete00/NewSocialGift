package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.GiftItem;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.example.newsocialgift.adapters.WishlistAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class WishListFragment extends Fragment {

    private WishlistAdapter wishlistadapter;
    private TextView tvNombre;
    private TextView WishlistDescription;
    private TextView WishlistCaducidad;

    private Button btnAddGift;

    private Button btnEditWishlist;
    private Button btnDeleteWishlist;
    private static String URL;

    private static final String ARG_ICON = "ARG_ICON";

    public static WishListFragment newInstance(@DrawableRes int iconId) {
        WishListFragment frg = new WishListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);
        return frg;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        Bundle arguments = getArguments();
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String TOKEN = preferences.getString("token", "");
        String userJson = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        String userID = user.getId();
        String wishlistID = null;
        String userWishlist = null;
        if (arguments != null) {
            userWishlist = arguments.getString("userID");
            wishlistID = arguments.getString("wishlistID");
        }
        URL = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/";
        URL = URL.concat(wishlistID);
        System.out.println(URL);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSingle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String finalWishlistID = wishlistID;
        List<GiftItem> giftItemList = new ArrayList<>();
        wishlistadapter = new WishlistAdapter(giftItemList, getContext(),TOKEN);
        recyclerView.setAdapter(wishlistadapter);

        tvNombre = view.findViewById(R.id.tvNombre);
        WishlistDescription = view.findViewById(R.id.WishlistDescription);
        WishlistCaducidad = view.findViewById(R.id.WishlistCaducidad);
        btnAddGift = view.findViewById(R.id.addWishlistButton);
        btnDeleteWishlist = view.findViewById(R.id.btnDeleteList);
        btnEditWishlist = view.findViewById(R.id.btnEditList);
        if(!userID.equals(userWishlist)){
            btnAddGift.setVisibility(View.INVISIBLE);
            btnDeleteWishlist.setVisibility(View.INVISIBLE);
            btnEditWishlist.setVisibility(View.INVISIBLE);
        }
        btnEditWishlist.setOnClickListener(v -> {

            // Crear un Bundle con los datos de la wishlist actual
            Bundle bundle = new Bundle();
            bundle.putString("wishlistID", finalWishlistID);

            // Abrir el fragmento de edición de wishlist y pasarle los datos actuales
            EditWishlistFragment editWishlistFragment = new EditWishlistFragment();
            editWishlistFragment.setArguments(bundle);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, editWishlistFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        btnDeleteWishlist.setOnClickListener(v -> {
            deleteWishlistFromAPI(finalWishlistID);
        });
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        btnAddGift.setOnClickListener(v -> {
            // Abrir el fragmento AddGiftFragment
            FragmentManager GiftFragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentGiftTransaction = GiftFragmentManager.beginTransaction();
            Bundle wishlistArguments = new Bundle();
            wishlistArguments.putString("wishlistID", finalWishlistID);
            AddGiftFragment addGiftFragment = new AddGiftFragment();
            addGiftFragment.setArguments(wishlistArguments);
            fragmentGiftTransaction.replace(R.id.container, addGiftFragment);
            fragmentGiftTransaction.commit();
        });

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        tvNombre.setText(response.getString("name"));
                        WishlistDescription.setText(response.getString("description"));
                        String fechacaducidad = response.getString("end_date");
                        String[] parts = fechacaducidad.split("T");
                        WishlistCaducidad.setText(parts[0]);
                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date date = dateFormat.parse(WishlistCaducidad.getText().toString());
                            if (date.before(currentDate)) {
                                WishlistCaducidad.setTextColor(Color.RED);
                            } else if (date.after(currentDate)) {
                                WishlistCaducidad.setTextColor(Color.GREEN);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        JSONArray giftsArray = response.getJSONArray("gifts");
                        for (int i = 0; i < giftsArray.length(); i++) {
                            JSONObject giftObject = giftsArray.getJSONObject(i);
                            int booked = giftObject.getInt("booked");
                            boolean checked = booked == 1;
                            String giftId = giftObject.getString("id");
                            String productUrl = giftObject.getString("product_url");
                            AtomicReference<String> photoUrl = new AtomicReference<>("");
                            AtomicReference<String> name = new AtomicReference<>("");
                            // Hacer una solicitud GET para obtener los datos del producto
                            JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, productUrl, null,
                                    productResponse -> {
                                        try {
                                            name.set(productResponse.getString("name"));
                                            // Obtener la URL de la imagen del producto
                                            photoUrl.set(productResponse.getString("photo"));
                                            GiftItem giftItem = new GiftItem(photoUrl.get(), String.valueOf(name.get()), checked, giftId);
                                            // Agregar a la lista giftItemList
                                            giftItemList.add(giftItem);
                                            wishlistadapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }, Throwable::printStackTrace) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<>();
                                    headers.put("Authorization", "Bearer " + TOKEN);
                                    return headers;
                                }
                            };
                            mQueue.add(productRequest);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }
        };
        mQueue.add(request);
        return view;
    }
    private void deleteWishlistFromAPI(String wishlistID) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishlistID;
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        String TOKEN = preferences.getString("token", "");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    // La solicitud de eliminación fue exitosa, maneja la respuesta aquí
                    Toast.makeText(getContext(), "Wishlist deleted successfully", Toast.LENGTH_SHORT).show();
                    // Realiza cualquier otra acción necesaria después de eliminar la lista
                    // Por ejemplo, puedes navegar a otro fragmento o realizar una actualización de la interfaz de usuario
                },
                error -> {
                    // Error al realizar la solicitud, maneja el error aquí
                    Toast.makeText(getContext(), "Failed to delete wishlist", Toast.LENGTH_SHORT).show();
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