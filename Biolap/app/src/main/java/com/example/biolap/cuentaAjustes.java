package com.example.biolap;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.concurrent.Executor;

public class cuentaAjustes extends AppCompatActivity {

    Button cambia;
    ImageView error, sin_conexion;
    EditText nuevaClave;
    int userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cuenta_ajustes);

        cambia = findViewById(R.id.cambiar_datos);
        error = findViewById(R.id.error);
        sin_conexion = findViewById(R.id.sin_conexion);
        nuevaClave = findViewById(R.id.name_user);

        // Recuperar el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1); // Obtener el ID del usuario almacenado

        if (userId == -1) {
            // Si no se encuentra el usuario (valor predeterminado -1)
            System.out.println("No se encontró el usuario");
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        } else {
            // Si se encuentra el ID del usuario
            System.out.println("Usuario encontrado con ID: " + userId);
            Toast.makeText(this, "ID del usuario: " + userId, Toast.LENGTH_SHORT).show();
        }

        cambia.setOnClickListener(view -> {
            String clave = nuevaClave.getText().toString().trim();
            if (clave.isEmpty()) {
                Toast.makeText(this, "La nueva contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
            } else {
                cambiarClave(clave);
            }
        });

        // Ajustar los márgenes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para cambiar la contraseña
    public void cambiarClave(String nuevaClave) {
        // URL de la API para cambiar la contraseña, pasando el userId y la nueva contraseña
        String url = "http://192.168.1.11/bio.lap/modificar_clave.php?id=" + userId;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    if (response.equals("success")) {
                        Toast.makeText(this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {

                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Aquí puedes agregar parámetros adicionales si es necesario
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(userId));
                params.put("clave", nuevaClave);
                return params;
            }
        };

        // Añadir la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(stringRequest);
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
                    // Si la autenticación es exitosa, llama al método cambiar()
                    //cambiarClave();

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
}

