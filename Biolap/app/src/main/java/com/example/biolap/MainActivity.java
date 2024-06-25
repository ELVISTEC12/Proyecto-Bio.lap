package com.example.biolap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText usuarioTXT;
    private EditText contraTXT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        usuarioTXT = findViewById(R.id.usuarioTxt);
        contraTXT = findViewById(R.id.contrasenaTxt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void ingresar(View view){
        String v1 = usuarioTXT.getText().toString();
        String v2 = contraTXT.getText().toString();
        if(v1.equals("Pablo") && v2.equals("riverplate")){
            Intent m = new Intent(this,menup.class);
            startActivity(m);
            usuarioTXT.setText("");
            contraTXT.setText("");
        }
        else{
            Toast.makeText(this, "Los datos no coinciden", Toast.LENGTH_SHORT).show();
        }
    }
    public void registrar(View view){
        Intent r = new Intent(this,registrarUsuario.class);
        startActivity(r);
    }
}