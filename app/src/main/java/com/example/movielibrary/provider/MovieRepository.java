package com.example.movielibrary.provider;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    private MovieDao movieDao;
    private LiveData<List<Movie>> AllMovie;

    MovieRepository(Application application){
        MovieDatabase db = MovieDatabase.getDatabase(application);
        movieDao = db.movieDao();
        AllMovie = movieDao.getAllMovies();
    }

    // function all from MovieDAO
    public LiveData<List<Movie>> getAllMovie(){
        return AllMovie;
    }
    void insert(Movie movie){
        MovieDatabase.databaseWriteExecutor.execute(()->movieDao.addMovie(movie));
        // java lambda
    }
    void deleteAll(){
        MovieDatabase.databaseWriteExecutor.execute(()->{movieDao.deleteAllMovie();});
    }
    void deleteLastMovie(){
        MovieDatabase.databaseWriteExecutor.execute(() -> {movieDao.deleteLastMovie();});
    }

    void deleteEXP(){
        MovieDatabase.databaseWriteExecutor.execute(() -> {movieDao.deleteExpensive();});
    }

    public LiveData<List<Movie>> selectGenre(String genre){
        return movieDao.selectionGenre(genre);
    }

    public LiveData<List<Movie>> selectYear(int minYear, int maxYear){
        return movieDao.selectionYear(minYear, maxYear);
    }
}
