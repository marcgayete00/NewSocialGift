package com.example.socialgiftmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.newsocialgift.R;

import java.nio.channels.Selector;

public class Login extends AppCompatActivity {

    private EditText usename;
    private EditText password;
    private Button login;
    private Button register;
    private Switch darkmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usename = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        darkmode = findViewById(R.id.darkmode);
        register = findViewById(R.id.registerButton);

        //Selector de idioma
        String[] opciones = {"Idioma", "Español", "Ingles"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
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

        //Switch de modo oscuro
        darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Aquí puedes agregar el código para manejar los cambios de estado del switch
            }
        });

        //Boton de login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar el código para manejar el evento de click del botón
            }
        });

        //Boton de registro
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar el código para manejar el evento de click del botón
            }
        });



    }
}