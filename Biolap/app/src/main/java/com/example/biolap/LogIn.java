package com.example.biolap;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    ProgressBar n;
    ImageView no;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        usuarioTXT = findViewById(R.id.usuarioTXT);
        contraTXT = findViewById(R.id.contrasenaTXT);
        errorT = findViewById(R.id.textView23);
        n=findViewById(R.id.barradeprogreso);
        no=findViewById(R.id.error);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void ini (View view){
        Intent mp = new Intent(this, menuPrincipal.class);
        startActivity(mp);
    }

    public void validar(View view){
        boolean val = true;
        String nombre = usuarioTXT.getText().toString();
        String clave = contraTXT.getText().toString();
        if(TextUtils.isEmpty(nombre)){
            usuarioTXT.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(clave)){
            contraTXT.setError("Campo obligatorio");
            val = false;
        }
        if(val){
            n.setVisibility(View.VISIBLE);//mostrar barra de progreso
            enviarDatos("http://192.168.1.6/bio.lap/validar_usuario.php");
        }
    }

    public void enviarDatos(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        String id = jsonResponse.getString("id");
                        String nombre = jsonResponse.getString("nombre");

                        // Guardar los datos del usuario
                        usuarioData ud = new usuarioData();
                        ud.setUsuario_nombre(nombre);
                        ud.setId_usuario(id);

                        // Redirigir a la pantalla principal
                        Intent mp = new Intent(getApplicationContext(), menuPrincipal.class);
                        startActivity(mp);
                    } else {
                        // Mostrar mensaje de error si no hay coincidencias
                        n.setVisibility(View.GONE);
                        no.setVisibility(View.VISIBLE);
                        Toast.makeText(LogIn.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    n.setVisibility(View.GONE);
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
                parametros.put("nombre", usuarioTXT.getText().toString());
                parametros.put("clave", contraTXT.getText().toString());
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
