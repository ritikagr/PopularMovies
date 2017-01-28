package com.iitism.ritik.popularmovies;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Ritik on 26-09-2016.
 */

public class Movie extends RealmObject{
    private String posterPath;
    private String title;
    private String overView;
    private String releaseDate;
    private String popularity;
    private String voteAvg;
    private String id;
    private RealmList<Trailer> trailerList;
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

    public Movie(String posterPath,String title,String overview,String releaseDate,String popularity,String voteAvg,String id,RealmList<Trailer> tList)
    {
        this.posterPath=posterPath;
        this.title = title;
        this.overView = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAvg = voteAvg;
        this.id = id;
        this.trailerList = tList;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(RealmList<Trailer> trailerList) {
        this.trailerList = trailerList;
    }
}
