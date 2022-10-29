package com.example.movielibrary.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("select * from movie")
    LiveData<List<Movie>> getAllMovies();

    @Insert
    void addMovie(Movie movie);

//    @Query("delete from movie where MovieTitle= :title")
//    void deleteMovie(String title);

    @Query("delete from movie where _id = (select MAX(_id) FROM movie)")
    void deleteLastMovie();

    @Query("delete FROM movie")
    void deleteAllMovie();

    @Query("delete from movie where MovieCost = (select MAX(MovieCost) FROM movie)")
    void deleteExpensive();

    @Query("select * from movie where MovieGenre like :genre")
    LiveData<List<Movie>> selectionGenre(String genre);

    @Query("select * from movie where MovieYear between :yearMin and :yearMax")
    LiveData<List<Movie>> selectionYear(int yearMin, int yearMax);

    @Query("select * from movie where MovieYear between :costMin and :costMax")
    LiveData<List<Movie>> selectionCost(int costMin, int costMax);
}
