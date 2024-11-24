package com.example.biolap;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
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
    EditText nuevaClave, repi;
    String a, b;
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
        repi=findViewById(R.id.correo_user);
        // Recuperar el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1); // Obtener el ID del usuario almacenado

        if (userId == -1) {
            error.setVisibility(View.VISIBLE);  // Muestra la imagen de error
            System.out.println("No se encontró el usuario");
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();

            // Crear un Handler para ocultar la imagen después de 4 segundos
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    error.setVisibility(View.GONE);  // Oculta la imagen después de 4 segundos
                }
            }, 4000);  // 4000 milisegundos = 4 segundos
        } else {
            // Si se encuentra el ID del usuario

            System.out.println("Usuario encontrado con ID: " + userId);
            Toast.makeText(this, "ID del usuario: " + userId, Toast.LENGTH_SHORT).show();
        }

       cambia.setOnClickListener(new  View.OnClickListener(){
           @Override
           public void onClick(View v){
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

    // Método para cambiar la contraseña
    public void cambiarClave() {
        a = nuevaClave.getText().toString();
        b = repi.getText().toString();
        boolean val = true;

        // Validar que ambos campos no estén vacíos
        if (TextUtils.isEmpty(a)) {
            nuevaClave.setError("Campo obligatorio");
            val = false;
        }
        if (TextUtils.isEmpty(b)) {
            repi.setError("Campo obligatorio");
            val = false;
        }

        // Validar que las claves coincidan
        if (!a.equals(b)) {
            repi.setError("Las contraseñas no coinciden");
            val = false;
        }

        // Si todo es válido, proceder con el cambio
        if (val) {
            cambios("http://192.168.1.11/bio.lap/modificar_clave.php?id=" + userId);
        }
    }


    private void cambios(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Se modificó con éxito", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error en la modificación", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                    sin_conexion.setVisibility(View.VISIBLE);
                    // Crear un Handler para ocultar la imagen después de 4 segundos
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sin_conexion.setVisibility(View.GONE);  // Oculta la imagen después de 4 segundos
                        }
                    }, 4000);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                sin_conexion.setVisibility(View.VISIBLE);
                // Crear un Handler para ocultar la imagen después de 4 segundos
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sin_conexion.setVisibility(View.GONE);  // Oculta la imagen después de 4 segundos
                    }
                }, 4000);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> datos = new HashMap<String, String>();
                datos.put("id", String.valueOf(userId)); // Enviar el ID del usuario
                datos.put("clave", a);  // Nueva clave
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
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
                    cambiarClave();

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

