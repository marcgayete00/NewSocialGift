package com.example.newsocialgift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditProfile extends FragmentActivity {


    private TextView username;
    private Button deleteAccount;
    private EditText description;
    private EditText password;
    private Button saveButton;

    private TextView characterCounter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        username = findViewById(R.id.username);
        deleteAccount = findViewById(R.id.deleteAccount);
        description = findViewById(R.id.description);
        characterCounter = findViewById(R.id.characterCounter);
        password = findViewById(R.id.password);
        saveButton = findViewById(R.id.saveButton);

        //Fragmento de utilidades (modo oscuro, idioma)
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        //Contador de caracteres
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se usa en este ejemplo
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Actualizar el contador de caracteres
                int currentLength = s.length();
                characterCounter.setText(currentLength + "/100");
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se usa en este ejemplo
            }
        });

        //Selector de genero
        String[] opciones = {"Gender", "Man", "Women","Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_spinner_dropdown_item, opciones);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_ejemplo);
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Codigo de prueba para probar la actividad EditProfile
                Intent intent = new Intent(EditProfile.this, Login.class);
                startActivity(intent);

            }
        });

    }
}