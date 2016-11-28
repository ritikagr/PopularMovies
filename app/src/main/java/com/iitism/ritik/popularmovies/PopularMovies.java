package com.iitism.ritik.popularmovies;

import android.app.Application;

/**
 * Created by ritik on 11/28/2016.
 */
public class PopularMovies extends Application{

    public static final String Key = "paste your api key here";
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "?api_key=" + Key;
}
