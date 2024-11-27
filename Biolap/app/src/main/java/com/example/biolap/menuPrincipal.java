package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.example.biolap.modelo.registros;
import com.example.biolap.modelo.usuarioData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class menuPrincipal extends AppCompatActivity {
    private TextView usuario;
    private String name;
    usuarioData ud = usuarioData.getInstance();
    private EditText dni, nombre;
    registros rg = registros.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menuprincipal);
        usuario = findViewById(R.id.nombreTXT);
        name = ud.getUsuario_nombre();
        usuario.setText(name);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void nuevoRegistro(View view){
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogFView = inflater.inflate(R.layout.dni_nombre, null);
            builder.setView(dialogFView);

            nombre = dialogFView.findViewById(R.id.NombrePacienteN);
            dni = dialogFView.findViewById(R.id.DniPacienteN);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            Button aceptar = dialogFView.findViewById(R.id.boton_seguir);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            Button cancelar = dialogFView.findViewById(R.id.boton_salir);
            AlertDialog dialog = builder.create();

            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    busqueda("http://192.168.1.11/bio.lap/obtener_pacientes.php");
                    dialog.dismiss();
                }
            });

            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
    }
    public void nomenclatura(View view){
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
        Intent nu = new Intent(this, nomenclaturas.class);
        startActivity(nu);
        //finish();
    }
    public void buscar(View view){
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
        Intent b = new Intent(this, gestionarAnalisis.class);
        startActivity(b);
        //finish();
    }
    public void ajustes(View view){
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
        Intent intent = new Intent(this, ajustes.class);
        startActivity(intent);
        finish();
    }

    private void busqueda(String url) {
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
                        rg.setId(jsonResponse.getString("id"));
                        rg.setNombreC(jsonResponse.getString("nombre"));
                        rg.setDni(jsonResponse.getString("dni"));
                        rg.setEdad(jsonResponse.getString("edad"));
                        rg.setTelefono(jsonResponse.getString("telefono"));
                        Toast.makeText(menuPrincipal.this, rg.getNombreC(), Toast.LENGTH_SHORT).show();
                        Intent fp = new Intent(getApplicationContext(), formPacientes.class);
                        startActivity(fp);
                    } else {
                        rg.setId("0");
                        rg.setNombreC(nombre.getText().toString());
                        rg.setDni(dni.getText().toString());
                        Toast.makeText(menuPrincipal.this, "nuevo", Toast.LENGTH_SHORT).show();
                        Intent fp = new Intent(getApplicationContext(), formPacientes.class);
                        startActivity(fp);
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
                datos.put("dni", dni.getText().toString());
                datos.put("nombre", nombre.getText().toString());
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