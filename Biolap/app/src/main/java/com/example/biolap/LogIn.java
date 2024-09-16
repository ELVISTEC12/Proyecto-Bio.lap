package com.example.biolap;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.annotation.Nullable;
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
    Button ing;
    TextView errorT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        usuarioTXT = findViewById(R.id.usuarioTXT);
        contraTXT = findViewById(R.id.contrasenaTXT);
        ing = findViewById(R.id.boton_ingresar);
        errorT = findViewById(R.id.textView23);

        ing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                enviarDatos("http://192.168.1.5/bio.lap/validar_usuario.php");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void enviarDatos(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        // Si el login es exitoso, obtener los datos
                        int id = jsonResponse.getInt("id");
                        String nombre = jsonResponse.getString("usuarios");

                        // Guardar el ID del usuario en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("USER_ID", id);  // Guardar ID
                        editor.putBoolean("isUserLoggedIn", true);  // Marcar usuario como logueado
                        editor.apply();

                        // Crear un objeto de usuarioData para almacenar la información temporalmente
                        usuarioData ud = new usuarioData();
                        ud.setNombre(nombre);
                        ud.setUsuarioId(id);

                        // Iniciar una nueva actividad (menuPrincipal)
                        Intent mp = new Intent(getApplicationContext(), menuPrincipal.class);
                        mp.putExtra("usuarios", ud.getNombre());
                        startActivity(mp);
                    }else{
                        Toast.makeText(LogIn.this, "No se encuentran coincidencias", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LogIn.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorT.setText(error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuarios", usuarioTXT.getText().toString());
                parametros.put("contra", contraTXT.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void registrar(View view) {
        Intent r = new Intent(this, registrarUsuario.class);
        startActivity(r);
    }

    public void recu(){
        Intent r = new Intent(this, perfilAjuste.class);
        startActivity(r);
    }

}
 /* int id = jsonResponse.getInt("id");
                        String nombre = jsonResponse.getString("usuarios");
                        usuarioData ud = new usuarioData();
                        ud.setNombre(nombre);
                        ud.setUsuarioId(id);
                        Intent mp = new Intent(getApplicationContext(), menuPrincipal.class);
                        mp.putExtra("usuarios", ud.getNombre());
                        startActivity(mp);*/
