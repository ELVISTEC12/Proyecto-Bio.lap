package com.example.biolap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biolap.modelo.NomLista;

public class nom_datos extends AppCompatActivity {
    private EditText codigo;
    private EditText nombre;
    private EditText form;
    private TextView id;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nom_datos);

        id = findViewById(R.id.idNom);
        codigo = findViewById(R.id.codigoNom);
        nombre = findViewById(R.id.nombreNom);
        form = findViewById(R.id.formNom);
        NomLista element = (NomLista) getIntent().getSerializableExtra("NomLista");
        codigo.setText(element.getCodigo());
        nombre.setText(element.getNombre());
        id.setText(element.getId());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}