package com.example.newsocialgift.activities;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.newsocialgift.Main;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.example.newsocialgift.fragments.Utilities;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends FragmentActivity {


    private Button deleteAccount;

    private ImageView profileImage;
    private TextView usernameTextView;
    private EditText usernameInputText;
    private EditText lastNameInputText;
    private EditText emailInputText;
    private EditText password;
    private EditText password2;
    private Button saveButton;
    private Button changePhoto;

    private Button submitURL;
    private String newURL;

    private void mostrarPopup() {
        // Inflar el diseño del pop-up
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup, null);

        // Obtener el botón submitURL del layout del popup
        Button submitURLPopup = popupView.findViewById(R.id.submitURL);

        // Crear el pop-up
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this)
                .setView(popupView)
                .setTitle("Introduce image URL:");

        // Mostrar el pop-up
        AlertDialog popup = popupBuilder.create();
        popup.show();
        popupView.findViewById(R.id.edit_text);




        submitURLPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newURL = ((EditText) popupView.findViewById(R.id.edit_text)).getText().toString();
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
        profileImage = findViewById(R.id.profileImage);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        saveButton = findViewById(R.id.saveButton);
        changePhoto = findViewById(R.id.changePhoto);
        WebView webView = findViewById(R.id.webView);

        //Fragmento de utilidades (modo oscuro, idioma)
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();

        //Obtener user del shared preferences
        // Obtener la referencia al SharedPreferences
        SharedPreferences preferences = getSharedPreferences("SocialGift", MODE_PRIVATE);

        String userJson = preferences.getString("user", "");

        // Convertir la cadena JSON en un objeto User utilizando Gson
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);

        usernameTextView.setText(user.getUsername());
        usernameInputText.setHint(user.getUsername());
        lastNameInputText.setHint(user.getLastName());
        emailInputText.setHint(user.getEmail());
        //Cargar la imagen del usuario en el imageview
        Glide.with(this)
                .load(user.getImage())
                .circleCrop()
                .into(profileImage);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtener el token del shared preferences
                SharedPreferences preferences = getSharedPreferences("SocialGift", MODE_PRIVATE);
                String token = preferences.getString("token", "");

                //Codigo de prueba para probar la actividad EditProfile
                JsonObjectRequest jsonObjectRequest = null;

                // Crear la cola de peticiones
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                // URL de la API para hacer el login
                String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users";

                // Crear el objeto JSON con los datos del usuario
                JSONObject jsonObject = new JSONObject();
                try {
                    SharedPreferences p = getSharedPreferences("SocialGift", MODE_PRIVATE);
                    if (password.getText().toString().equals(p.getString("password", ""))) {

                        //Obtain name
                        if (usernameInputText.getText().toString().equals("")) {
                            jsonObject.put("name", user.getUsername());
                        } else {
                            jsonObject.put("name", usernameInputText.getText().toString());
                        }
                        //obtain last name
                        if (lastNameInputText.getText().toString().equals("")) {
                            jsonObject.put("last_name", user.getLastName());
                        } else {
                            jsonObject.put("last_name", lastNameInputText.getText().toString());
                        }

                        //obtain email
                        if (emailInputText.getText().toString().equals("")) {
                            jsonObject.put("email", user.getEmail());
                        } else {
                            jsonObject.put("email", emailInputText.getText().toString());
                        }

                        //obtain password
                        if (password2.getText().toString().equals("")) {

                            String password = p.getString("password", "");
                            jsonObject.put("password", password);
                        } else {
                            jsonObject.put("password", password2.getText().toString());
                        }

                        //obtain image
                        if (newURL == null) {
                            jsonObject.put("image", user.getImage());
                        } else {
                            jsonObject.put("image", newURL);
                        }
                    } else {
                        Toast.makeText(EditProfile.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // La petición fue exitosa
                                // Obtener el token de la respuesta
                                user.setUsername(usernameInputText.getText().toString());
                                user.setLastName(lastNameInputText.getText().toString());
                                user.setEmail(emailInputText.getText().toString());
                                user.setImage(newURL);

                                //Editar el shared preferences en password
                                SharedPreferences preferences = getSharedPreferences("SocialGift", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("password", password2.getText().toString());

                                Toast.makeText(EditProfile.this, "Data updated", Toast.LENGTH_SHORT).show();
                                //Redireccionar a la activity main
                                //Recargar la pagina sin hacer intent



                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // La petición falló
                                Toast.makeText(EditProfile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }
                };

                // Añadir la petición a la cola
                requestQueue.add(jsonObjectRequest);
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