package com.iitism.ritik.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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

import io.realm.MovieRealmProxy;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private List<Movie> movieList=null;
    private MovieAdapter mListAdapter = null;
    private MovieAdapter mGridAdapter = null;
    private ListView mListView;
    private GridView mGridview;
    private String URL = null;
    private int mPage = 1;
    private int totalPages = 0;
    private ProgressDialog mPd;
    private MenuItem mPrevMenuItem;
    private MenuItem mNextMenuItem;
    private Menu mMenu;
    private Realm mRealm;
    private FloatingActionButton fab;

    //list=1 or grid=2;
    private int mList_Grid_View = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPd = new ProgressDialog(this);
        final Movie movie[] = new Movie[]{
        };

        mRealm = Realm.getDefaultInstance();

        URL = PopularMovies.BASE_URL + getString(R.string.popular) + PopularMovies.API_KEY + PopularMovies.LANG_PAGE;

        movieList = Collections.synchronizedList(new ArrayList<Movie>());

        mListView = (ListView) findViewById(R.id.movie_list);

        mGridview = (GridView) findViewById(R.id.movie_list_grid);

        this.setAdapters(mList_Grid_View);

        fab = (FloatingActionButton) findViewById(R.id.fab);
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
                String movie_id = movie1.getId();
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
                String movie_id = movie1.getId();
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
            mListAdapter = new MovieAdapter(this,R.layout.list_item_layout,movieList);
            findViewById(R.id.content_main_list).setVisibility(View.VISIBLE);
            findViewById(R.id.content_main_grid).setVisibility(View.GONE);
            mListView.setAdapter(mListAdapter);
        }
        else if(viewArrangement == 2)
        {
            mGridAdapter = new MovieAdapter(this,R.layout.list_item_layout_grid, movieList);
            findViewById(R.id.content_main_list).setVisibility(View.GONE);
            findViewById(R.id.content_main_grid).setVisibility(View.VISIBLE);
            mGridview.setAdapter(mGridAdapter);
        }
        if(getSupportActionBar().getTitle()=="Favourite Movies")
            fetchFavouriteMovies();
        else
        fetchMovies(URL);
    }

    public void fetchMovies(String url)
    {
        url = url + String.valueOf(mPage);
        Log.i("MainActivity", url);

        mPd.setMessage("Loading...");
        mPd.setCancelable(false);
        mPd.setCanceledOnTouchOutside(false);
        mPd.show();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPd.cancel();
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
    }

    public void showJson(String response)
    {
        try {
            JSONObject jsonObject = new JSONObject(response);
            totalPages = jsonObject.getInt("total_pages");
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
                if(mPage>1 && mPage<totalPages)
                {
                    mPrevMenuItem.setEnabled(true);
                    mNextMenuItem.setEnabled(true);
                }
                else if(mPage==1 && totalPages>1)
                {
                    mPrevMenuItem.setEnabled(false);
                    mNextMenuItem.setEnabled(true);
                }
                else if(mPage==1 && totalPages==1)
                {
                    mPrevMenuItem.setEnabled(false);
                    mPrevMenuItem.setEnabled(false);
                }
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

    public void fetchFavouriteMovies()
    {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Movie> movieRealmResult = realm.where(Movie.class).findAll();

                movieList.clear();

                for (Movie movie: movieRealmResult) {
                    movieList.add(new Movie(movie.getPosterPath(),movie.getTitle(),movie.getOverView(),
                            movie.getReleaseDate(),movie.getPopularity(),movie.getVoteAvg(),movie.getId()));

                    if(mList_Grid_View == 1)
                    {
                        mListAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        mGridAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.mMenu = menu;
        mPrevMenuItem = this.mMenu.findItem(R.id.prev);
        mNextMenuItem = this.mMenu.findItem(R.id.next);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id)
        {
            case R.id.get_top_rated:
                mPage = 1;
                URL = PopularMovies.BASE_URL + getString(R.string.top_rated) +PopularMovies.API_KEY + PopularMovies.LANG_PAGE;
                getSupportActionBar().setTitle("Top Rated Movies");
                showPrevNextMenu();
                fetchMovies(URL);
                break;
            case R.id.get_popular:
                mPage = 1;
                URL = PopularMovies.BASE_URL + getString(R.string.popular) +PopularMovies.API_KEY + PopularMovies.LANG_PAGE;
                getSupportActionBar().setTitle("Popular Movies");
                showPrevNextMenu();
                fetchMovies(URL);
                break;
            case R.id.get_upcoming:
                mPage = 1;
                URL = PopularMovies.BASE_URL + getString(R.string.upcoming) +PopularMovies.API_KEY + PopularMovies.LANG_PAGE;
                getSupportActionBar().setTitle("Upcoming Movies");
                showPrevNextMenu();
                fetchMovies(URL);
                break;
            case R.id.get_favourite:
                getSupportActionBar().setTitle("Favourite Movies");
                hidePrevNextMenu();
                fetchFavouriteMovies();
                break;
            case R.id.prev:
                if(mPage>1) {
                    mPage -= 1;
                    fetchMovies(URL);
                }
                break;

            case R.id.next:
                if(mPage<totalPages) {
                    mPage += 1;
                    fetchMovies(URL);
                }
                break;
            case R.id.refresh:
                if(getSupportActionBar().getTitle()=="Favourite Movies")
                    fetchFavouriteMovies();
                else
                fetchMovies(URL);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showPrevNextMenu()
    {
        mPrevMenuItem.setVisible(true);
        mNextMenuItem.setVisible(true);
    }

    public void hidePrevNextMenu()
    {
        mPrevMenuItem.setVisible(false);
        mNextMenuItem.setVisible(false);
    }


    @Override
    public void onRefresh() {
        fetchMovies(URL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getSupportActionBar().getTitle()=="Favourite Movies")
            fetchFavouriteMovies();
        else
            fetchMovies(URL);
    }
}
