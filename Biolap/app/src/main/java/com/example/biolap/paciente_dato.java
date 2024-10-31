package com.example.biolap;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biolap.modelo.NomLista;
import com.example.biolap.modelo.PacienteLista;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import android.os.Environment;
import java.io.File;

public class paciente_dato extends AppCompatActivity {
    private EditText dni;
    private EditText nombre;
    private EditText telefono;
    private TextView id;
    private EditText edad;
    private EditText medico;
    private EditText rutina;
    private EditText obra_social;
    private String idP,nombreP,telefonoP,edadP,medicoP,rutinaP,obraP,dniP;
    gestionarAnalisis ga = new gestionarAnalisis();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_paciente_dato);

        dni = findViewById(R.id.DNIPasienteE);
        nombre = findViewById(R.id.NombrePaciente);
        telefono = findViewById(R.id.telefonoPaciente);
        edad = findViewById(R.id.EdadPaciente);
        obra_social = findViewById(R.id.ObraSocialPaciente);
        medico = findViewById(R.id.MedicoPaciente);
        rutina = findViewById(R.id.RutinaPaciente);
        id = findViewById(R.id.idPacienteE);

        PacienteLista element = (PacienteLista) getIntent().getSerializableExtra("PacienteLista");
        if (element != null) {
            dni.setText(element.getDni());
            nombre.setText(element.getNombreP());
            telefono.setText(element.getTelefono());
            edad.setText(element.getEdad());
            obra_social.setText(element.getObra_social());
            medico.setText(element.getMedico());
            rutina.setText(element.getRutina());
            id.setText(element.getIdP());
        } else {
            Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void modificarP(View view){
        idP = id.getText().toString();
        nombreP = nombre.getText().toString();
        obraP = obra_social.getText().toString();
        dniP = dni.getText().toString();
        edadP = edad.getText().toString();
        telefonoP = telefono.getText().toString();
        medicoP = medico.getText().toString();
        rutinaP = rutina.getText().toString();
        boolean val = true;

        if(TextUtils.isEmpty(nombreP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(obraP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(dniP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(edadP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(telefonoP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(medicoP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }
        if(TextUtils.isEmpty(rutinaP)){
            nombre.setError("Campo obligatorio");
            val = false;
        }

        if(val){
            modDatos("http://192.168.0.129/bio.lap/modificar_paciente.php");
        }
    }
    public void eliminarP(View view){
        idP = id.getText().toString();
        nombreP = nombre.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro que deseas eliminar '" + nombreP + "' ?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliDatos("http://192.168.237.162/bio.lap/eliminar_paciente.php");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void eliDatos(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        ga.finish();
                        Toast.makeText(getApplicationContext(), "Se eliminó con éxito", Toast.LENGTH_SHORT).show();
                        Intent nom = new Intent(getApplicationContext(), gestionarAnalisis.class);
                        startActivity(nom);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error en la eliminacion", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> datos = new HashMap<String, String>();
                datos.put("id", idP);
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    private void modDatos(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        ga.finish();
                        Toast.makeText(getApplicationContext(), "Se modificó con éxito", Toast.LENGTH_SHORT).show();
                        Intent nom = new Intent(getApplicationContext(), gestionarAnalisis.class);
                        startActivity(nom);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error en la modificación", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> datos = new HashMap<String, String>();
                datos.put("id", idP);
                datos.put("nombre", nombreP);
                datos.put("obra_social", obraP);
                datos.put("dni", dniP);
                datos.put("edad", edadP);
                datos.put("telefono", telefonoP);
                datos.put("medico", medicoP);
                datos.put("rutina", rutinaP);
                return datos;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }


    public void generarPDF(View view) {
        try {
            // Crear el directorio en la carpeta de Descargas
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Pacientes");
            if (!pdfDir.exists() && !pdfDir.mkdirs()) {
                Log.e("PDF_DIR", "Failed to create directory");
                Toast.makeText(this, "No se pudo crear el directorio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Definir la ruta del archivo PDF
            String pdfPath = pdfDir + "/paciente_" + dniP + ".pdf";
            Log.d("PDF_PATH", "PDF path: " + pdfPath);

            // Crear el archivo PDF
            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Añadir los datos del paciente al PDF
            document.add(new Paragraph("Datos del Paciente:"));
            document.add(new Paragraph("DNI: " + dniP));
            document.add(new Paragraph("Nombre: " + nombreP));
            document.add(new Paragraph("Teléfono: " + telefonoP));
            document.add(new Paragraph("Edad: " + edadP));
            document.add(new Paragraph("Obra Social: " + obraP));
            document.add(new Paragraph("Médico: " + medicoP));
            document.add(new Paragraph("Rutina: " + rutinaP));

            // Cerrar el documento
            document.close();

            // Mostrar la ubicación donde se guardó el PDF
            Toast.makeText(this, "PDF generado en: " + pdfPath, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("PDF_ERROR", "Error al generar PDF", e);
            Toast.makeText(this, "Error al generar el PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}