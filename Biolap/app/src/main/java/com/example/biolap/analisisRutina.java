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
import com.example.biolap.modelo.registros;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class analisisRutina extends AppCompatActivity {
    private EditText rutinasTXT;
    private EditText form;
    private String pasiente;
    private String obra;
    private String edad;
    private String dni;
    private String telefono;
    private String medico;

    registros rg = registros.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analisis_rutina);

        rutinasTXT = findViewById(R.id.rutinaTXT);
        form = findViewById(R.id.rutinaFormTXT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void buscarN(View view){
        boolean verificar = true;
        String cod = rutinasTXT.getText().toString();
        if (cod.equals("")) {
            rutinasTXT.setError("Campo obligatorio para la búsqueda");
            verificar = false;
        }
        if (verificar) {
            resultadoss("http://192.168.0.108/bio.lap/mostrar_nom.php");
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
                        String formN = jsonResponse.getString("formulario");
                        form.setText(formN);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error en la búsqueda", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
            pasiente = rg.getNombreC();
            obra = rg.getObra_social();
            edad = rg.getEdad();
            dni = rg.getDni();
            telefono = rg.getTelefono();
            medico = rg.getMedico();
            guardar("http://192.168.0.108/bio.lap/insertar_paciente.php");
        }
    }

    private void guardar(String url) {
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
                        Toast.makeText(getApplicationContext(), "Error en la búsqueda", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> datos = new HashMap<String, String>();
                datos.put("nombre",pasiente);
                datos.put("obra_social",obra);
                datos.put("edad",edad);
                datos.put("dni",dni);
                datos.put("telefono",telefono);
                datos.put("medico",medico);
                datos.put("rutina",form.getText().toString());
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }
}