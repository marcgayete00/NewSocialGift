package com.example.newsocialgift.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsocialgift.R;
import com.example.newsocialgift.activities.EditProfile;
import com.example.newsocialgift.adapters.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment  extends Fragment {
    private static final String ARG_ICON = "ARG_ICON";
    private EditText search;
    private Button cancel;

    public static SearchFragment newInstance(@DrawableRes int iconId) {
        SearchFragment frg = new SearchFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        search = view.findViewById(R.id.search);
        cancel = view.findViewById(R.id.cancel_button);


        List<String> listaElementos = new ArrayList<>();
        listaElementos.add("Elemento 1");
        listaElementos.add("Elemento 2");
        listaElementos.add("Elemento 3");


        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        SearchAdapter sAdapter = new SearchAdapter(listaElementos);
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                //Esconder teclado
                // Obtiene el servicio de entrada de método
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            }
        });

        return view;
    }

}
