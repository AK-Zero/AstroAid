package com.example.astroassistant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apod_int {

    @GET("apod?api_key=jky3wdScMMhRmCDAqqRopLFKoM3ARCIPtrPLpy5z&hd=True")
    Call<apod_response> getAPOD(@Query("date") String date);

}
