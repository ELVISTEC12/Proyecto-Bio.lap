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

import com.example.biolap.modelo.usuarioData;

public class ajustes extends AppCompatActivity {

    private TextView cambio_c;
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
        EditText correo = dialogFView.findViewById(R.id.cambiar_correo);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button aceptar = dialogFView.findViewById(R.id.boton_aceptar);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button cancelar = dialogFView.findViewById(R.id.boton_cancelar);
        AlertDialog dialog = builder.create();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ajustes.this, "Listo", Toast.LENGTH_SHORT).show();
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
    /*
    public void guardar(View view){
       try{
            OutpuntStebfir archivos = new Outout||Reader(
            archivo.
       }
    }
    */

}