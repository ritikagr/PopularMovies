package com.iitism.ritik.popularmovies;

import android.app.Application;

import com.android.volley.toolbox.StringRequest;

import io.realm.Realm;

/**
 * Created by ritik on 11/28/2016.
 */
public class PopularMovies extends Application{

    public static final String Key = "add API key here";
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "?api_key=" + Key;
    public static final String LANG_PAGE = "&language=en-US&page=";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
