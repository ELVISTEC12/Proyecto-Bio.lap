package com.example.biolap;

import android.content.Intent;
import android.os.Bundle;
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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText usuarioTXT;
    private EditText contraTXT;

    private Button ing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        usuarioTXT = findViewById(R.id.usuarioTxt);
        contraTXT = findViewById(R.id.contrasenaTxt);
        ing= findViewById(R.id.boton_ingresar);
        ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String url = "http://192.168.1.4/bio.lap/validar_usuario.php";
                ingresar("http://192.168.1.4/bio.lap/validar_usuario.php");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void ingresar(String URL) {
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Convertir la respuesta a un objeto JSON
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Toast.makeText(getApplicationContext(), "Operación exitosa", Toast.LENGTH_SHORT).show();
                        // Navegar a la nueva actividad
                        Intent intent = new Intent(getApplicationContext(), menup.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                if (error.networkResponse != null) {
                    errorMessage += " Código de respuesta: " + error.networkResponse.statusCode;
                }
                Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametro = new HashMap<String, String>();
                parametro.put("nombre", usuarioTXT.getText().toString());
                parametro.put("contra", contraTXT.getText().toString());
                return parametro;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }


        /*String v1 = usuarioTXT.getText().toString();
        String v2 = contraTXT.getText().toString();
        if(v1.equals("Pablo") && v2.equals("riverplate")){
            Intent m = new Intent(this,menup.class);
            startActivity(m);
            usuarioTXT.setText("");
            contraTXT.setText("");
        }
        else{
            Toast.makeText(this, "Los datos no coinciden", Toast.LENGTH_SHORT).show();
        }*/

    public void registrar(View view){
        Intent r = new Intent(this,registrarUsuario.class);
        startActivity(r);
    }
}