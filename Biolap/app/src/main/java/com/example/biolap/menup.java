package com.example.biolap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class menup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menup);
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
        Intent nn = new Intent(this, nomenclaturas.class);
        startActivity(nn);
    }
    public void buscar(View view){
        Intent b = new Intent(this, gestionarAnalisis.class);
        startActivity(b);
    }
    public void ajustes(View view){
        Intent a = new Intent(this, ajustes.class);
        startActivity(a);
    }
}