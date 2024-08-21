package com.example.biolap.retrofit_data;



import com.example.biolap.modelo.NomList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApiService {
    @GET("getMostrarNom.php")
    Call<List<NomList>> getMostrarNom();
}
