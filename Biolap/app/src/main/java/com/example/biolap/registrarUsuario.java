package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.biolap.sqlCod.registarCod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class registrarUsuario extends AppCompatActivity {
    EditText nombre, correo, contra, conta1;
    Button b1;
    CheckBox check;
    boolean verificaded = true;

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void registrar(View view) {
        String nombreUsuario = nombre.getText().toString().trim();
        String correoUsuario = correo.getText().toString().trim();
        String contrasena = contra.getText().toString().trim();
        String confirmacionContrasena = conta1.getText().toString().trim();

        if (TextUtils.isEmpty(nombreUsuario)) {
            nombre.setError("Campo obligatorio");
            verificaded = false;
        }
        if (TextUtils.isEmpty(correoUsuario) || !Patterns.EMAIL_ADDRESS.matcher(correoUsuario).matches()) {
            correo.setError("Correo electrónico inválido");
            verificaded = false;

        }
        if (TextUtils.isEmpty(contrasena)) {
            contra.setError("Campo obligatorio");
            verificaded = false;
        }
        if (TextUtils.isEmpty(confirmacionContrasena)) {
            conta1.setError("Campo obligatorio");
            verificaded = false;
        }
        if (!contrasena.equals(confirmacionContrasena)) {
            conta1.setError("Las contraseñas no coinciden");
            verificaded = false;
        }
        if (check != null && !check.isChecked()) {
            Toast.makeText(this, "Debe aceptar los términos", Toast.LENGTH_SHORT).show();
            verificaded = false;
        }
        registarCod rd = new registarCod();
        if (verificaded) {
            boolean v = rd.registro(nombreUsuario, correoUsuario, contrasena);
            if (v) {
                Toast.makeText(this, "Registrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
