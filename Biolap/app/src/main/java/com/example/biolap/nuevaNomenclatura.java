package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.example.biolap.modelo.usuarioData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class nuevaNomenclatura extends AppCompatActivity {
    private EditText codNom;
    private EditText nomNom;
    private EditText formNom;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_nomenclatura);
        codNom = findViewById(R.id.codNomTXT);
        nomNom = findViewById(R.id.nomNomTXT);
        formNom = findViewById(R.id.formNomTXT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void menu(View view){
        Intent m = new Intent(this, menuPrincipal.class);
        startActivity(m);
    }

    public void validar(View view){
        boolean val = true;
        String codigo = codNom.getText().toString();
        String nombre = nomNom.getText().toString();
        String form = formNom.getText().toString();
        if(TextUtils.isEmpty(codigo)){
            codNom.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(nombre)){
            nomNom.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(form)){
            formNom.setError("Campo obligatorio");
            val = false;
        }
        if(val){
            datos("http://192.168.0.108/bio.lap/insertar_nomenclatura.php");
        }
    }

    private void datos(String url){
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(nuevaNomenclatura.this, "Se creo con éxito", Toast.LENGTH_SHORT).show();
                        Intent mp = new Intent(getApplicationContext(), nomenclaturas.class);
                        startActivity(mp);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error al cargar", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> datos = new HashMap<String,String>();
                datos.put("codigo",codNom.getText().toString());
                datos.put("nombre",nomNom.getText().toString());
                datos.put("formulario",formNom.getText().toString());

                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }
}