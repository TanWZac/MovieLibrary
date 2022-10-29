package com.example.movielibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movielibrary.provider.Movie;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private List<Movie> movies;

    public MyRecyclerAdapter(){ }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false); //CardView inflated as RecyclerView list item
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cardYear.setText(String.valueOf(movies.get(position).getYear()));
        holder.cardTitle.setText(movies.get(position).getTitle());
        holder.cardKeyword.setText("Keyword: " + movies.get(position).getKeyword());
        holder.cardGenre.setText("Genre: "+movies.get(position).getGenre());
        holder.cardCost.setText("$" + String.valueOf(movies.get(position).getCost()));
        holder.cardCountry.setText("Country: "+movies.get(position).getCountry());
    }

    @Override
    public int getItemCount() {
        if (movies == null){
            return 0;
        }
        else{
            return movies.size();
        }
    }
    public void setMovies(List<Movie> mov) {
        movies = mov;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardYear;
        public TextView cardTitle;
        public TextView cardCost;
        public TextView cardKeyword;
        public TextView cardCountry;
        public TextView cardGenre;
        public CardView cv;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardYear = itemView.findViewById(R.id.card_year);
            cardTitle = itemView.findViewById(R.id.card_title);
            cardCost = itemView.findViewById(R.id.card_cost);
            cardCountry = itemView.findViewById(R.id.card_country);
            cardGenre = itemView.findViewById(R.id.card_genre);
            cardKeyword = itemView.findViewById(R.id.card_keyword);
            cv = itemView.findViewById(R.id.card_view);
        }
    }
}