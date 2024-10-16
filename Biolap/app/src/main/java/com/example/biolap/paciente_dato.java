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
import com.example.biolap.modelo.PacienteLista;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class paciente_dato extends AppCompatActivity {
    private EditText dni;
    private EditText nombre;
    private EditText telefono;
    private TextView id;
    private EditText edad;
    private EditText medico;
    private EditText rutina;
    private EditText obra_social;
    private String idP,nombreP,telefonoP,edadP,medicoP,rutinaP,obraP,dniP;
    gestionarAnalisis ga = new gestionarAnalisis();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_paciente_dato);

        dni = findViewById(R.id.DNIPasienteE);
        nombre = findViewById(R.id.NombrePaciente);
        telefono = findViewById(R.id.telefonoPaciente);
        edad = findViewById(R.id.EdadPaciente);
        obra_social = findViewById(R.id.ObraSocialPaciente);
        medico = findViewById(R.id.MedicoPaciente);
        rutina = findViewById(R.id.RutinaPaciente);
        id = findViewById(R.id.idPacienteE);

        PacienteLista element = (PacienteLista) getIntent().getSerializableExtra("PacienteLista");
        if (element != null) {
            dni.setText(element.getDni());
            nombre.setText(element.getNombreP());
            telefono.setText(element.getTelefono());
            edad.setText(element.getEdad());
            obra_social.setText(element.getObra_social());
            medico.setText(element.getMedico());
            rutina.setText(element.getRutina());
            id.setText(element.getIdP());
        } else {
            Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void modificarP(View view){
        idP = id.getText().toString();
        nombreP = nombre.getText().toString();
        obraP = obra_social.getText().toString();
        dniP = dni.getText().toString();
        edadP = edad.getText().toString();
        telefonoP = telefono.getText().toString();
        medicoP = medico.getText().toString();
        rutinaP = rutina.getText().toString();
        boolean val = true;

        if(TextUtils.isEmpty(nombreP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(obraP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(dniP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(edadP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(telefonoP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(medicoP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(rutinaP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }

        if(val){
            modDatos("http://192.168.0.129/bio.lap/modificar_paciente.php");
        }
    }
    public void eliminarP(View view){
        idP = id.getText().toString();
        nombreP = nombre.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro que deseas eliminar '" + nombreP + "' ?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliDatos("http://192.168.0.129/bio.lap/eliminar_paciente.php");
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
                        ga.finish();
                        Toast.makeText(getApplicationContext(), "Se eliminó con éxito", Toast.LENGTH_SHORT).show();
                        Intent nom = new Intent(getApplicationContext(), gestionarAnalisis.class);
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
                datos.put("id", idP);
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
                        ga.finish();
                        Toast.makeText(getApplicationContext(), "Se modificó con éxito", Toast.LENGTH_SHORT).show();
                        Intent nom = new Intent(getApplicationContext(), gestionarAnalisis.class);
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
                datos.put("id", idP);
                datos.put("nombre", nombreP);
                datos.put("obra_social", obraP);
                datos.put("dni", dniP);
                datos.put("edad", edadP);
                datos.put("telefono", telefonoP);
                datos.put("medico", medicoP);
                datos.put("rutina", rutinaP);
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }
}