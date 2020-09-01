package com.example.astroassistant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface display_int {

    @GET("/asset/{nasa_id}")
    Call<display_obj> getdisplay_img(@Path("nasa_id") String id);

}
