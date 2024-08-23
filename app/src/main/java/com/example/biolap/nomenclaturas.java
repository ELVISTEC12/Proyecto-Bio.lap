package com.example.biolap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.biolap.modelo.NomList;
import com.example.biolap.retrofit_data.RetrofitApiService;
import com.example.biolap.retrofit_data.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class nomenclaturas extends AppCompatActivity {
    //private RecyclerAdapter adapter;
    private RecyclerView rvNom;
    private RetrofitApiService retrofitApiService;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nomenclaturas);
        rvNom=findViewById(R.id.nomList);
        retrofitApiService = RetrofitClient.getApiService();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void getItemSQL(){
        retrofitApiService.getMostrarNom().enqueue(new Callback<List<NomList>>() {
            @Override
            public void onResponse(Call<List<NomList>> call, Response<List<NomList>> response) {

            }

            @Override
            public void onFailure(Call<List<NomList>> call, Throwable t) {

            }
        });
    }


}