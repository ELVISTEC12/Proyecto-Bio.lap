package com.example.biolap;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class gestionarAnalisis extends AppCompatActivity {
    private ListView mostra;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaPacientes;
    EditText b ;
    Button apli;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestionar_analisis);
        mostra = findViewById(R.id.mostra);
        b = findViewById(R.id.buscar_d);
        apli = findViewById(R.id.b_buscar);
        listaPacientes = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaPacientes);
        mostra.setAdapter(adapter);

        obtenerPacientes();
        apli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = b.getText().toString().trim();
                if (!term.isEmpty()) {
                    buscarPacientes(term); // Llamar al método de búsqueda
                } else {
                    Toast.makeText(gestionarAnalisis.this, "Por favor, ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void obtenerPacientes() {
        String URL = "http://192.168.1.6/bio.lap/obtener_pacientes.php"; // Asegúrate de que la URL sea correcta

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                JSONArray data = jsonResponse.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject paciente = data.getJSONObject(i);
                                    String nombre = paciente.getString("nombre");
                                    String fecha = paciente.getString("fecha");
                                    listaPacientes.add(nombre + " - " + fecha); // Formato para mostrar en el ListView
                                }
                                adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                            } else {
                                //Toast.makeText(TuActividad.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(gestionarAnalisis.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(gestionarAnalisis.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void buscarPacientes(String term) {
        String URL = "http://192.168.1.6/bio.lap/buscar_pacientes.php?term=" + term; // Asegúrate de que la URL sea correcta

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            listaPacientes.clear(); // Limpiar la lista antes de agregar nuevos resultados

                            if (success) {
                                JSONArray data = jsonResponse.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject paciente = data.getJSONObject(i);
                                    String nombre = paciente.getString("nombre");
                                    String fecha = paciente.getString("fecha");
                                    listaPacientes.add(nombre + " - " + fecha); // Formato para mostrar en el ListView
                                }
                                adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                            } else {
                                Toast.makeText(gestionarAnalisis.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(gestionarAnalisis.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(gestionarAnalisis.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}