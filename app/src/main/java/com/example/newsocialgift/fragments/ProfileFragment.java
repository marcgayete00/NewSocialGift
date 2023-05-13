package com.example.newsocialgift.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.newsocialgift.R;

public class ProfileFragment  extends Fragment {

    private static final String ARG_ICON = "ARG_ICON";

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

        return view;
    }
}
