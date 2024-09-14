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

public class ajustes extends AppCompatActivity {
    private TextView nombre;
    private String v;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustes);
        //nombre = findViewById(R.id.u);
        Intent intent = getIntent();
        v = intent.getStringExtra("nombre");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void cam(View view){
        Toast.makeText(this, "nombre: "+v, Toast.LENGTH_SHORT).show();
    }
    public void cuenta(View view){
        Intent c = new Intent(this, perfilAjuste.class);
        startActivity(c);
    }
    public void perfil(View view){
        Intent p = new Intent(this, cuentaAjustes.class);
        startActivity(p);
    }
}