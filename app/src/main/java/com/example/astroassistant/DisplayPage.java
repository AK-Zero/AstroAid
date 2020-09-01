package com.example.astroassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class DisplayPage extends AppCompatActivity {

    TextView title , description;
    ImageView disp;
    LottieAnimationView loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);

        getSupportActionBar().hide();
        Intent intent = getIntent();

        final String title_txt = intent.getStringExtra("title");
        final String desc = intent.getStringExtra("desc");
        String nasa_id = intent.getStringExtra("id");

        title = findViewById(R.id.title2);
        description = findViewById(R.id.description);
        disp = findViewById(R.id.disp_img);
        loader = findViewById(R.id.loader3);
        title.setMovementMethod(new ScrollingMovementMethod());
        description.setMovementMethod(new ScrollingMovementMethod());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://images-api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        display_int inter = retrofit.create(display_int.class);
        Call<display_obj> call = inter.getdisplay_img(nasa_id);

        call.enqueue(new Callback<display_obj>() {
            @Override
            public void onResponse(Call<display_obj> call, Response<display_obj> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(DisplayPage.this, "FailureError", Toast.LENGTH_LONG).show();
                    return;
                }
                display_obj obj = response.body();
                int i = 0;
                for(;i<obj.getCollectio().getItem().size() ; i++ ){
                    String x = obj.getCollectio().getItem().get(i).getHref();
                    if(x.endsWith("medium.jpg")){
                        disp.setVisibility(View.VISIBLE);
                        Picasso.with(DisplayPage.this).load(x.replace("http" , "https")).into(disp);
                        break;
                    }
                }
                if(disp.getVisibility()==View.INVISIBLE){
                    for(i=0;i<obj.getCollectio().getItem().size() ; i++ ){
                        String x = obj.getCollectio().getItem().get(i).getHref();
                        if(x.endsWith("small.jpg")){
                            disp.setVisibility(View.VISIBLE);
                            Picasso.with(DisplayPage.this).load(x.replace("http" , "https")).into(disp);
                            break;
                        }
                    }
                }
                if(disp.getVisibility()==View.INVISIBLE) {
                    Toast.makeText(DisplayPage.this , "No image exists for this data entry!!!" , Toast.LENGTH_LONG).show();
                }

                title.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                title.setText(title_txt);
                description.setText(desc);
                loader.cancelAnimation();
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<display_obj> call, Throwable t) {
                Toast.makeText(DisplayPage.this, "Internet Connection Weak or Unavailable! Reason : " + t.toString(), Toast.LENGTH_LONG).show();
                loader.cancelAnimation();
                loader.setVisibility(View.GONE);
            }
        });


    }
}
