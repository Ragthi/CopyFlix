package com.example.copyflix.ui.home;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.copyflix.Mysingleton;
import com.example.copyflix.R;
import com.example.copyflix.databinding.FragmentHomeBinding;
import com.example.copyflix.ui.Movieadapter;
import com.example.copyflix.ui.moviedata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
private FragmentHomeBinding binding;
//ClickListiner listiner;
    Movieadapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

    binding = FragmentHomeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();


        RecyclerView recyclerView = (RecyclerView) binding.homeRecycler;


        adapter =new Movieadapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getdata();

        return root;
    }

    private void getdata() {
        String url = "https://api.tvmaze.com/search/shows?q=all";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    ArrayList<moviedata> moviearray = new ArrayList<>();

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(response.length()==0){
                                Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
                            }
                            for (int i=0;i<response.length();i++){
                                String name = response.getJSONObject(i).getJSONObject("show").getString("name");
                                String summary = response.getJSONObject(i).getJSONObject("show").getString("summary");
                                summary = summary.replace("<p>","");
                                summary = summary.replace("<b>","");
                                summary = summary.replace("</b>","");
                                summary = summary.replace("</p>","");

                                String photo_url="https://st4.depositphotos.com/14953852/24787/v/600/depositphotos_247872612-stock-illustration-no-image-available-icon-vector.jpg";
                                JSONObject images = response.getJSONObject(i).getJSONObject("show");
                                if (images.has("image") && !images.isNull("image")) {
                                    Log.d("link","pass");
                                    photo_url = images.getJSONObject("image").getString("medium");
                                }

                                moviedata temp = new moviedata(
                                        name,
                                        summary,
                                        photo_url
                                );

                                moviearray.add(temp);
                            }
                            adapter.updatemovie(moviearray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();

                    }
                });

        Mysingleton.getInstance(this.getContext()).addToRequestQueue(jsonObjectRequest);

        //volly and then api call to get data in list of moviedata then call updated function of adapter class
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}