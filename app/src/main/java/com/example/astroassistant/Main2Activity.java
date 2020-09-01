package com.example.astroassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {

    List<String> titles = new ArrayList<>();
    List<String> descriptions = new ArrayList<>();
    List<String> nasa_ids = new ArrayList<>();
    RecyclerView rlist;
    myadapter adapter;
    LottieAnimationView loader;
    TextView message;
    Call<ivl_response> call;
    int stat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        message = findViewById(R.id.message);
        rlist = findViewById(R.id.rlist);
        rlist.setLayoutManager(new LinearLayoutManager(this));

        loader = findViewById(R.id.loader1);

        Toast t = Toast.makeText(Main2Activity.this , "Start Searching...." , Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()){
                    getItems(newText);
                    loader.setVisibility(View.VISIBLE);
                    loader.playAnimation();
                }
                else{
                    message.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        return true;
    }

    private void getItems(String query){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://images-api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ivl_int inter = retrofit.create(ivl_int.class);
        if(stat!=0) {
            call.cancel();
        }
        call = inter.getIVL(query);
        stat++;
        call.enqueue(new Callback<ivl_response>() {
            @Override
            public void onResponse(Call<ivl_response> call, Response<ivl_response> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Main2Activity.this, "FailureError", Toast.LENGTH_LONG).show();
                    return;
                }
                message.setVisibility(View.GONE);
                ivl_response obj = response.body();
                titles.clear();
                descriptions.clear();
                nasa_ids.clear();
                assert obj != null;
                for(int i = 0; i<obj.getCollectio().getItem().size() ; i++){
                    titles.add(obj.getCollectio().getItem().get(i).getData().get(0).getTitle());
                    String des = obj.getCollectio().getItem().get(i).getData().get(0).getDescription();
                    if(des!=null) {
                        descriptions.add(des);
                    }
                    else{
                        descriptions.add("No Description Available!!");
                    }
                    nasa_ids.add(obj.getCollectio().getItem().get(i).getData().get(0).getNasa_id());
                }
                adapter = new myadapter(Main2Activity.this , titles , descriptions , nasa_ids);
                rlist.setAdapter(adapter);
                if(adapter.getItemCount()==0){
                    message.setVisibility(View.VISIBLE);
                }
                loader.cancelAnimation();
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ivl_response> call, Throwable t) {

            }
        });
    }
}
