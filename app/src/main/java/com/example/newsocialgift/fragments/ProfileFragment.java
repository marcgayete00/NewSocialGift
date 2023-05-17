package com.example.newsocialgift.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsocialgift.GridItem;
import com.example.newsocialgift.R;
import com.example.newsocialgift.adapters.GridAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment  extends Fragment {

    private static final String ARG_ICON = "ARG_ICON";
    private RecyclerView recyclerView;
    private GridAdapter adapter;

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

        return view;
    }
}
