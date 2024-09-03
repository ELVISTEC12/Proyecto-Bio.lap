package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import org.json.JSONException;
import org.json.JSONObject;


public class cuentaAjustes extends AppCompatActivity {

    TextView name_user, correo, contra;
    Button cerrar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cuenta_ajustes);

        name_user = findViewById(R.id.name_user);
        correo = findViewById(R.id.correo_user);
        contra = findViewById(R.id.Contraseña_user);
        cerrar = findViewById(R.id.cerrar);

        // Obtener el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId != -1) {
            // URL con el ID del usuario
            String url = "http://192.168.1.3/bio.lap/selec_user.php?id=" + userId;
            //fetchUserData(url);
        } else {
            // Manejar el caso de ID no válido
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cerrar(v);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
/*
    private void fetchUserData(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {
                                JSONObject userData = jsonResponse.getJSONObject("user");

                                // Mostrar los datos del usuario en los TextView correspondientes
                                name_user.setText(userData.getString("usuarios"));
                                correo.setText(userData.getString("correo"));
                                contra.setText(userData.getString("contra"));
                            } else {
                                Toast.makeText(getApplicationContext(), "Error: " + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Error de red";
                if (error.networkResponse != null) {
                    errorMessage += " Código de respuesta: " + error.networkResponse.statusCode;
                }
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Añadir la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void cerrar(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Limpiar SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("USER_ID");
                        editor.putBoolean("isUserLoggedIn", false);
                        editor.apply();

                        // Navegar a la actividad de inicio de sesión
                        Intent intent = new Intent(getApplicationContext(), LogIn.class);
                        startActivity(intent);
                        finish(); // Finaliza la actividad actual
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }*/
}
