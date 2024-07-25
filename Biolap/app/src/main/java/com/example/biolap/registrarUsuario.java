package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.HashMap;
import java.util.Map;

public class registrarUsuario extends AppCompatActivity {
    EditText nombre, correo, contra, conta1;
    Button b1;
    CheckBox check;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_usuario);
        nombre = findViewById(R.id.nuser_r);
        correo = findViewById(R.id.correo_r);
        contra = findViewById(R.id.contra1_r);
        conta1 = findViewById(R.id.contra2_r);
        b1 = findViewById(R.id.b_r);
        check = findViewById(R.id.checkBox);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarDatos()) {
                    //enviarDatos("http://192.168.1.4/bio.lap/insertar.php");
                    enviarDatos("http://192.168.1.4/bio.lap/insertar.php");
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validarDatos() {
        String nombreUsuario = nombre.getText().toString().trim();
        String correoUsuario = correo.getText().toString().trim();//Quita los espacios iniciales, finales y repetidos del texto.
        String contrasena = contra.getText().toString().trim();
        String confirmacionContrasena = conta1.getText().toString().trim();

        // Validar el campo de nombre
        if (TextUtils.isEmpty(nombreUsuario)) {
            nombre.setError("Campo obligatorio");
            return false;
        }

        // Validar el correo electrónico
        if (TextUtils.isEmpty(correoUsuario) || !Patterns.EMAIL_ADDRESS.matcher(correoUsuario).matches()) {
            correo.setError("Correo electrónico inválido");
            return false;
        }

        // Validar la contraseña
        if (TextUtils.isEmpty(contrasena)) {
            contra.setError("Campo obligatorio");
            return false;
        }

        // Validar la confirmación de contraseña
        if (TextUtils.isEmpty(confirmacionContrasena)) {
            conta1.setError("Campo obligatorio");
            return false;
        }

        // Verificar que las contraseñas coincidan
        if (!contrasena.equals(confirmacionContrasena)) {
            conta1.setError("Las contraseñas no coinciden");
            return false;
        }

        // Verificar si el CheckBox está marcado
        if (check != null && !check.isChecked()) {
            Toast.makeText(this, "Debe aceptar los términos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void enviarDatos(String url) {
      // Log.d("EnviarDatos", "Enviando datos a: " + url);

       StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
             //  Log.d("EnviarDatos", "Respuesta: " + response);
               Toast.makeText(getApplicationContext(), "Operación exitosa", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getApplicationContext(), menup.class);
               startActivity(intent);
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.e("EnviarDatos", "Error: " + error.toString());
               Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
           }
       }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> pa = new HashMap<String, String>();
               pa.put("usuarios", nombre.getText().toString());
               pa.put("contra", contra.getText().toString());
               pa.put("correo", correo.getText().toString());

               return pa;
           }
       };

       RequestQueue requestQueue = Volley.newRequestQueue(this);
       requestQueue.add(sr);
   }

}
