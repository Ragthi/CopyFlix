package com.example.copyflix.ui;

public class moviedata {
    String movie_name;
    String movie_summary;
    String image_url;

    public moviedata(String name,
             String summary,
             String url)
    {
        this.movie_name = name;
        this.movie_summary = summary;
        this.image_url = url;
    }
}
