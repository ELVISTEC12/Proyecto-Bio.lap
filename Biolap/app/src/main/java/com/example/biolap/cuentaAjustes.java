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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import java.util.concurrent.Executor;

public class cuentaAjustes extends AppCompatActivity {

    TextView name_user, correo, contra;
    Button cerrar, cambia;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cuenta_ajustes);

        // Inicializar los TextView y Button
        name_user = findViewById(R.id.name_user);
        correo = findViewById(R.id.correo_user);
        contra = findViewById(R.id.Contraseña_user);
        cerrar = findViewById(R.id.cerrar);
        cambia = findViewById(R.id.cambiar_datos);

        // Obtener el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId != -1) {
            // URL con el ID del usuario
            String url = "http://192.168.193.224/bio.lap/selec_user.php?id=" + userId;
            fetchUserData(url);  // Llamar a fetchUserData con la URL correcta
        } else {
            // Manejar el caso de ID no válido
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }

        // Evento al hacer clic en el botón cerrar sesión
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar(v); // Llama al método para cerrar sesión
            }
        });

        // Verificación biométrica antes de permitir cambios
        cambia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarAutenticacion();
            }
        });

        // Ajustar los márgenes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para obtener los datos del usuario
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

    // Método para cerrar sesión
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
    }


    private void verificarAutenticacion() {
        BiometricManager biometricManager = BiometricManager.from(this);

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                == BiometricManager.BIOMETRIC_SUCCESS) {
            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(cuentaAjustes.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(getApplicationContext(), "Autenticación fallida: " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    cambiar();  // Si la autenticación es exitosa, llama al método cambiar()
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Fallo al autenticar", Toast.LENGTH_SHORT).show();
                }
            });

            // Crear el diálogo de autenticación biométrica
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Autenticación requerida")
                    .setSubtitle("Verifica tu identidad para cambiar los datos")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .build();

            biometricPrompt.authenticate(promptInfo);

        } else {
            Toast.makeText(this, "No se puede usar autenticación biométrica en este dispositivo", Toast.LENGTH_SHORT).show();
        }
    }

    // Método cambiar() que actualiza los datos
    private void cambiar() {
        // Obtener el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL del archivo PHP que se encargará de actualizar los datos
        String url = "http://192.168.1.5/bio.lap/acc.php";

        // Obtener los datos que el usuario desea cambiar
        String user = name_user.getText().toString();
        String corr = correo.getText().toString();
        String con = contra.getText().toString();

        // Crear la solicitud POST con Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                Toast.makeText(getApplicationContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("usuarios", user);
                params.put("correo", corr);
                params.put("contra", con);
                params.put("id", String.valueOf(userId)); // Pasar el ID del usuario
                return params;
            }
        };

        // Añadir la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void mostra(View view){

    }
}
