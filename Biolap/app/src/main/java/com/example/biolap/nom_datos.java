package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import com.example.biolap.modelo.NomLista;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class nom_datos extends AppCompatActivity {
    private EditText codigo;
    private EditText nombre;
    private EditText form;
    private TextView id;
    private String idN;
    private String nombreN;
    private String codigoN;
    private String formulario;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nom_datos);

        id = findViewById(R.id.idNom);
        codigo = findViewById(R.id.codigoNom);
        nombre = findViewById(R.id.nombreNom);
        form = findViewById(R.id.formNom);
        NomLista element = (NomLista) getIntent().getSerializableExtra("NomLista");
        if (element != null) {
            form.setText(element.getFormulario());
            codigo.setText(element.getCodigo());
            nombre.setText(element.getNombre());
            id.setText(element.getId());
        } else {
            Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void modificarN(View view){
        idN = id.getText().toString();
        codigoN = codigo.getText().toString();
        nombreN = nombre.getText().toString();
        formulario = form.getText().toString();
        boolean val = true;

        if(TextUtils.isEmpty(codigoN)){
            codigo.setError("Cambio obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(nombreN)){
            nombre.setError("Cambio obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(formulario)){
            form.setText("Campo obligatorio");
            val = false;
        }
        if(val){
            modDatos("http://192.168.0.108/bio.lap/modificar_nom.php");
        }
    }
    public void eliminarN(View view){
        String n = nombre.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro que deseas eliminar '" + n + "' ?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliDatos("http://192.168.0.108/bio.lap/eliminar_nom.php");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void eliDatos(String url) {
        idN = id.getText().toString();
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Se eliminó con éxito", Toast.LENGTH_SHORT).show();
                        Intent nom = new Intent(getApplicationContext(), nomenclaturas.class);
                        startActivity(nom);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error en la eliminacion", Toast.LENGTH_SHORT).show();
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
                datos.put("id", idN);
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    private void modDatos(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Se modificó con éxito", Toast.LENGTH_SHORT).show();
                        Intent nom = new Intent(getApplicationContext(), nomenclaturas.class);
                        startActivity(nom);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error en la modificación", Toast.LENGTH_SHORT).show();
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
                datos.put("id", idN);
                datos.put("codigo", codigoN);
                datos.put("nombre", nombreN);
                datos.put("formulario", formulario);
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }
}