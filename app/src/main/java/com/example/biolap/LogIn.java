package com.example.biolap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LogIn extends AppCompatActivity {
    EditText usuarioTXT;
    EditText contraTXT;
    Button ing, re;
    FrameLayout errorContainer;
    View overlay;
    ImageView errorImage;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificar el estado de inicio de sesión
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false);

        if (isUserLoggedIn) {
            // Usuario ya está registrado/iniciado sesión, redirigir a menuPrincipal
            Intent intent = new Intent(this, menuPrincipal.class);
            startActivity(intent);
            finish();
            return;
        }

        usuarioTXT = findViewById(R.id.usuarioTXT);
        contraTXT = findViewById(R.id.contrasenaTXT);
        ing = findViewById(R.id.boton_ingresar);
        re=findViewById(R.id.boton_registrar);
        errorContainer = findViewById(R.id.errorContainer);
        overlay = findViewById(R.id.overlay);
        errorImage = findViewById(R.id.errorImage);
        exitButton = findViewById(R.id.exitButton);

        // Configurar botón de ingresar
        ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtil.isNetworkAvailable(LogIn.this)) {
                    // Mostrar imagen de error y botón de salir
                    errorContainer.setVisibility(View.VISIBLE);
                    overlay.setVisibility(View.VISIBLE); // Mostrar el overlay semitransparente

                    exitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish(); // Cierra la actividad
                        }
                    });
                } else {
                    //ingresar("https://192.168.1.2/bio.lap/validar_usuario.php");
                    saltar();

                }
            }
        });
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtil.isNetworkAvailable(LogIn.this)) {
                    // Mostrar imagen de error y botón de salir
                    errorContainer.setVisibility(View.VISIBLE);
                    overlay.setVisibility(View.VISIBLE); // Mostrar el overlay semitransparente

                    exitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish(); // Cierra la actividad
                        }
                    });
                } else {
                    registrar();

                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void ingresar(final String URL) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    String params = "nombre=" + URLEncoder.encode(usuarioTXT.getText().toString(), "UTF-8")
                            + "&contra=" + URLEncoder.encode(contraTXT.getText().toString(), "UTF-8");

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                    writer.write(params);
                    writer.flush();
                    writer.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                if (response != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Toast.makeText(getApplicationContext(), "Operación exitosa", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), menuPrincipal.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public void registrar() {
        Intent r = new Intent(this, registrarUsuario.class);
        startActivity(r);
    }
    public void saltar() {
        Intent r = new Intent(this, menuPrincipal.class);
        startActivity(r);
    }
}
