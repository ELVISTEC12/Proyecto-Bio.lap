package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.biolap.modelo.registros;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class analisisRutina extends AppCompatActivity {
    private EditText rutinasTXT;
    private EditText form;
    private String obra;
    private String medico;
    private String nom = "";
    private String id;
    private ProgressBar carga;
    private ImageView no_encontrado, sin_servidor;

    registros rg = registros.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analisis_rutina);

        rutinasTXT = findViewById(R.id.rutinaTXT);
        form = findViewById(R.id.rutinaFormTXT);
        carga=findViewById(R.id.barradeprogreso);
        no_encontrado=findViewById(R.id.error);
        sin_servidor=findViewById(R.id.sin_conexion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void buscarN(View view){
        carga.setVisibility(View.VISIBLE);
        boolean verificar = true;
        String cod = rutinasTXT.getText().toString();
        if (cod.equals("")) {
            rutinasTXT.setError("Campo obligatorio para la búsqueda");
            verificar = false;
        }
        if (verificar) {
            resultadoss("http://192.168.1.11/bio.lap/mostrar_nom.php");
            carga.setVisibility(View.GONE);
        }
    }

    private void resultadoss(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        String formN = "\n" + jsonResponse.getString("formulario");
                        String act = form.getText().toString();
                        nom = act + formN ;
                        form.setText(nom);
                    } else {

                        Toast.makeText(getApplicationContext(), "Error en la búsqueda", Toast.LENGTH_SHORT).show();
                        no_encontrado.setVisibility(View.VISIBLE);
                        new android.os.Handler().postDelayed(() -> {
                            no_encontrado.setVisibility(View.GONE);
                        }, 3000);
                    }
                } catch (JSONException e) {
                    sin_servidor.setVisibility(View.VISIBLE);
                    new android.os.Handler().postDelayed(() -> {
                        sin_servidor.setVisibility(View.GONE);
                    }, 3000);
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sin_servidor.setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(() -> {
                    sin_servidor.setVisibility(View.GONE);
                }, 3000);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> datos = new HashMap<String, String>();
                datos.put("codigo", rutinasTXT.getText().toString());
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    public void finalizar(View view){
        String re = form.getText().toString();
        boolean val = true;
        if(TextUtils.isEmpty(re)){
            form.setError("No se escribió nada");
            val = false;
        }
        if(val) {
            id = rg.getId();
            obra = rg.getObra_social();
            medico = rg.getMedico();
            guardar("http://192.168.0.108/bio.lap/insertar_paciente.php");
        }
    }

    private void guardar(String url) {
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(analisisRutina.this, "Se guardó con exito", Toast.LENGTH_SHORT).show();
                        Intent m = new Intent(getApplicationContext(), menuPrincipal.class);
                        startActivity(m);
                        finish();
                    } else {
                        no_encontrado.setVisibility(View.VISIBLE);
                        new android.os.Handler().postDelayed(() -> {
                            no_encontrado.setVisibility(View.GONE);
                        }, 3000);
                        Toast.makeText(getApplicationContext(), "Error en la búsqueda", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    sin_servidor.setVisibility(View.VISIBLE);
                    new android.os.Handler().postDelayed(() -> {
                        sin_servidor.setVisibility(View.GONE);
                    }, 3000);
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sin_servidor.setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(() -> {
                    sin_servidor.setVisibility(View.GONE);
                }, 3000);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> datos = new HashMap<String, String>();
                datos.put("nombre",id);
                datos.put("obra_social",obra);
                datos.put("medico",medico);
                datos.put("rutina",form.getText().toString());
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}