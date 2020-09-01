package com.example.astroassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button datepicker;
    ImageView img_apod;
    YouTubePlayerFragment youtubeFragment;
    TextView explanation , title_tv;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    YouTubePlayerSupportFragment frag;
    FrameLayout frameLayout;
    String titletxt , exp , type , url , date;
    LottieAnimationView loader ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        loader = findViewById(R.id.loader);
        datepicker = findViewById(R.id.datepicker);
        img_apod = findViewById(R.id.apod_img);
        youtubeFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.youtubeFragment);
        explanation = findViewById(R.id.explanation);
        explanation.setMovementMethod(new ScrollingMovementMethod());
        title_tv = findViewById(R.id.title);
        title_tv.setMovementMethod(new ScrollingMovementMethod());
        frameLayout = findViewById(R.id.frameLayout);
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager() , "datepicker");
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR , year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH , dayOfMonth);
        if(c.getTimeInMillis()<Calendar.getInstance().getTimeInMillis()) {
            contentsetter(c);
            youtubeFragment.onDestroy();
            youtubeFragment.onCreate(null);
        }
        loader.setVisibility(View.VISIBLE);
        loader.playAnimation();
    }

    public void contentsetter(Calendar c){

        date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/planetary/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apod_int inter = retrofit.create(apod_int.class);
        Call<apod_response> call = inter.getAPOD(date);
        call.enqueue(new Callback<apod_response>() {
            @Override
            public void onResponse(Call<apod_response> call, Response<apod_response> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "FailureError", Toast.LENGTH_LONG).show();
                    return;
                }

                apod_response obj = response.body();
                 titletxt = obj.getTitle();
                 exp = obj.getExplanation();
                 type = obj.getMedia_type();
                 url = obj.getUrl();
                title_tv.setVisibility(View.VISIBLE);
                explanation.setVisibility(View.VISIBLE);
                title_tv.setText(titletxt);
                explanation.setText(exp);
                SharedPreferences pref = getSharedPreferences("ABC" , MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("title" , titletxt);
                edit.putString("type" , type);
                edit.putString("exp" , exp);
                edit.putString("url" , url);
                edit.putString("date" , date);
                edit.apply();
                onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(url.substring(30,41));
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                };
                if(type.equals("image")){
                    frameLayout.setVisibility(View.INVISIBLE);
                    img_apod.setVisibility(View.VISIBLE);
                    Picasso.with(MainActivity.this).load(url).into(img_apod);
                }
                else if(type.equals("video")){
                    frameLayout.setVisibility(View.VISIBLE);
                    img_apod.setVisibility(View.INVISIBLE);
                    youtubeFragment.initialize(YouTubeConfig.getApiKey() , onInitializedListener);
                }
                loader.cancelAnimation();
                loader.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<apod_response> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Internet Connection Weak or Unavailable! Reason : " + t.toString(), Toast.LENGTH_LONG).show();
                loader.cancelAnimation();
                loader.setVisibility(View.GONE);
            }
        });

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SharedPreferences pref = getSharedPreferences("ABC" , MODE_PRIVATE);
        titletxt = pref.getString("title","Temp");
        exp = pref.getString("exp","Temp");
        type = pref.getString("type","Temp");
        url = pref.getString("url","Temp");
        date = pref.getString("date","Temp");
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(url.substring(30,41));
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        if(type!=null) {
            if (type.equals("image")) {
                frameLayout.setVisibility(View.INVISIBLE);
                img_apod.setVisibility(View.VISIBLE);
                Picasso.with(MainActivity.this).load(url).into(img_apod);
            } else if (type.equals("video")) {
                frameLayout.setVisibility(View.VISIBLE);
                img_apod.setVisibility(View.INVISIBLE);
                youtubeFragment.initialize(YouTubeConfig.getApiKey(), onInitializedListener);
            }
        }
        title_tv.setText(titletxt);
        explanation.setText(exp);
        if(titletxt!=null && !titletxt.isEmpty()){
            title_tv.setVisibility(View.VISIBLE);
            explanation.setVisibility(View.VISIBLE);
        }
    }
}
