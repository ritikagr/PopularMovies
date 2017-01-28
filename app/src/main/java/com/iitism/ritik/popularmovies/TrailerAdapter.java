package com.iitism.ritik.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Ritik on 02-10-2016.
 */
public class TrailerAdapter extends ArrayAdapter {
    Context context;
    List<Trailer> tList;
    int resourceId;
    public TrailerAdapter(Context context, int resource, List<Trailer> objects) {
        super(context, resource , objects);
        this.context = context;
        this.resourceId = resource;
        this.tList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TrailerHolder trailerHolder = null;

        if(view==null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(resourceId,parent,false);

            trailerHolder = new TrailerHolder();

            trailerHolder.v_name = (TextView) view.findViewById(R.id.trailer);
            trailerHolder.v_key = (TextView) view.findViewById(R.id.trailer_id);

            view.setTag(trailerHolder);
        }
        else
        {
            trailerHolder = (TrailerHolder) view.getTag();
        }

        Trailer trailer = tList.get(position);
        trailerHolder.v_name.setText(trailer.getTname());
        trailerHolder.v_key.setText(trailer.getTkey());

        return view;
    }

    static class TrailerHolder
    {
        TextView v_name;
        TextView v_key;
    }
}
