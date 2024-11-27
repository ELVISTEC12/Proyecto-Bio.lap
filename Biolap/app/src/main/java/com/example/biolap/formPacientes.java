package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biolap.modelo.obraLista;
import com.example.biolap.modelo.registros;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class formPacientes extends AppCompatActivity {
    EditText n, ob, ed, dni, num, medi;
    boolean verificaded = true;
    private Spinner obra_social;
    private List<obraLista> itemList = new ArrayList<>();
    private ArrayAdapter<obraLista> adapter;
    private String idO, idp;
    registros rg = registros.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_pacientes);
        n= findViewById(R.id.n_p);
        ed= findViewById(R.id.edad);
        num=findViewById(R.id.nu_p);
        medi=findViewById(R.id.med);
        dni=findViewById(R.id.dnipa);
        obra_social=findViewById(R.id.obra_socialS);
        idp = rg.getId();
        if(idp.equals("0")){
            n.setText(rg.getNombreC());
            dni.setText(rg.getDni());
        }
        else{
            n.setText(rg.getNombreC());
            dni.setText(rg.getDni());
            num.setText(rg.getTelefono());
            ed.setText(rg.getEdad());
        }

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, listaPacientes);

        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        obra_social.setAdapter(adapter);

        obras("http://192.168.1.11/bio.lap/obra_social.php");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void rutina(View view) {
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
        String nombre = n.getText().toString();

        obraLista pacienteSeleccionado = (obraLista) obra_social.getSelectedItem();

        idO = pacienteSeleccionado.getId();

        String edad = ed.getText().toString();
        String numero = num.getText().toString();
        String medico = medi.getText().toString();
        String DNI = dni.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            n.setError("Ingrese el nombre del paciente");
            verificaded = false;
        }
        if (TextUtils.isEmpty(edad)) {
            ed.setError("Ingrese la fecha de nacimiento de " + nombre);
            verificaded = false;
        }

        if (TextUtils.isEmpty(numero)) {
            num.setError("Ingrese el número telefónico de " + nombre);
            verificaded = false;
        }
        if (TextUtils.isEmpty(medico)) {
            medi.setError("Ingrese el médico de " + nombre);
            verificaded = false;
        }

        if (TextUtils.isEmpty(DNI)) {
            dni.setError("Ingrese el DNI de " + nombre);
            verificaded = false;
        }

        if (verificaded) {
            Toast.makeText(this, idp, Toast.LENGTH_SHORT).show();
            if(idp.equals("0")){
                rg.setMedico(medico);
                rg.setObra_social(idO);
                agregar("http://192.168.0.108/bio.lap/nuevo_paciente.php");
            }
            else{
                rg.setMedico(medico);
                rg.setObra_social(idO);
                Intent ar = new Intent(getApplicationContext(), analisisRutina.class);
                startActivity(ar);
            }
        }
    }

    private void agregar(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        rg.setId(jsonResponse.getString("id"));

                        Toast.makeText(getApplicationContext(), "Se guardó con exito", Toast.LENGTH_SHORT).show();
                        Intent ar = new Intent(getApplicationContext(), analisisRutina.class);
                        startActivity(ar);

                    } else {
                        Toast.makeText(getApplicationContext(), "Error en la carga", Toast.LENGTH_SHORT).show();
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
                datos.put("nombre",n.getText().toString());
                datos.put("edad",ed.getText().toString());
                datos.put("dni",dni.getText().toString());
                datos.put("telefono",num.getText().toString());
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    private void obras(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        itemList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String nombre = jsonObject.getString("nombre");
                                itemList.add(new obraLista(id, nombre));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error al procesar datos JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);//Hoshigaoka yuri / 호시가오카 유리 (Nobunaga-sensei no Osanazuma / 노부나가 선생님의 어린 아내)
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
