package com.example.copyflix.ui;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.copyflix.R;

public class viewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView summary;
    ImageView photo;
    View view;

    viewHolder(View itemView)
    {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.movie_name);
        summary = (TextView)itemView.findViewById(R.id.movie_summary);
        photo = (ImageView) itemView.findViewById(R.id.movie_photo);
        view = itemView;
    }
}
