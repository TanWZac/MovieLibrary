package com.example.movielibrary.provider;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private LiveData<List<Movie>> AllMovies, Filtering;
//    private String genre;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        AllMovies = movieRepository.getAllMovie();
    }
    
    // All taken from movie repo
    public LiveData<List<Movie>> getAllMovie(){
        return AllMovies;
    }
    public void insert(Movie movie) {
        movieRepository.insert(movie);
    }
    public void deleteAll(){
        movieRepository.deleteAll();
    }
    public void deleteLastMovie() { movieRepository.deleteLastMovie();}
    public void deleteExp() {movieRepository.deleteEXP();}
    public LiveData<List<Movie>> filterGenre(String genre) {
        return  movieRepository.selectGenre(genre);
    }
    public LiveData<List<Movie>> filterYear(int minYear, int maxYear) {
        return movieRepository.selectYear(minYear, maxYear);
    }

}
