package com.iitism.ritik.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private List<Movie> movieList=null;
    private MovieAdapter mListAdapter = null;
    private MovieAdapter mGridAdapter = null;
    private ListView mListView;
    private GridView mGridview;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout mSwipeRefreshLayoutGrid;
    private String URL = null;


    //list=1 or grid=2;
    private int mList_Grid_View = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayoutGrid = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshGrid);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayoutGrid.setOnRefreshListener(this);
        final Movie movie[] = new Movie[]{

        };

        URL = PopularMovies.BASE_URL + getString(R.string.popular) + PopularMovies.API_KEY;

        movieList = Collections.synchronizedList(new ArrayList<Movie>());

        mListView = (ListView) findViewById(R.id.movie_list);

        mGridview = (GridView) findViewById(R.id.movie_list_grid);

        this.setAdapters(mList_Grid_View);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                fetchMovies(URL);
            }
        });

        mSwipeRefreshLayoutGrid.post(new Runnable() {
            @Override
            public void run() {
                fetchMovies(URL);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mList_Grid_View == 1)
                {
                    mList_Grid_View = 2;
                }
                else
                {
                    mList_Grid_View = 1;
                }
                setAdapters(mList_Grid_View);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie1 = (Movie) parent.getItemAtPosition(position);
                String movie_id = movie1.id;
                //Toast.makeText(MainActivity.this,movie_id,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Movie_Details.class);
                intent.putExtra("Movie_id",movie_id);
                startActivity(intent);
                //getMovieDetail(movie_id);
            }
        });

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie1 = (Movie) parent.getItemAtPosition(position);
                String movie_id = movie1.id;
                //Toast.makeText(MainActivity.this,movie_id,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Movie_Details.class);
                intent.putExtra("Movie_id",movie_id);
                startActivity(intent);
                //getMovieDetail(movie_id);
            }
        });
    }

    public void setAdapters(int viewArrangement)
    {
        if(viewArrangement == 1)
        {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayoutGrid.setVisibility(View.GONE);
            mListAdapter = new MovieAdapter(this,R.layout.list_item_layout,movieList);
            mListView.setAdapter(mListAdapter);
            mSwipeRefreshLayout.setRefreshing(true);
        }
        else if(viewArrangement == 2)
        {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSwipeRefreshLayoutGrid.setVisibility(View.VISIBLE);
            mGridAdapter = new MovieAdapter(this,R.layout.list_item_layout_grid, movieList);
            mGridview.setAdapter(mGridAdapter);
            mSwipeRefreshLayoutGrid.setRefreshing(true);
        }
        fetchMovies(URL);
    }

    public void fetchMovies(String url)
    {
        if(mList_Grid_View == 1)
        mSwipeRefreshLayout.setRefreshing(true);
        else
        mSwipeRefreshLayoutGrid.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        if(mList_Grid_View == 1)
            mSwipeRefreshLayout.setRefreshing(false);
        else
            mSwipeRefreshLayoutGrid.setRefreshing(false);
    }

    public void showJson(String response)
    {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            movieList.clear();
            int n = jsonArray.length();
            for(int i=0;i<n;i++)
            {
                JSONObject movieObject = jsonArray.getJSONObject(i);
                String title = movieObject.getString("original_title");
                String poster_path = movieObject.getString("poster_path");
                String overView = movieObject.getString("overview");
                String releaseDate = movieObject.getString("release_date");
                String popularity = movieObject.getString("popularity");
                String voteAvg = movieObject.getString("vote_average");
                String id = movieObject.getString("id");
                movieList.add(new Movie(poster_path,title,overView,releaseDate,popularity,voteAvg,id));
                if(mList_Grid_View == 1)
                {
                    mListAdapter.notifyDataSetChanged();
                }
                else
                {
                    mGridAdapter.notifyDataSetChanged();
                }

            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"Not available",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id)
        {
            case R.id.get_top_rated:
                URL = PopularMovies.BASE_URL + getString(R.string.top_rated) +PopularMovies.API_KEY;
                fetchMovies(URL);
                break;
            case R.id.get_popular:
                URL = PopularMovies.BASE_URL + getString(R.string.popular) +PopularMovies.API_KEY;
                fetchMovies(URL);
                break;
            case R.id.get_upcoming:
                URL = PopularMovies.BASE_URL + getString(R.string.upcoming) +PopularMovies.API_KEY;
                fetchMovies(URL);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        fetchMovies(URL);
    }
}
