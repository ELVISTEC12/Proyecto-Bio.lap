package com.example.biolap;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class perfilAjuste extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_ajuste);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void ventana() {
        // Crear un AlertDialog.Builder para construir la ventana de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Configurar el título del diálogo
        builder.setTitle("Ingresar Nombre de Usuario");

        // Crear un EditText para ingresar el nombre de usuario
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT); // Configurar el tipo de entrada como texto

        // Agregar el EditText al diálogo
        builder.setView(input);

        // Configurar el botón "Modificar" y su acción
        builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener el texto ingresado por el usuario
                String nombreUsuario = input.getText().toString();

                // Aquí puedes agregar la lógica para manejar el nombre de usuario ingresado
                // Por ejemplo, actualizar un campo de texto, enviar a una base de datos, etc.
                Toast.makeText(getApplicationContext(), "Nombre de usuario: " + nombreUsuario, Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar un botón "Cancelar" opcional si deseas
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel(); // Cerrar el diálogo sin realizar ninguna acción
            }
        });

        // Mostrar el diálogo
        builder.show();
    }

}