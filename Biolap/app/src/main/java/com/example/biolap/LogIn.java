package com.example.biolap;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biolap.modelo.usuarioData;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {
    EditText usuarioTXT;
    EditText contraTXT;
    TextView errorT;
    ProgressBar n;
    ImageView no, inter, sinconex, mostrar, ocultar;
    usuarioData ud = usuarioData.getInstance();
    Button sin_in;

    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//ojo
        ocultar = findViewById(R.id.ojos);
        mostrar=findViewById(R.id.ojosa);



        usuarioTXT = findViewById(R.id.usuarioTXT);
        contraTXT = findViewById(R.id.contrasenaTXT);
        errorT = findViewById(R.id.textView23);
        n = findViewById(R.id.barradeprogreso);
        no = findViewById(R.id.error);
        inter = findViewById(R.id.sin_internet);
        sin_in = findViewById(R.id.bo_sin_internet);
        sinconex = findViewById(R.id.sin_conexion);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        // Cargar datos almacenados (si existen) en los EditText
        cargarDatosGuardados();

        sin_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salir();
            }
        });
        // Iniciar la animación al hacer clic

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void validar(View view) {
        if (!isConnected()) {
            inter.setVisibility(View.VISIBLE);
            sin_in.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();

        } else {
            boolean val = true;
            String nombre = usuarioTXT.getText().toString();
            String clave = contraTXT.getText().toString();
            if (TextUtils.isEmpty(nombre)) {
                usuarioTXT.setError("Campo obligatorio");
                val = false;
            }
            if (TextUtils.isEmpty(clave)) {
                contraTXT.setError("Campo obligatorio");
                val = false;
            }
            if (val) {
                n.setVisibility(View.VISIBLE);

                enviarDatos("http://192.168.1.88/bio.lap/validar_usuario.php");

            }
        }
    }

    public void enviarDatos(String URL) {
        if (!isConnected()) {
            inter.setVisibility(View.VISIBLE);
            sin_in.setVisibility(View.VISIBLE);
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();

        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            // Guardar el ID del usuario en SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("USER_ID", jsonResponse.getInt("id")); // Obtener el ID desde la respuesta
                            editor.apply();

                            // Guardar el nombre de usuario en la instancia ud
                            ud.setUsuario_nombre(jsonResponse.getString("nombre"));
                            ud.setId_usuario(jsonResponse.getString("id"));

                            // Redirigir al menú principal
                            Intent mp = new Intent(getApplicationContext(), menuPrincipal.class);
                            startActivity(mp);
                            finish();
                        } else {
                            n.setVisibility(View.GONE);
                            no.setVisibility(View.VISIBLE);
                            Toast.makeText(LogIn.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            new android.os.Handler().postDelayed(() -> {
                                no.setVisibility(View.GONE);
                            }, 3000);
                        }
                    } catch (JSONException e) {
                        n.setVisibility(View.GONE);
                        sinconex.setVisibility(View.VISIBLE);
                        new android.os.Handler().postDelayed(() -> {
                            sinconex.setVisibility(View.GONE);
                        }, 3000);
                        e.printStackTrace();
                        Toast.makeText(LogIn.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorT.setText(error.toString());
                    n.setVisibility(View.GONE);
                    sinconex.setVisibility(View.VISIBLE);
                    new android.os.Handler().postDelayed(() -> {
                        sinconex.setVisibility(View.GONE);
                    }, 3000);
                }
            }) {

                @Override

                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    String nombre = usuarioTXT.getText().toString();
                    String clave = contraTXT.getText().toString();

                    parametros.put("nombre", nombre);
                    parametros.put("clave", clave);

                    // Guardar temporalmente los datos de nombre y clave, si es necesario
                    guardarDatos(nombre, clave);

                    return parametros;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void guardarDatos(String nombre, String clave) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nombre", nombre);
        editor.putString("clave", clave);
        editor.apply();
    }

    private void cargarDatosGuardados() {
        String nombreGuardado = sharedPreferences.getString("nombre", "");
        String claveGuardada = sharedPreferences.getString("clave", "");
        usuarioTXT.setText(nombreGuardado);
        contraTXT.setText(claveGuardada);
    }

    public void registrar(View view) {
        if (!isConnected()) {
            inter.setVisibility(View.VISIBLE);
            sin_in.setVisibility(View.VISIBLE);
        } else {
            Intent r = new Intent(this, registrarUsuario.class);
            startActivity(r);
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void salir() {
        System.exit(0);
    }

    private boolean isPasswordVisible = false; // Declara la variable al inicio


    public void contraseña(View view){

        if(isPasswordVisible) {
            // Si la contraseña está visible, ocultarla
            contraTXT.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ocultar.setVisibility(View.VISIBLE);
            mostrar.setVisibility(View.GONE);
        } else {
            // Si la contraseña está oculta, mostrarla
            contraTXT.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ocultar.setVisibility(View.GONE);
            mostrar.setVisibility(View.VISIBLE);
        }

        // Alternar el estado de la variable booleana
        isPasswordVisible = !isPasswordVisible;

        // Para mover el cursor al final del texto
        contraTXT.setSelection(contraTXT.getText().length());
    }

}

