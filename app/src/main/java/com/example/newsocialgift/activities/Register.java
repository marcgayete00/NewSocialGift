package com.example.newsocialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.newsocialgift.R;
import com.example.newsocialgift.fragments.Utilities;

public class Register extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText email;
    private EditText surname;

    private Button photo;
    private Button register;
    private Button backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        surname = findViewById(R.id.surname);
        photo = findViewById(R.id.photo);
        register = findViewById(R.id.register);
        backToLogin = findViewById(R.id.backToLogin);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        //Boton de foto de perfil
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar el código para manejar el evento de click del botón
            }
        });


        //Boton registrarse
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar el código para manejar el evento de click del botón
            }
        });

        //Boton volver Login
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }
}