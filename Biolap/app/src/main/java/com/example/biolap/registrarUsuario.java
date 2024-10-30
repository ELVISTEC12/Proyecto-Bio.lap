package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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

    boolean verificaded = true;
    ProgressBar cr;
    ImageView no, sinconex;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_usuario);
        nombre = findViewById(R.id.nuser_r);
        correo = findViewById(R.id.correo_r);
        contra = findViewById(R.id.contra1_r);
        conta1 = findViewById(R.id.contra2_r);
        b1 = findViewById(R.id.b_r);

        no=findViewById(R.id.no_carga);
        cr=findViewById(R.id.carga_registro);
        sinconex=findViewById(R.id.sin_conexion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void registrar(View view) {
        // Inicializamos verificaded como verdadero
        boolean verificaded = true;

        // Obtén los valores de los campos
        String nombreUsuario = nombre.getText().toString().trim();
        String correoUsuario = correo.getText().toString().trim();
        String contrasena = contra.getText().toString().trim();
        String confirmacionContrasena = conta1.getText().toString().trim();

        // Validaciones de los campos
        if (TextUtils.isEmpty(nombreUsuario)) {
            nombre.setError("Campo obligatorio");
            verificaded = false;
        }
        if (TextUtils.isEmpty(correoUsuario) || !Patterns.EMAIL_ADDRESS.matcher(correoUsuario).matches()) {
            correo.setError("Correo electrónico inválido");
            verificaded = false;
        }
        if (TextUtils.isEmpty(contrasena)) {
            contra.setError("Campo obligatorio");
            verificaded = false;
        }
        if (TextUtils.isEmpty(confirmacionContrasena)) {
            conta1.setError("Campo obligatorio");
            verificaded = false;
        }
        if (!contrasena.equals(confirmacionContrasena)) {
            conta1.setError("Las contraseñas no coinciden");
            verificaded = false;
        }


        // Solo procedemos si verificaded es verdadero
        if (verificaded) {
            cr.setVisibility(View.VISIBLE);
            String URL = "http://192.168.1.2/bio.lap/insertar.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            // Registro exitoso, navega a la siguiente pantalla
                            cr.setVisibility(View.GONE);
                            Intent mp = new Intent(getApplicationContext(), menuPrincipal.class);
                            startActivity(mp);
                        } else {
                            cr.setVisibility(View.GONE);
                            nombre.setText("");
                            correo.setText("");
                            contra.setText("");
                            conta1.setText("");
                            // Muestra mensaje de error
                            Toast.makeText(getApplicationContext(), "Error al registrarse", Toast.LENGTH_SHORT).show();
                            no.setVisibility(View.VISIBLE);
                           // no.setVisibility(View.VISIBLE);
                            new android.os.Handler().postDelayed(() -> {
                                no.setVisibility(View.GONE);
                            }, 3000);


                            String message = jsonResponse.getString("message");
                            Log.e("RegisterError", message);


                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Desbloquear la pantalla y recargar actividad
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    recreate();
                                }
                            }, 3000);
                        }
                    } catch (JSONException e) {
                        cr.setVisibility(View.GONE);
                        nombre.setText("");
                        correo.setText("");
                        contra.setText("");
                        conta1.setText("");
                        sinconex.setVisibility(View.VISIBLE);
                        new android.os.Handler().postDelayed(() -> {
                            sinconex.setVisibility(View.GONE);
                        }, 3000);
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cr.setVisibility(View.GONE);
                    nombre.setText("");
                    correo.setText("");
                    contra.setText("");
                    conta1.setText("");
                    sinconex.setVisibility(View.VISIBLE);
                    new android.os.Handler().postDelayed(() -> {
                        sinconex.setVisibility(View.GONE);
                    }, 3000);
                    String errorMessage = "Error: " + error.getMessage();
                    if (error.networkResponse != null) {
                        errorMessage += " | Código de estado: " + error.networkResponse.statusCode;
                    }

                    Toast.makeText(registrarUsuario.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("VolleyError", errorMessage);
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("nombre", nombreUsuario);
                    parametros.put("correo", correoUsuario);
                    parametros.put("clave", contrasena);
                    return parametros;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            // Si verificaded es falso, no se procede con la solicitud
            Toast.makeText(this, "Debe completar todos los campos correctamente", Toast.LENGTH_SHORT).show();
        }
    }
}
