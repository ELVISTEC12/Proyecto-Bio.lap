package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class formPacientes extends AppCompatActivity {
    EditText n, ob, ed, dni, fe, num, medi;
     boolean verificaded = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_pacientes);
        n= findViewById(R.id.n_p);//nombre paciente
        ed= findViewById(R.id.edad);//edad del paciente
        ob=findViewById(R.id.obra);//obra social del paciente
        num=findViewById(R.id.nu_p);//numero telefonico del paciente
        medi=findViewById(R.id.med);//medico del paciente
        fe=findViewById(R.id.fecha);
        dni=findViewById(R.id.dnipa);//dni del paciente


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void rutina(View view){
        String nombre=n.getText().toString();
        String obra=ob.getText().toString();
        String edad=ed.getText().toString();
        String numero=num.getText().toString();
        String medico= medi.getText().toString();
        String fecha=fe.getText().toString();
        String DNI=dni.getText().toString();
        if(TextUtils.isEmpty(nombre)){
          n.setError("Ingrese el nombre del paciente");
          verificaded = false;
        }
        if(TextUtils.isEmpty(obra)){
            ob.setError("Ingrese la obra social de "+nombre);
            verificaded=false;
        }
        if (TextUtils.isEmpty(edad)){
            ed.setError("Ingrese la edad de "+nombre);
            verificaded=false;
        }
        if (TextUtils.isEmpty(numero)){
            num.setError("Ingrese el número telefonico de "+nombre);
            verificaded=false;
        }
        if (TextUtils.isEmpty(medico)){
            medi.setError("Ingrese el medico de "+nombre);
            verificaded=false;
        }
        if (TextUtils.isEmpty(fecha)){
            fe.setError("Ingrese la fecha");
            verificaded=false;
        }
        if (TextUtils.isEmpty(DNI)){
            dni.setError("Ingrese el dni de "+nombre);
            verificaded=false;
        }




        /*Intent r = new Intent(this, analisisRutina.class);
        startActivity(r);*/
    }
}