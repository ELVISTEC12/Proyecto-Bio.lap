package com.example.biolap;

import android.content.Intent;
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

        b = findViewById(R.id.buscar_d);
        pacientes = findViewById(R.id.pacientes_listado);
        pacientes.setLayoutManager(new LinearLayoutManager(this));


        String url = "http://192.168.1.11/bio.lap/lista_pac.php";


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<PacienteLista> PacienteLista = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String nombre = jsonObject.getString("nombre");
                                String obra = jsonObject.getString("obra_social");
                                String edad = jsonObject.getString("edad");
                                String dni = jsonObject.getString("dni");
                                String telefono = jsonObject.getString("telefono");
                                String medico = jsonObject.getString("medico");
                                String rutina = jsonObject.getString("rutina");
                                String fecha = jsonObject.getString("fecha_creacion");

                                PacienteLista pacientes = new PacienteLista(id, nombre, obra, edad, dni, telefono, medico, rutina, fecha);
                                PacienteLista.add(pacientes);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        PacienteAdapter adapter = new PacienteAdapter(PacienteLista, new PacienteAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(PacienteLista item) {
                                moverP(item);
                            }
                        });
                        pacientes.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(gestionarAnalisis.this, error.toString(), Toast.LENGTH_SHORT).show();
                        ;
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

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
}