package com.iitism.ritik.popularmovies;

/**
 * Created by Ritik on 26-09-2016.
 */
public class Movie {
    public String posterPath;
    public String title;
    public String overView;
    public String releaseDate;
    public String popularity;
    public String voteAvg;
    public String id;
    public Movie()
    {

    }

    public Movie(String posterPath,String title,String overview,String releaseDate,String popularity,String voteAvg,String id)
    {
        this.posterPath=posterPath;
        this.title = title;
        this.overView = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAvg = voteAvg;
        this.id = id;
    }
}
