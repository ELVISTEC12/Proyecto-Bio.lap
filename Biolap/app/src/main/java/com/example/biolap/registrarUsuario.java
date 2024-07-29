package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class registrarUsuario extends AppCompatActivity {
    EditText nombre, correo, contra, conta1;
    Button b1;
    CheckBox check;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false);

        if (isUserLoggedIn) {
            // Usuario ya está registrado/iniciado sesión, redirigir a menup
            Intent intent = new Intent(this, menup.class);
            startActivity(intent);
            finish();
            return;
        }*/
                EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_usuario);
        nombre = findViewById(R.id.nuser_r);
        correo = findViewById(R.id.correo_r);
        contra = findViewById(R.id.contra1_r);
        conta1 = findViewById(R.id.contra2_r);
        b1 = findViewById(R.id.b_r);
        check = findViewById(R.id.checkBox);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarDatos()) {
                    enviarDatos("http://192.168.1.3/bio.lap/insertar.php");
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validarDatos() {
        String nombreUsuario = nombre.getText().toString().trim();
        String correoUsuario = correo.getText().toString().trim();
        String contrasena = contra.getText().toString().trim();
        String confirmacionContrasena = conta1.getText().toString().trim();

        if (TextUtils.isEmpty(nombreUsuario)) {
            nombre.setError("Campo obligatorio");
            return false;
        }

        if (TextUtils.isEmpty(correoUsuario) || !Patterns.EMAIL_ADDRESS.matcher(correoUsuario).matches()) {
            correo.setError("Correo electrónico inválido");
            return false;
        }

        if (TextUtils.isEmpty(contrasena)) {
            contra.setError("Campo obligatorio");
            return false;
        }

        if (TextUtils.isEmpty(confirmacionContrasena)) {
            conta1.setError("Campo obligatorio");
            return false;
        }

        if (!contrasena.equals(confirmacionContrasena)) {
            conta1.setError("Las contraseñas no coinciden");
            return false;
        }

        if (check != null && !check.isChecked()) {
            Toast.makeText(this, "Debe aceptar los términos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void enviarDatos(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        // Capturar el ID del usuario
                        int userId = jsonResponse.getInt("user_id");
                        Toast.makeText(getApplicationContext(), "Operación exitosa", Toast.LENGTH_SHORT).show();

                        // Guardar el ID del usuario en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("USER_ID", userId);
                        editor.apply();

                        // Navegar a la actividad de menú
                        Intent intent = new Intent(getApplicationContext(), menup.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EnviarDatos", "Error: " + error.toString());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pa = new HashMap<>();
                pa.put("usuarios", nombre.getText().toString());
                pa.put("contra", contra.getText().toString());
                pa.put("correo", correo.getText().toString());
                return pa;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }
}
