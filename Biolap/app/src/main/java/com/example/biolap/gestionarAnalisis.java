package com.example.biolap;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biolap.modelo.NomAdapter;
import com.example.biolap.modelo.NomLista;
import com.example.biolap.modelo.PacienteAdapter;
import com.example.biolap.modelo.PacienteLista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class gestionarAnalisis extends AppCompatActivity {

    private EditText b;
    private RecyclerView pacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestionar_analisis);

        pacientes = findViewById(R.id.pacientes_listado);
        pacientes.setLayoutManager(new LinearLayoutManager(this));

        String url = "http://192.168.1.5/bio.lap/lista_pac.php";
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray data = response.getJSONArray("data");
                                List<PacienteLista> pacienteLista = new ArrayList<>();

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject = data.getJSONObject(i);

                                    String id = jsonObject.getString("registro_id");
                                    String nombre = jsonObject.getString("paciente_nombre");
                                    String obra = jsonObject.getString("obra_social_nombre");
                                    String edad = jsonObject.getString("paciente_edad");
                                    String dni = jsonObject.getString("paciente_dni");
                                    String telefono = jsonObject.getString("paciente_telefono");
                                    String medico = jsonObject.getString("medico_nombre");
                                    String rutina = jsonObject.getString("rutina_detalle");
                                    String fecha = jsonObject.getString("fecha_registro");

                                    PacienteLista pacientes = new PacienteLista(id, nombre, obra, edad, dni, telefono, medico, rutina, fecha);
                                    pacienteLista.add(pacientes);
                                }

                                PacienteAdapter adapter = new PacienteAdapter(pacienteLista, new PacienteAdapter.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(PacienteLista item) {
                                        moverP(item);
                                    }
                                });

                                pacientes.setAdapter(adapter);
                            } else {
                                Toast.makeText(gestionarAnalisis.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(gestionarAnalisis.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(gestionarAnalisis.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void moverP(PacienteLista item){
        Intent nn = new Intent(getApplicationContext(),paciente_dato.class);
        nn.putExtra("PacienteLista", item);
        startActivity(nn);
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