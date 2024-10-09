package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.biolap.modelo.usuarioData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ajustes extends AppCompatActivity {

    private TextView cambio_c;
    private String id;
    private EditText usuario;
    private String nombre;
    usuarioData ud = usuarioData.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustes);

        cambio_c = findViewById(R.id.cambio_correo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void camCorreo(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogFView = inflater.inflate(R.layout.cambiar_correo,null);
        builder.setView(dialogFView);
        usuario = dialogFView.findViewById(R.id.cambiar_correo);
        usuario.setText(ud.getUsuario_nombre());
        id = ud.getId_usuario();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button aceptar = dialogFView.findViewById(R.id.boton_aceptar);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button cancelar = dialogFView.findViewById(R.id.boton_cancelar);
        AlertDialog dialog = builder.create();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = usuario.getText().toString();
                modificar("http://192.168.0.108/bio.lap/modificar_usuario.php");
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

    public void cuenta(View view){
        Intent c = new Intent(this, cuentaAjustes.class);
        startActivity(c);
    }

    private void modificar(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        ud.setUsuario_nombre(nombre);
                        Toast.makeText(ajustes.this, "Listo", Toast.LENGTH_SHORT).show();
                        Intent mp = new Intent(getApplicationContext(), menuPrincipal.class);
                        startActivity(mp);
                        finish();
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
                datos.put("id", id);
                datos.put("nombre", nombre);
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

}