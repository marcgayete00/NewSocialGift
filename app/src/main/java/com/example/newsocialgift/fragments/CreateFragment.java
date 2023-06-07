package com.example.newsocialgift.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateFragment  extends Fragment {

    private static final String ARG_ICON = "ARG_ICON";
    public static CreateFragment newInstance(@DrawableRes int iconId) {
        CreateFragment frg = new CreateFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ICON, iconId);
        frg.setArguments(args);

        return frg;
    }



    private EditText etName, etDescription, etEndDate;
    private Button btnCreate;

    private Calendar calendar;
    private SimpleDateFormat apiDateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_wish_list, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();
        etName = view.findViewById(R.id.etName);
        etDescription = view.findViewById(R.id.etDescription);
        etEndDate = view.findViewById(R.id.etEndDate);
        btnCreate = view.findViewById(R.id.btnCreate);

        // Obtener la instancia del calendario
        calendar = Calendar.getInstance();

        // Formato de fecha utilizado por la API
        apiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

        // Asignar el OnClickListener al campo de texto de la fecha
        etEndDate.setOnClickListener(v -> showDatePickerDialog());

        btnCreate.setOnClickListener(v -> createWishlist());

        return view;
    }

    private void showDatePickerDialog() {
        // Obtener la fecha actual
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog y mostrarlo
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, yearSelected, monthOfYear, dayOfMonthSelected) -> {
            // Crear una instancia de Calendar con la fecha seleccionada
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(yearSelected, monthOfYear, dayOfMonthSelected);

            // Obtener la fecha seleccionada como una cadena en el formato de la API
            String selectedDate = apiDateFormat.format(selectedCalendar.getTime());

            // Actualizar el campo de texto con la fecha seleccionada por el usuario
            etEndDate.setText(selectedDate);
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void createWishlist() {
        String name = etName.getText().toString();
        String description = etDescription.getText().toString();
        String endDate = etEndDate.getText().toString();

        // Validar los campos de entrada (puedes agregar tus propias validaciones)

        // Crear el objeto JSON con los datos de la wishlist
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("name", name);
            requestData.put("description", description);
            requestData.put("end_date", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Hacer la solicitud POST a la API
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                response -> {
                    // La solicitud fue exitosa, maneja la respuesta aquí
                    Toast.makeText(requireContext(), "Wishlist created successfully", Toast.LENGTH_SHORT).show();
                    // Realizar cualquier otra acción necesaria después de crear la wishlist
                },
                error -> {
                    // Error al realizar la solicitud, maneja el error aquí
                    Toast.makeText(requireContext(), "Failed to create wishlist", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIxLCJlbWFpbCI6ImFkbWluc0BnbWFpbC5jb20iLCJpYXQiOjE2ODYxNTczOTR9.Sf_cx3sYWF2rN5_pTgy9uf6MlRMuAPpJMLv8ULsUUcA");
                return headers;
            }
        };

        RequestQueue mQueue = Volley.newRequestQueue(requireContext());
        mQueue.add(request);
    }
}
