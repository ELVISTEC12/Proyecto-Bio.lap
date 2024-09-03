package com.example.biolap;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biolap.conexion.Conexion;
import com.example.biolap.sqlCod.usuarioCod;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {
    EditText usuarioTXT;
    EditText contraTXT;
    Button ing;
    String usuario,contrasena;
    TextView errorT;
    boolean verificared=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        usuarioTXT = findViewById(R.id.usuarioTXT);
        contraTXT = findViewById(R.id.contrasenaTXT);
        ing = findViewById(R.id.boton_ingresar);
        errorT = findViewById(R.id.textView23);

        ing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //enviarDatos("http://bio-lab.byethost13.com/validar_usuario.php");
                validar();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public boolean validar(){
        usuario = usuarioTXT.getText().toString().trim();
        contrasena = contraTXT.getText().toString().trim();
        if(TextUtils.isEmpty(usuario)){
            usuarioTXT.setError("Campo obligatorio");
            return false;
        }
        if(TextUtils.isEmpty(contrasena)){
            contraTXT.setError("Campo obligatorio");
            return false;
        }
        usuarioCod uc = new usuarioCod();
        boolean v = uc.verificar(usuario,contrasena);
        if(!v){
            Intent mp = new Intent(this, menuPrincipal.class);
            startActivity(mp);
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    public void enviarDatos(String URL){
        boolean verificacion = validar();
        if (verificacion){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.isEmpty()){
                        Intent mp = new Intent(getApplicationContext(), menuPrincipal.class);
                        startActivity(mp);
                    }
                    else{
                        Toast.makeText(LogIn.this, "Datos erroneos", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorT.setText(error.toString());
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>parametros= new HashMap<String,String>();
                    parametros.put("nombre",usuarioTXT.getText().toString());
                    parametros.put("contra",contraTXT.getText().toString());
                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(this, "Error en validacion", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrar(View view) {
        Intent r = new Intent(this, registrarUsuario.class);
        startActivity(r);
    }
}
