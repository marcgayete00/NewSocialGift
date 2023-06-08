package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.newsocialgift.MessagesModel;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.example.newsocialgift.adapters.MessagesAdapter;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment  extends Fragment {
    private static final String ARG_ICON = "ARG_ICON";
    private RecyclerView mRecyclerView;
    private MessagesAdapter mAdapter;
    private List<MessagesModel> mData = new ArrayList<>();

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

        Bundle arguments = getArguments();

        String id = null;
        if (arguments != null) {
            id = arguments.getString("id");
        }

        if (id == null) {
            SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
            String userJson = preferences.getString("user", "");
            // Convertir la cadena JSON en un objeto User utilizando Gson
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);
            id = user.getId();
        }

        System.out.println("id: " + id);

        return view;
    }
}
