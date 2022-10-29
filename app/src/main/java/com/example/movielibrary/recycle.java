package com.example.movielibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.movielibrary.provider.Movie;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import com.example.movielibrary.provider.MovieDatabase;
import com.example.movielibrary.provider.MovieViewModel;
import java.util.ArrayList;
import java.util.List;

public class recycle extends AppCompatActivity{

    RecyclerView recyclerView;
    MyRecyclerAdapter adapter;

    private MovieViewModel movieViewModel;
    int x,y=0;
    String val;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view);

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new MyRecyclerAdapter();

        Intent intent = getIntent();
        if (intent.hasExtra("min")){
            Bundle extras = intent.getExtras();
            x = Integer.parseInt(extras.getString("min"));
            y = Integer.parseInt(extras.getString("max"));
        }
        else {
            val = intent.getStringExtra("intVar");
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        if (val!= null && x == 0 && y == 0 ){
            movieViewModel.filterGenre(val).observe(this, newData ->{
                adapter.setMovies(newData);
                adapter.notifyDataSetChanged();
            });
        }
        else if (x>0&&y>0){
            movieViewModel.filterYear(x,y).observe(this, newData->{
                adapter.setMovies(newData);
                adapter.notifyDataSetChanged();
            });
        }
        else{
            movieViewModel.getAllMovie().observe(this, newData -> {
                adapter.setMovies(newData);
                adapter.notifyDataSetChanged();
            });
        }
    }
}
