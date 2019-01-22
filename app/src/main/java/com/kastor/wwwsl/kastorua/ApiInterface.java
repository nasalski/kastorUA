package com.kastor.wwwsl.kastorua;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/ru/api/get-categs/")
    Call<List<Category>> getCategory();
    // возвращаем все серии конкретного раздела
    @GET("/ru/api/get-cat-series")
    Call<List<Series>> getSeries(@Query("id") int id);
    // возвращаем конкретную серию
    @GET("/ru/api/get-serie")
    Call<List<Series>> getSerie(@Query("id") int id);
    //возвращаем колеса конкретной серии
    @GET("/ru/api/get-serie-wheels")
    Call<List<Weel>> getSerieWeels(@Query("id") int id);
}
