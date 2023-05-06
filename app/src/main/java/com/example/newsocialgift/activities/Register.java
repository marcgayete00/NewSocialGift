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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.Main;
import com.example.newsocialgift.R;
import com.example.newsocialgift.fragments.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText email;
    private EditText surname;

    private EditText photo;
    private Button register;
    private Button backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        photo = findViewById(R.id.photo);
        surname = findViewById(R.id.surname);
        register = findViewById(R.id.register);
        backToLogin = findViewById(R.id.backToLogin);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        //Boton de foto de perfil

        //Boton registrarse
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameString = username.getText().toString();
                String lastNameString = surname.getText().toString();
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String photoString = photo.getText().toString();

                if (usernameString.isEmpty() || lastNameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty() || photoString.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                JsonObjectRequest jsonObjectRequest = null;
                // Crear la cola de peticiones
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                // URL de la API para hacer el login
                String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users";

                // Crear el objeto JSON con los datos del usuario
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", usernameString);
                    jsonObject.put("last_name", lastNameString);
                    jsonObject.put("email", emailString);
                    jsonObject.put("password", passwordString);
                    jsonObject.put("image",  photoString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println(usernameString+" "+lastNameString+" "+emailString+" "+passwordString+" "+photoString);
                jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // La petición fue exitosa
                                Toast.makeText(Register.this, "Register successful", Toast.LENGTH_SHORT).show();
                                //Redireccionar a la activity login
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // La petición falló
                                Toast.makeText(Register.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonObjectRequest);
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
