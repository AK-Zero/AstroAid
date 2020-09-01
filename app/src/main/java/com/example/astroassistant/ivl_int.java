package com.example.astroassistant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ivl_int {

    @GET("/search")
    Call<ivl_response> getIVL(@Query("q") String query);

}
