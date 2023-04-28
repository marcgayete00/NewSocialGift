package com.example.newsocialgift.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.Main;
import com.example.newsocialgift.R;
import com.example.newsocialgift.fragments.HomeFragment;
import com.example.newsocialgift.fragments.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button register;

    private String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1";

    JsonObjectRequest jsonObjectRequest;

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

                // Comprobar que los campos no están vacíos
                if (emailString.isEmpty() || passwordString.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Inicializar el objeto JsonObjectRequest
                JsonObjectRequest jsonObjectRequest = null;

                // Crear la cola de peticiones
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                // URL de la API para hacer el login
                String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/login";

                // Crear el objeto JSON con los datos del usuario
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", emailString);
                    jsonObject.put("password", passwordString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Crear la petición POST con Volley
                jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // La petición fue exitosa
                                try {
                                    // Obtener el token de la respuesta
                                    String token = response.getString("accessToken");
                                    Toast.makeText(Login.this, "JSON web token: " + token, Toast.LENGTH_SHORT).show();
                                    //Redireccionar a la activity main
                                    Intent intent = new Intent(Login.this, Main.class);
                                    startActivity(intent);

                                    startActivity(intent);

                                    // TODO: Guardar el token y navegar a la actividad principal
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // La petición falló
                                Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                // Añadir la petición a la cola
                requestQueue.add(jsonObjectRequest);
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

