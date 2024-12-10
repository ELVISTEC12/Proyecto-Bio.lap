package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
    Button ce;
    usuarioData ud = usuarioData.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustes);

        cambio_c = findViewById(R.id.cambio_correo);
        ce = findViewById(R.id.as);
        ce.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cerrar();
                    }
                }
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void cerrar() {
        // Crear un cuadro de diálogo para confirmar la acción
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?");

        // Botón de confirmación
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Eliminar los datos de SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Redirigir al Login y finalizar actividad actual
                Intent intent = new Intent(ajustes.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        // Botón de cancelación
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cierra el cuadro de diálogo sin hacer nada
                dialog.dismiss();
            }
        });

        // Mostrar el cuadro de diálogo
        builder.create().show();
    }


    public void camCorreo(View view) {
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogFView = inflater.inflate(R.layout.cambiar_correo, null);
        builder.setView(dialogFView);

        usuario = dialogFView.findViewById(R.id.cambiar_correo);
        usuario.setText(ud.getUsuario_nombre());
        id = ud.getId_usuario();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button aceptar = dialogFView.findViewById(R.id.boton_salir);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button cancelar = dialogFView.findViewById(R.id.boton_seguir);
        AlertDialog dialog = builder.create();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = usuario.getText().toString();
                modificar("http://192.168.1.5/bio.lap/modificar_usuario.php");

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

    public void cuenta(View view) {
        Intent c = new Intent(this, cuentaAjustes.class);
        startActivity(c);
    }
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    private void modificar(String url) {
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

    public void Contactanos(View view) {
        // Crear un intent para enviar un correo
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        // Especificar la URI de Gmail
        emailIntent.setData(Uri.parse("mailto:"));

        // Añadir los correos destinatarios
        String[] addresses = {"coronado@gmail.com", "pablo@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);

        // Asunto del correo
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo");

        // Cuerpo del mensaje
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Este es el cuerpo del mensaje.");

        // Verificar si Gmail está disponible en el dispositivo
        emailIntent.setPackage("com.google.android.gm");

        // Comprobar si hay una aplicación de Gmail para manejar el correo
        try {
            startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No tienes la aplicación Gmail instalada.", Toast.LENGTH_SHORT).show();
        }
    }

}
