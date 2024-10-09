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
    private String name;
    usuarioData ud = usuarioData.getInstance();
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
        Intent nr = new Intent(this, formPacientes.class);
        startActivity(nr);
        //finish();
    }
    public void nomenclatura(View view){
        Intent nu = new Intent(this, nomenclaturas.class);
        startActivity(nu);
        //finish();
    }
    public void buscar(View view){
        Intent b = new Intent(this, gestionarAnalisis.class);
        startActivity(b);
        //finish();
    }
    public void ajustes(View view){
        Intent intent = new Intent(this, ajustes.class);
        startActivity(intent);
        finish();
    }
}