package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class formPacientes extends AppCompatActivity {
    EditText n, ob, ed, dni, num, medi;
     boolean verificaded = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_pacientes);
        n= findViewById(R.id.n_p);//nombre paciente
        ed= findViewById(R.id.edad);//edad del paciente
        ob=findViewById(R.id.obra);//obra social del paciente
        num=findViewById(R.id.nu_p);//numero telefonico del paciente
        medi=findViewById(R.id.med);//medico del paciente

        dni=findViewById(R.id.dnipa);//dni del paciente


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void rutina(View view) {
        String nombre = n.getText().toString();
        String obra = ob.getText().toString();
        String edad = ed.getText().toString();
        String numero = num.getText().toString();
        String medico = medi.getText().toString();

        String DNI = dni.getText().toString();

         // Inicialización

        if (TextUtils.isEmpty(nombre)) {
            n.setError("Ingrese el nombre del paciente");
            verificaded = false;
        }
        if (TextUtils.isEmpty(obra)) {
            ob.setError("Ingrese la obra social de " + nombre);
            verificaded = false;
        }
        if (TextUtils.isEmpty(edad)) {
            ed.setError("Ingrese la edad de " + nombre);
            verificaded = false;
        } else {
            try {
                int edadInt = Integer.parseInt(edad);
                if (edadInt <= 0) {
                    ed.setError("La edad debe ser mayor que 0");
                    verificaded = false;
                }
            } catch (NumberFormatException e) {
                ed.setError("Ingrese una edad válida");
                verificaded = false;
            }
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
            String URL = "http://192.168.1.2/bio.lap/insertar_paciente.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Intent mp = new Intent(getApplicationContext(), menuPrincipal.class);
                            startActivity(mp);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al registrarse", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(formPacientes.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("nombre", nombre);
                    parametros.put("obra_social", obra);
                    parametros.put("edad", edad);
                    parametros.put("dni", DNI);
                    parametros.put("n_telefono", numero);
                    parametros.put("n_medico", medico);

                    return parametros;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Debe completar todos los campos correctamente", Toast.LENGTH_SHORT).show();
        }
    }




        /*Intent r = new Intent(this, analisisRutina.class);
        startActivity(r);*/
    }
