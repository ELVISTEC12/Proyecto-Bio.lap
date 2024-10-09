package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.android.volley.toolbox.Volley;
import com.example.biolap.modelo.NomAdapter;
import com.example.biolap.modelo.NomLista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class nomenclaturas extends AppCompatActivity {
    private RecyclerView lista_nom;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nomenclaturas);


        lista_nom = findViewById(R.id.mostrar_nom);
        lista_nom.setLayoutManager(new LinearLayoutManager(this));

        String url = "http://192.168.0.108/bio.lap/lista_p.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<NomLista> nomenclaturaList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String nombre = jsonObject.getString("nombre");
                                String codigo = jsonObject.getString("codigo");
                                String id = jsonObject.getString("id");

                                NomLista nomenclatura = new NomLista(id,nombre, codigo);
                                nomenclaturaList.add(nomenclatura);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        NomAdapter adapter = new NomAdapter(nomenclaturaList, new NomAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(NomLista item) {
                                mover(item);
                            }
                        });
                        lista_nom.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(nomenclaturas.this, error.toString(), Toast.LENGTH_SHORT).show();;
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


    public void mover(NomLista item){
        Intent nn = new Intent(this,nom_datos.class);
        nn.putExtra("NomLista", item);
        startActivity(nn);
    }

    public void agregarN(View view){
        Intent an = new Intent(getApplicationContext(), nuevaNomenclatura.class);
        startActivity(an);
        finish();
    }
}