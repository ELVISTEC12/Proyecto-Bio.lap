package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class nomenclaturaMenu extends AppCompatActivity {
    private EditText codigo;
    private EditText nombre;
    private EditText formulario;
    private Button buscar;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        codigo = findViewById(R.id.codBuscador);
        nombre = findViewById(R.id.nombreBusc);
        formulario = findViewById(R.id.formBusc);
        buscar = findViewById(R.id.buscador);

        buscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resultados("http://192.168.0.108/bio.lap/mostrar_nom.php");
            }
        });

        setContentView(R.layout.activity_nomenclaturamenu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; //"http://192.168.0.108/bio.lap/validar_usuario.php"
        });
    }

    public void resultados(String url){
        String code = codigo.getText().toString();
        if(TextUtils.isEmpty(code)){
            codigo.setError("Campo oblicatorio para la búsqueda");
        }
        else{
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            String nomNom = jsonResponse.getString("nombre");
                            String formNom = jsonResponse.getString("formulario");
                            nombre.setText(nomNom);
                            formulario.setText(formNom);

                        } else {
                            Toast.makeText(getApplicationContext(), "Error con la búsqueda", Toast.LENGTH_SHORT).show();
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
                    datos.put("codigo",codigo.getText().toString());
                    return datos;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(sr);
        }
    }

    public void agregar(View view){
        Intent nn = new Intent(this, nuevaNomenclatura.class);
        startActivity(nn);
    }
}