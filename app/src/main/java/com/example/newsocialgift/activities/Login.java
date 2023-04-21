package com.example.newsocialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.Main;
import com.example.newsocialgift.R;
import com.example.newsocialgift.fragments.Utilities;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button register;

    private String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        System.out.println("Hola");
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.registerButton);

        //Fragmento de utilidades (modo oscuro, idioma)
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        //Boton de login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/login";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Obtener el código de respuesta
                                NetworkResponse networkResponse = new NetworkResponse(response.getBytes());
                                //Comparar codigo de respuesta
                                System.out.println(networkResponse.statusCode);
                                if (networkResponse.statusCode == 200) {
                                    Intent intent = new Intent(Login.this, Main.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Manejar el error de la solicitud
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        // Agrega los parámetros de la solicitud POST aquí
                        params.put("email", emailString);
                        params.put("password", passwordString);
                        System.out.println(emailString+ " " + passwordString);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

        //Boton de registro
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

            }
        });



    }
}