package com.example.newsocialgift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        email = findViewById(R.id.username);
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
                //Codigo de prueba para probar la actividad EditProfile

                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url + "/users").openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes("email=emailString&password=passwordString");
                    wr.flush();
                    wr.close();

                    int responseCode = connection.getResponseCode();
                    System.out.println("Response Code : " + responseCode);

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    reader.close();
                    String jsonResponse = response.toString();
                    System.out.println("Response Body : " + jsonResponse);

                    // Obtener el token del cuerpo de la respuesta
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String token = jsonObject.getString("accessToken");
                    System.out.println("Token : " + token);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Intent intent = new Intent(Login.this, Main.class);
                //startActivity(intent);
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