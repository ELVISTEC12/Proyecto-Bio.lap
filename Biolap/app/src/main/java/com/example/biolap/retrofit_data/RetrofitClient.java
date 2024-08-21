package com.example.biolap.retrofit_data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit=null;
    public static final String URL_BASE ="";
    public static RetrofitApiService getApiService(){
        if(retrofit==null){
            retrofit= new Retrofit.Builder().baseUrl(URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(RetrofitApiService.class);
    }
}
