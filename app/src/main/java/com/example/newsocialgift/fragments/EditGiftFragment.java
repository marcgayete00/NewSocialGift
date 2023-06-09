package com.example.newsocialgift.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.newsocialgift.R;

public class EditGiftFragment extends Fragment {
    private int giftId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Obtener el ID del regalo seleccionado desde los argumentos
        Bundle args = getArguments();
        if (args != null) {
            giftId = args.getInt("giftId", 0);
        }

        // Inflar el diseño del fragmento y configurar los elementos de interfaz de usuario
        View view = inflater.inflate(R.layout.fragment_edit_gift, container, false);
        // Configurar los elementos de interfaz de usuario para editar el regalo
        // ...

        return view;
    }

    // Resto del código del fragmento
}
