package com.example.movielibrary.provider;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.example.movielibrary.provider.Movie.TABLE_NAME;
import java.io.Serializable;


@Entity(tableName = TABLE_NAME)
public class Movie {

    public static final String TABLE_NAME = "movie";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @NonNull

    @ColumnInfo(name=COLUMN_ID)
    private int id;

    public static final String COLUMN_Title = "MovieTitle";
    @ColumnInfo(name=COLUMN_Title)
    private String title;

    public static final String COLUMN_Year = "MovieYear";
    @ColumnInfo(name= COLUMN_Year)
    private int year;

    public static final String COLUMN_Country = "MovieCountry";
    @ColumnInfo(name=COLUMN_Country)
    private String country;

    public static final String COLUMN_Genre = "MovieGenre";
    @ColumnInfo(name=COLUMN_Genre)
    private String genre;

    public static final String COLUMN_Keyword = "MovieKeyword";
    @ColumnInfo(name=COLUMN_Keyword)
    private String keyword;

    public static final String COLUMN_Cost = "MovieCost";
    @ColumnInfo(name=COLUMN_Cost)
    private int cost;


    public Movie(String title, int year, String country, String genre, String keyword, int cost) {
        this.title = title;
        this.year = year;
        this.country = country;
        this.genre = genre;
        this.keyword = keyword;
        this.cost = cost;
    }
    public void setId(@NonNull int id){this.id= id;}
    public void setTitle(String title) {
        this.title = title;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }

    public Movie() { }
    public String getTitle(){
        return title;
    }
    public String getCountry(){
        return country;
    }
    public String getGenre(){
        return genre;
    }
    public String getKeyword(){
        return keyword;
    }
    public int getYear(){
        return year;
    }
    public int getCost(){
        return cost;
    }
    public int getId(){ return id;}

    @NonNull
    public static Movie fromContentValues(@Nullable ContentValues values) {
        final Movie movie = new Movie();
        if (values != null && values.containsKey(COLUMN_ID)){
            movie.id = values.getAsInteger(COLUMN_ID);
        }
        if (values != null && values.containsKey(COLUMN_Title)){
            movie.title = values.getAsString(COLUMN_Title);
        }
        if (values != null && values.containsKey(COLUMN_Year)) {
            movie.year = values.getAsInteger(COLUMN_Year);
        }
        if (values != null && values.containsKey(COLUMN_Country)) {
            movie.country = values.getAsString(COLUMN_Country);
        }
        if (values != null && values.containsKey(COLUMN_Genre)) {
            movie.genre = values.getAsString(COLUMN_Genre);
        }
        if (values != null && values.containsKey(COLUMN_Keyword)) {
            movie.keyword = values.getAsString(COLUMN_Keyword);
        }
        return movie;
    }
}


