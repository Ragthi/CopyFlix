package com.example.copyflix.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.example.copyflix.bottom_navigation;
import com.example.copyflix.databinding.FragmentDashboardBinding;
import com.example.copyflix.ui.Movieadapter;
import com.example.copyflix.ui.moviedata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
private FragmentDashboardBinding binding;
    Movieadapter adapter;
    EditText searchmovie;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

    binding = FragmentDashboardBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        bottom_navigation csActivity;
        csActivity      = (bottom_navigation) getActivity();
        csActivity.getSupportActionBar().hide();

        searchmovie = binding.searchMovie;
        RecyclerView recyclerView = (RecyclerView) binding.dashboardRecycler;

        adapter =new Movieadapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        searchmovie.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    closekeyboard();
                    getdata();

                    return true;

                }
                return false;
            }
        });



        return root;
    }

    private void closekeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void getdata() {
        String url = "https://api.tvmaze.com/search/shows?q="+searchmovie.getText().toString();

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