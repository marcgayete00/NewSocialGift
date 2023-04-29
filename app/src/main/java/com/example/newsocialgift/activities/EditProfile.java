package com.example.newsocialgift.activities;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.newsocialgift.R;
import com.example.newsocialgift.fragments.Utilities;

public class EditProfile extends FragmentActivity {


    private Button deleteAccount;

    private TextView usernameTextView;
    private EditText usernameInputText;
    private EditText lastNameInputText;
    private EditText emailInputText;
    private EditText password;
    private EditText password2;
    private Button saveButton;
    private Button changePhoto;

    private Button submitURL;

    private void mostrarPopup() {
        // Inflar el diseño del pop-up
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup, null);

        // Obtener el botón submitURL del layout del popup
        Button submitURLPopup = popupView.findViewById(R.id.submitURL);

        // Crear el pop-up
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this)
                .setView(popupView)
                .setTitle("Ingrese su texto:");

        // Mostrar el pop-up
        AlertDialog popup = popupBuilder.create();
        popup.show();

        submitURLPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        deleteAccount = findViewById(R.id.deleteAccount);
        usernameInputText = findViewById(R.id.usernameImput);
        lastNameInputText = findViewById(R.id.lastnameInput);
        emailInputText = findViewById(R.id.emailImput);
        usernameTextView = findViewById(R.id.username);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        saveButton = findViewById(R.id.saveButton);
        changePhoto = findViewById(R.id.changePhoto);

        //Fragmento de utilidades (modo oscuro, idioma)
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Codigo de prueba para probar la actividad EditProfile
                Intent intent = new Intent(EditProfile.this, Login.class);
                startActivity(intent);

            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopup();
            }
        });





    }
}