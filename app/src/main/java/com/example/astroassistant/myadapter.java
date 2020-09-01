package com.example.astroassistant;

import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder> {

    List<String> titles;
    List<String> descriptions;
    List<String> nasa_ids;
    Context mcontext;

    public myadapter(Context context, List<String> titles1,
                     List<String> descriptions1,
                     List<String> nasa_ids1) {

        titles = new ArrayList<>(titles1);
        descriptions = new ArrayList<>(descriptions1);
        nasa_ids = new ArrayList<>(nasa_ids1);
        mcontext = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item, parent, false);
        return new myviewholder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, final int position) {
        holder.title.setText(titles.get(position));
        holder.desc.setText(descriptions.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, DisplayPage.class);
                intent.putExtra("id", nasa_ids.get(position));
                intent.putExtra("title", titles.get(position));
                intent.putExtra("desc", descriptions.get(position));
                mcontext.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView title, desc;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
        }
    }

}
