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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.newsocialgift.HomeModel;
import com.example.newsocialgift.User;
import com.example.newsocialgift.activities.EditProfile;
import com.example.newsocialgift.R;
import com.example.newsocialgift.adapters.HomeAdapter;
import com.google.gson.Gson;

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



    public static HomeFragment newInstance(@DrawableRes int iconId) {
        HomeFragment frg = new HomeFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        mRecyclerView = view.findViewById(R.id.recyclerView);

        // Agrega algunos datos al adaptador
        mData.add(new HomeModel(R.drawable.ic_profile, "Texto 1", R.drawable.ic_more, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus aliquet eleifend enim ut dictum. Ut rutrum, libero non tempor mattis, purus erat varius metus, id imperdiet tortor nisi sit amet tortor. In tempus massa ac nunc sagittis, vel sagittis odio auctor. Nulla ullamcorper placerat finibus. Donec ut libero sit amet magna rutrum maximus ac vel mauris. Donec aliquet eros a commodo ultrices. Aenean vel hendrerit dolor, et ornare lorem. Nunc ullamcorper nisl orci, ut convallis felis finibus id. Maecenas id libero in urna rutrum egestas. Praesent et nisi a sem ornare rutrum. Nam sed quam eget massa ultricies mollis."));
        mData.add(new HomeModel(R.drawable.ic_profile, "Texto 2", R.drawable.ic_more, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus aliquet eleifend enim ut dictum. Ut rutrum, libero non tempor mattis, purus erat varius metus, id imperdiet tortor nisi sit amet tortor. In tempus massa ac nunc sagittis, vel sagittis odio auctor. Nulla ullamcorper placerat finibus. Donec ut libero sit amet magna rutrum maximus ac vel mauris. Donec aliquet eros a commodo ultrices. Aenean vel hendrerit dolor, et ornare lorem. Nunc ullamcorper nisl orci, ut convallis felis finibus id. Maecenas id libero in urna rutrum egestas. Praesent et nisi a sem ornare rutrum. Nam sed quam eget massa ultricies mollis."));
        mData.add(new HomeModel(R.drawable.ic_profile, "Texto 3", R.drawable.ic_more, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus aliquet eleifend enim ut dictum. Ut rutrum, libero non tempor mattis, purus erat varius metus, id imperdiet tortor nisi sit amet tortor. In tempus massa ac nunc sagittis, vel sagittis odio auctor. Nulla ullamcorper placerat finibus. Donec ut libero sit amet magna rutrum maximus ac vel mauris. Donec aliquet eros a commodo ultrices. Aenean vel hendrerit dolor, et ornare lorem. Nunc ullamcorper nisl orci, ut convallis felis finibus id. Maecenas id libero in urna rutrum egestas. Praesent et nisi a sem ornare rutrum. Nam sed quam eget massa ultricies mollis."));

        mAdapter = new HomeAdapter(mData);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        myButton = view.findViewById(R.id.myButton);

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

        // Crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        System.out.println("Userid "+id);
        // URL de la API para hacer el login
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + id;
        System.out.println("URL "+url);
        // Hacer la petición GET
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


        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        return view;
    }


}
