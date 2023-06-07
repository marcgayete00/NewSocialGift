package com.example.newsocialgift.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.GiftItem;
import com.example.newsocialgift.R;
import com.example.newsocialgift.adapters.WishlistAdapter;

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
    private static String URL;
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTI1LCJlbWFpbCI6ImFkbWluNkBnbWFpbC5jb20iLCJpYXQiOjE2ODI1MjQ0NDd9.a-RQGEZwgvYJJfbI0yYcIV_0pESm1fTcvwlwjljCJjU";
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

        String wishlistID = null;
        if (arguments != null) {
            wishlistID = arguments.getString("wishlistID");
        }
        URL = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/";
        URL = URL.concat(wishlistID);
        System.out.println(URL);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSingle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<GiftItem> giftItemList = new ArrayList<>();
        wishlistadapter = new WishlistAdapter(giftItemList);
        recyclerView.setAdapter(wishlistadapter);

        tvNombre = view.findViewById(R.id.tvNombre);
        WishlistDescription = view.findViewById(R.id.WishlistDescription);
        WishlistCaducidad = view.findViewById(R.id.WishlistCaducidad);
        btnAddGift = view.findViewById(R.id.addWishlistButton);
        String finalWishlistID = wishlistID;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        btnAddGift.setOnClickListener(v -> {
            // Abrir el fragmento AddGiftFragment
            FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            Bundle wishlistArguments = new Bundle();
            wishlistArguments.putString("wishlistID", finalWishlistID);
            AddGiftFragment addGiftFragment = new AddGiftFragment();
            addGiftFragment.setArguments(wishlistArguments);
            fragmentTransaction2.replace(R.id.fragment_container, addGiftFragment);
            fragmentTransaction2.commit();
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
}