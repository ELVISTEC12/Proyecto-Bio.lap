package com.example.biolap;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.MediaStore;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
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
import androidx.core.content.FileProvider;
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

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.itextpdf.io.IOException;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

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
<<<<<<< HEAD
            modDatos("http://192.168.74.162/bio.lap/modificar_paciente.php");
=======
            modDatos("http://192.168.1.11/bio.lap/modificar_paciente.php");
>>>>>>> 6d4204bfd60272dbb0ab8d7974c24e814561e8ba
        }
    }
    public void eliminarP(View view){
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
        idP = id.getText().toString();
        nombreP = nombre.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro que deseas eliminar '" + nombreP + "' ?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
<<<<<<< HEAD
                        eliDatos("http://192.168.0.108/bio.lap/eliminar_paciente.php");
=======
                        eliDatos("http://192.168.1.11/bio.lap/eliminar_paciente.php");
>>>>>>> 6d4204bfd60272dbb0ab8d7974c24e814561e8ba
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
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
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
        if (!isConnectedToInternet()) {
            // Si no hay conexión a Internet
            Toast.makeText(this, "Por favor, conéctese a Internet", Toast.LENGTH_SHORT).show();
            return; // Salir para no enviar la solicitud
        }
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
        // Capturar los datos del paciente
        String nombreP = nombre.getText().toString();
        String obraP = obra_social.getText().toString();
        String dniP = dni.getText().toString();
        String edadP = edad.getText().toString();
        //String telefonoP = telefono.getText().toString();
        String medicoP = medico.getText().toString();
        String rutinaP = rutina.getText().toString();

        // Obtener la fecha actual
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        try {
            Uri pdfUri;
            OutputStream outputStream;
            int i=0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Usar MediaStore para Android 10 y superior (API 29+)
                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DISPLAY_NAME, "paciente_" + dniP +(i)+".pdf");
                values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                pdfUri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (pdfUri == null) {
                    throw new IOException("No se pudo crear el archivo PDF");
                }
                outputStream = getContentResolver().openOutputStream(pdfUri);
            } else {
                // Para Android 9 y versiones anteriores
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "paciente_" + dniP +(i)+".pdf");
                pdfUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", pdfFile);
                outputStream = new FileOutputStream(pdfFile);
            }

            // Crear y escribir el PDF
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.microscopio); // Asegúrate de que "microscopio" sea el nombre correcto
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapData = stream.toByteArray();
            ImageData imageData = ImageDataFactory.create(bitmapData);
            com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);

            // Ajustar el tamaño de la imagen (opcional)
            image.setWidth(50); // Cambia el tamaño según tus necesidades
            image.setHeight(50);

            // Agregar la imagen al documento
            document.add(image);
            // Encabezado del documento
            document.add(new Paragraph("CENTRO DE ESPECIALIDADES MEDICAS \"SAN GREGORIO\"").setBold().setFontSize(12));
            document.add(new Paragraph("LABORATORIO DE ANALISIS CLINICOS").setFontSize(10));
            document.add(new Paragraph("BIOQUIMICA: FABIOLA PANTOJA - M.P 311\nPEDRO GOYENA Nº 33    TELEFONO: 3888 - 426915    SAN PEDRO DE JUJUY").setFontSize(10));
            document.add(new Paragraph("\n"));

            // Información del paciente y médico
            document.add(new Paragraph("PACIENTE: " + nombreP + "\nDR/A: " + medicoP + "\nD.N.I: " + dniP + "    EDAD: " + edadP + "    FECHA DE ANALISIS: " + fechaActual + "\n").setFontSize(10));

            // Línea separadora
            document.add(new Paragraph("______________________________________________________________").setTextAlignment(TextAlignment.CENTER));

            // Resultados de hemoglobina glicosilada
           /* document.add(new Paragraph("\nDETERMINACION DE HEMOGLOBINA GLICOSILADA:\n").setBold().setFontSize(10));
            document.add(new Paragraph("RESULTADO: ........................................: 6.03 %").setFontSize(10));
            document.add(new Paragraph("METODO: INMUNOTURBIDIMETRIA AUTOMATIZADO").setFontSize(10));
            document.add(new Paragraph("VALOR DE REFERENCIA: NIVELES NORMALES, NO DIABETICOS:\n"
                    + "    EXCELENTE CONTROL DE DIABETES ............... 4 a 6 %\n"
                    + "    BUEN CONTROL DE DIABETES .................... 6.5 a 7.5 %\n"
                    + "    FALLA EN EL CONTROL DE DIABETES ............. > a 7.5 %\n"
                    + "    NIVELES DE HIPOGLUCEMIA ..................... < a 4.0 %").setFontSize(10));*/

            // Línea separadora
            //document.add(new Paragraph("______________________________________________________________").setTextAlignment(TextAlignment.CENTER));

            // Química clínica y otros detalles
           // document.add(new Paragraph("\nQUIMICA CLINICA\n").setBold().setFontSize(10));
            //document.add(new Paragraph("GLUCOSA: 93 mg/dl                   VR: 70-110").setFontSize(10));

            // Rutina
            document.add(new Paragraph("\nRUTINA: " + rutinaP).setFontSize(10));

            // Cerrar el documento
            document.close();
            outputStream.close();

            Toast.makeText(this, "PDF generado correctamente en la carpeta de Descargas", Toast.LENGTH_SHORT).show();

            // Abrir el archivo PDF automáticamente
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (Exception e) {
            Log.e("PDF_ERROR", "Error al generar PDF", e);
            Toast.makeText(this, "Error al generar el PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

}