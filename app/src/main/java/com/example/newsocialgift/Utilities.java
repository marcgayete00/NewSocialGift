package com.example.newsocialgift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

public class Utilities extends Fragment {

    private Spinner languageSpinner;
    private Switch darkModeSwitch;
    private Switch darkmode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_utilities, container, false);

        String[] opciones = {"Idioma", "Español", "Ingles"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, opciones);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_ejemplo);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = parent.getItemAtPosition(position).toString();
                // Aquí puedes agregar el código que se ejecutará cuando el usuario seleccione una opción
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Aquí puedes agregar el código que se ejecutará cuando el usuario no seleccione ninguna opción
            }
        });

        //Switch de modo oscuro
        darkmode = view.findViewById(R.id.darkmode);
        darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Aquí puedes agregar el código para manejar los cambios de estado del switch
            }
        });

        return view;
    }
}

