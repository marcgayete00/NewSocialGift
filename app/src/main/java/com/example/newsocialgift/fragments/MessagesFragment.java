package com.example.newsocialgift.fragments;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

public class MessagesFragment  extends Fragment {
    private static final String ARG_ICON = "ARG_ICON";


    public static MessagesFragment newInstance(@DrawableRes int iconId) {
        MessagesFragment frg = new MessagesFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }
}
