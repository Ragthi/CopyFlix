package com.example.copyflix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.copyflix.ui.moviedata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class detailsMovie extends AppCompatActivity {
    ImageView mphotu,mredirectline;
    TextView mname,mgenere,mlength,mlanguage,mrating,msummary;
    String reurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        mphotu = (ImageView) findViewById(R.id.detail_photo);
        mredirectline = findViewById(R.id.redirect_link);
        mname = findViewById(R.id.movie_name);
        mgenere = findViewById(R.id.movie_genre);
        mlength = findViewById(R.id.movie_length);
        mlanguage = findViewById(R.id.movie_language);
        mrating = findViewById(R.id.movie_rating);
        msummary = findViewById(R.id.movie_sum);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name");
            String summary = extras.getString("summary");
            String photo = extras.getString("photo");
            mname.setText(name);
            msummary.setText(summary);
            Log.d("link",photo);
            Glide.with(detailsMovie.this).load(photo).placeholder(R.drawable.logo).into(mphotu);
            getremaningdata(name);

        }
        mredirectline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(reurl));
                startActivity(i);
            }
        });

    }


    private void getremaningdata(String name) {

        String url = "https://api.tvmaze.com/search/shows?q="+name;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    ArrayList<moviedata> moviearray = new ArrayList<>();

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject shows = response.getJSONObject(0).getJSONObject("show");
                            mlanguage.setText(shows.getString("language"));
                                if (shows.has("runtime") && !shows.isNull("runtime")) {
                                    mlength.setText(shows.getString("runtime"));
                                }else{
                                    mlength.setText("NA");
                                }
                            if (shows.getJSONObject("rating").has("average") && !shows.getJSONObject("rating").isNull("average")) {
                                mrating.setText(shows.getJSONObject("rating").getString("average")+"/10");
                            }else{
                                mlength.setText("NA");
                            }
                            JSONArray genere = shows.getJSONArray("genres");
                            String gene = genere.getString(0);
                            for (int j=1;j<genere.length();j++){
                                   gene+=" | "+genere.getString(j);
                            }
                            mgenere.setText(gene);
                            reurl = shows.getString("url");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(detailsMovie.this, "Network error", Toast.LENGTH_SHORT).show();

                    }
                });

        Mysingleton.getInstance(detailsMovie.this).addToRequestQueue(jsonObjectRequest);



    }
}