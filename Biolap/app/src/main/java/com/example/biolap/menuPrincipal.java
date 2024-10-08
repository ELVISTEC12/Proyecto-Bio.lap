package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biolap.modelo.usuarioData;

public class menuPrincipal extends AppCompatActivity {
    private TextView usuario;
    usuarioData ud = new usuarioData();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menuprincipal);
        usuario = findViewById(R.id.nombreTXT);

        usuario.setText(ud.getUsuario_nombre());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void nuevoRegistro(View view){
        Intent nr = new Intent(this, formPacientes.class);
        startActivity(nr);
    }
    public void nomenclatura(View view){
        Intent nu = new Intent(this, nomenclaturas.class);
        startActivity(nu);
    }
    public void buscar(View view){
        Intent b = new Intent(this, gestionarAnalisis.class);
        startActivity(b);
    }
    public void ajustes(View view){
        Toast.makeText(this, ud.getUsuario_nombre(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ajustes.class);
        startActivity(intent);
        /*usuarioData ud = new usuarioData();
        Toast.makeText(this, "nombre: "+ud.getNombre(), Toast.LENGTH_SHORT).show();*/
    }
}