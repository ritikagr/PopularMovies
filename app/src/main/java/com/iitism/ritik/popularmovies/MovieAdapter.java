package com.iitism.ritik.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ritik on 26-09-2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    Context context;
    int resourceId;
    List<Movie> data=null;
    public MovieAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MovieHolder movieHolder = null;

        if(view==null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(resourceId,parent,false);
            movieHolder = new MovieHolder();

            if(resourceId == R.layout.list_item_layout) {
                movieHolder.txtTitle = (TextView) view.findViewById(R.id.title);
                movieHolder.icon = (ImageView) view.findViewById(R.id.image_view);
                movieHolder.id = (TextView) view.findViewById(R.id.movie_id);
            }
            else {
                movieHolder.txtTitle = (TextView) view.findViewById(R.id.title_grid);
                movieHolder.icon = (ImageView) view.findViewById(R.id.image_view_grid);
                movieHolder.id = (TextView) view.findViewById(R.id.movie_id_grid);
            }

            view.setTag(movieHolder);
        }
        else
        {
            movieHolder = (MovieHolder) view.getTag();
        }

        Movie movie = data.get(position);

        movieHolder.txtTitle.setText(movie.getTitle());
        movieHolder.id.setText(movie.getId());
        String poster_url = "http://image.tmdb.org/t/p/w342"+movie.getPosterPath();
        Picasso.with(context).load(poster_url).placeholder(R.drawable.placeholder).error(R.drawable.error).into(movieHolder.icon);

        return view;
    }

    static class MovieHolder{
        TextView txtTitle;
        ImageView icon;
        TextView id;
    }
}
