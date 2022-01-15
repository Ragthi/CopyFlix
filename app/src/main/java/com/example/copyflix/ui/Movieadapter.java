package com.example.copyflix.ui;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.copyflix.R;
import com.example.copyflix.detailsMovie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Movieadapter extends RecyclerView.Adapter<viewHolder> {

    ArrayList<moviedata> list = new ArrayList<>();
    Context context;
    //ClickListiner listiner;


    public Movieadapter(Context context)
    {

        this.context = context;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType)
    {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.item_recycler, parent, false);

        viewHolder viewHolder = new viewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final viewHolder viewHolder, final int position)
    {

        String name = list.get(position).movie_name;
        String summ = list.get(position).movie_summary;
        String image = list.get(position).image_url;
        viewHolder.name.setText(name);
        viewHolder.summary.setText(summ);
        Glide.with(viewHolder.itemView.getContext()).load(image).centerCrop().placeholder(R.drawable.logo).into(viewHolder.photo);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent todetails = new Intent(view.getContext(), detailsMovie.class);
                todetails.putExtra("name",name);
                todetails.putExtra("summary",summ);
                todetails.putExtra("photo",image);
                context.startActivity(todetails);
            }
        });
    }

    public void updatemovie(List<moviedata> updated){
        list.clear();
        list.addAll(updated);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
