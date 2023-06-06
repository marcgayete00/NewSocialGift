package com.example.newsocialgift.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class WishListFragment extends Fragment {

    private WishlistAdapter wishlistadapter;
    private TextView tvNombre;
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
        // Create and set the adapter
        wishlistadapter = new WishlistAdapter(giftItemList);
        recyclerView.setAdapter(wishlistadapter);

        tvNombre = view.findViewById(R.id.tvNombre);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        RequestQueue mQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        // Get the gift data from the JSON response
                        tvNombre.setText(response.getString("name"));
                        System.out.println(response.getString("name"));
                        JSONArray giftsArray = response.getJSONArray("gifts");
                        System.out.println(giftsArray.length());
                        System.out.println(giftsArray);
                        for (int i = 0; i < giftsArray.length(); i++) {
                            JSONObject giftObject = giftsArray.getJSONObject(i);
                            int booked = giftObject.getInt("booked");
                            boolean checked = booked == 1;
                            String giftId = giftObject.getString("id");
                            System.out.println(giftId);
                            String productUrl = giftObject.getString("product_url");
                            System.out.println(productUrl);
                            AtomicReference<String> photoUrl = new AtomicReference<>("");
                            AtomicReference<String> name = new AtomicReference<>("");
                            // Hacer una solicitud GET para obtener los datos del producto
                            JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, productUrl, null,
                                    productResponse -> {
                                        try {
                                            name.set(productResponse.getString("name"));
                                            System.out.println(name.get());
                                            // Obtener la URL de la imagen del producto
                                            photoUrl.set(productResponse.getString("photo"));
                                            System.out.println(photoUrl.get());

                                            System.out.println(booked);
                                            System.out.println("name: " + name.get());
                                            // Create a GiftItem instance with the obtained data
                                            GiftItem giftItem = new GiftItem(photoUrl.get(), String.valueOf(name.get()), checked, giftId);
                                            // Agregar a la lista giftItemList
                                            giftItemList.add(giftItem);

                                            // Notificar al adaptador que los datos han cambiado
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
                            // Agregar la solicitud del producto a la cola de solicitudes
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