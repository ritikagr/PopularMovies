package com.iitism.ritik.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.preference.TwoStatePreference;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Movie_Details extends AppCompatActivity {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "?api_key=1fc12ca5d33e28912325b3188ac062e8&append_to_response=videos";
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private ImageView mMovie_image;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mReleaseDate;
    private TextView mUserRating;
    private List<Trailer> tList;
    private String mMovieId = null;
    private ProgressDialog mPd;

    private ListView trailerList;
    private ArrayAdapter mTrailerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mMovieId = intent.getStringExtra("Movie_id");
        mPd = new ProgressDialog(this);

        mMovie_image = (ImageView) findViewById(R.id.movie_image);
        mTitle = (TextView) findViewById(R.id.movie_title);
        mOverview = (TextView) findViewById(R.id.overview);
        mReleaseDate = (TextView) findViewById(R.id.releaseDate);
        mUserRating = (TextView) findViewById(R.id.userRating);

        trailerList = (ListView) findViewById(R.id.trailers_list);
        tList = Collections.synchronizedList(new ArrayList<Trailer>());
        mTrailerAdapter = new TrailerAdapter(this,R.layout.trailers_list_item,tList);
        trailerList.setAdapter(mTrailerAdapter);

        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer tr = (Trailer) parent.getItemAtPosition(position);
                String key = tr.Tkey;
                Intent youtubeIntent = new Intent(getApplicationContext(),YoutubePlayer.class);
                youtubeIntent.putExtra("KEY",key);
                startActivity(youtubeIntent);
            }
        });

        getMovieDetail(mMovieId);

    }

    public void getMovieDetail(String movie_id)
    {
        mPd.setMessage("Loading...");
        mPd.setCancelable(false);
        mPd.setCanceledOnTouchOutside(false);
        mPd.show();
        StringRequest stringRequest = new StringRequest(BASE_URL + movie_id + API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPd.cancel();
                try {
                    showDetailView(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Movie_Details.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showDetailView(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String Title = jsonObject.getString("original_title");
        String poularity = jsonObject.getString("popularity");
        String overview = jsonObject.getString("overview");
        String poster_path = jsonObject.getString("poster_path");
        String releaseDate = jsonObject.getString("release_date");
        String runtime = jsonObject.getString("runtime");
        JSONArray jsonArray = jsonObject.getJSONArray("spoken_languages");
        String language = jsonArray.getJSONObject(0).getString("name");

        String status = jsonObject.getString("status");
        String tagline = jsonObject.getString("tagline");
        String vote_average = jsonObject.getString("vote_average");

        JSONObject jsonObject1 = jsonObject.getJSONObject("videos");
        JSONArray jsonArray1 = jsonObject1.getJSONArray("results");

        String poster_url = "http://image.tmdb.org/t/p/w780"+ poster_path;

        Picasso.with(this).load(poster_url).error(R.drawable.error)
                .placeholder(R.drawable.placeholder)
                .into(mMovie_image, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                findViewById(R.id.image_layout).setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                findViewById(R.id.image_layout).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
        mTitle.setText(Title);
        getSupportActionBar().setTitle(mTitle.getText());
        findViewById(R.id.overview_layout).setVisibility(View.VISIBLE);
        mOverview.setText(overview);
        findViewById(R.id.releaseDate_layout).setVisibility(View.VISIBLE);
        mReleaseDate.setText(releaseDate);
        findViewById(R.id.user_rating_layout).setVisibility(View.VISIBLE);
        mUserRating.setText(vote_average);
        findViewById(R.id.trailer_title).setVisibility(View.VISIBLE);
        findViewById(R.id.user_rating_layout).setVisibility(View.VISIBLE);

        for(int i=0;i<jsonArray1.length();i++)
        {
            JSONObject j = jsonArray1.getJSONObject(i);
            Trailer t = new Trailer(j.getString("name"),j.getString("key"));
            tList.add(t);
            mTrailerAdapter.notifyDataSetChanged();
        }

        findViewById(R.id.card_trailer_list).setVisibility(View.VISIBLE);
        findViewById(R.id.seeReview).setVisibility(View.VISIBLE);
    }

    public void OnClickSeeReview(View v)
    {
        if(!mTitle.getText().toString().isEmpty() && mMovieId!=null) {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("movieId", mMovieId);
            intent.putExtra("movie_name", mTitle.getText());
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
