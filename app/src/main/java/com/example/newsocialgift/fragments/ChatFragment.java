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

import com.example.newsocialgift.ChatModel;
import com.example.newsocialgift.MessageLayoutManager;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.example.newsocialgift.adapters.ChatAdapter;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment  extends Fragment {
    private static final String ARG_ICON = "ARG_ICON";
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private List<ChatModel> mData = new ArrayList<>();

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
        String id = arguments.getString("userID");

        System.out.println("id: " + id);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mData.add(new ChatModel(id, "Hola"));
        mData.add(new ChatModel(userID, "Como estas?"));
        mData.add(new ChatModel(userID, "Bien y tu?"));
        mData.add(new ChatModel(id, "Bien y tu?"));
        mData.add(new ChatModel(userID, "Bien"));
        mAdapter = new ChatAdapter(mData, userID);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setLayoutManager(new MessageLayoutManager(getContext(), userID, mRecyclerView));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
