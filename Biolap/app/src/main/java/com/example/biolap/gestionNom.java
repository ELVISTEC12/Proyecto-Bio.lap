package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class gestionNom extends AppCompatActivity {
    private EditText codigo;
    private EditText nombre;
    private EditText formulario;
    private String id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_nom);
        codigo = findViewById(R.id.codNomBus);
        nombre = findViewById(R.id.nombreResult);
        formulario = findViewById(R.id.formResult);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void busqueda(View view) {
        boolean verificar = true;
        String cod = codigo.getText().toString();
        if (cod.equals("")) {
            codigo.setError("Campo obligatorio para la búsqueda");
            verificar = false;
        }
        if (verificar) {
            resultados("http://192.168.1.5/bio.lap/mostrar_nom.php");
        }
    }

    private void resultados(String url) {
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
                        String nombreN = jsonResponse.getString("nombre");
                        String formN = jsonResponse.getString("formulario");
                        id = jsonResponse.getString("id");
                        nombre.setText(nombreN);
                        formulario.setText(formN);
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
                datos.put("codigo", codigo.getText().toString());
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    public void modificar(View view) {
        boolean verificar = true;
        String nom = nombre.getText().toString();
        String form = formulario.getText().toString();
        if (nom.equals("")) {
            nombre.setError("Campo obligatorio");
            verificar = false;
        }
        if (form.equals("")) {
            formulario.setError("Campo obligatorio");
            verificar = false;
        }
        if (verificar) {
            modDatos("http://192.168.1.88/bio.lap/modificar_nom.php");
        }
    }

    private void modDatos(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(gestionNom.this, "Cambios realizados con éxito", Toast.LENGTH_SHORT).show();
                        nombre.setText("");
                        formulario.setText("");
                        codigo.setText("");
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
                datos.put("id", id);
                datos.put("codigo", codigo.getText().toString());
                datos.put("nombre", nombre.getText().toString());
                datos.put("formulario", formulario.getText().toString());
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    public void eliminarNom(View view) {
        String n = nombre.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro que deseas eliminar '" + n + "' ?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliDatos("http://192.168.1.88/bio.lap/eliminar_nom.php");
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
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(gestionNom.this, "Se eliminó con éxito", Toast.LENGTH_SHORT).show();
                        nombre.setText("");
                        formulario.setText("");
                        codigo.setText("");
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
                datos.put("id", id);
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    public void agregar(View view){
        Intent nn = new Intent (this, nuevaNomenclatura.class); startActivity(nn);
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
