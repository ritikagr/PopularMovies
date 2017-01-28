package com.iitism.ritik.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewActivity extends AppCompatActivity {

    private static String movieId = null;
    private static String movieName = "Reviews";
    private static int mPage = 1;
    private JSONArray reviewsArray = null;
    private static int reviewArrayIndex = -1;
    private static int totalPages = 0;
    private static int totalReviews = 0;
    private Button mPrevBtn;
    private Button mNextBtn;
    private LinearLayout mBtnLayout;
    private TextView mAuthor;
    private TextView mContent;
    private ProgressDialog mPd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");
        movieName = intent.getStringExtra("movie_name");
        mPd = new ProgressDialog(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(movieName);
        mPrevBtn = (Button) findViewById(R.id.prevBtn);
        mNextBtn = (Button) findViewById(R.id.nextBtn);
        mBtnLayout = (LinearLayout) findViewById(R.id.btnLayout);
        mAuthor = (TextView) findViewById(R.id.reviewBy);
        mContent = (TextView) findViewById(R.id.reviewContent);

        getReview();
    }

    public void getReview()
    {
        mPd.setMessage("Loading...");
        mPd.setCancelable(false);
        mPd.setCanceledOnTouchOutside(false);
        mPd.show();
        String reviewURL = PopularMovies.BASE_URL + movieId + "/reviews" + PopularMovies.API_KEY + PopularMovies.LANG_PAGE + String.valueOf(mPage);
        StringRequest stringRequest = new StringRequest(reviewURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ReviewActivity", response);
                mPd.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if (jsonArray.length()>0)
                    reviewsArray = jsonArray;

                    totalPages = jsonObject.getInt("total_pages");
                    totalReviews = jsonObject.getInt("total_results");
                    if (jsonArray.length()>0) {
                        reviewArrayIndex = 0;
                        mBtnLayout.setVisibility(View.VISIBLE);
                    }
                    showReview();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast(error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showReview()
    {
        if(reviewsArray!=null && reviewArrayIndex>=0)
        {

            try {
                if (mPage==totalPages && reviewArrayIndex==totalReviews-1)
                {
                    //disable next button
                    mNextBtn.setEnabled(false);
                    mPrevBtn.setEnabled(true);
                }
                else if(mPage==1 && reviewArrayIndex==0)
                {
                    //disable prev button
                    mPrevBtn.setEnabled(false);
                    mNextBtn.setEnabled(true);
                }
                else if(mPage==1 && mPage==totalPages && reviewArrayIndex==0 && reviewArrayIndex==totalReviews-1)
                {
                    mPrevBtn.setEnabled(false);
                    mNextBtn.setEnabled(false);
                }
                else
                {
                    mPrevBtn.setEnabled(true);
                    mNextBtn.setEnabled(true);
                }

                mAuthor.setVisibility(View.VISIBLE);
                findViewById(R.id.card_reviewContent).setVisibility(View.VISIBLE);
                JSONObject jsonObject = reviewsArray.getJSONObject(reviewArrayIndex);
                String author = jsonObject.getString("author");
                String content = jsonObject.getString("content");
                String url = jsonObject.getString("url");

                mAuthor.setText("Review by "+ author);
                mContent.setText(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(totalPages==0)
        {
            findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        }
    }

    public void OnClickNext(View v)
    {
        if(reviewArrayIndex<reviewsArray.length()-1)
        {
            reviewArrayIndex +=1;
            showReview();
        }
        else if(mPage<totalPages){
            mPage +=1;
            getReview();
        }
        else {

        }
    }

    public void OnClickPrev(View v)
    {
        if(reviewArrayIndex>0)
        {
            reviewArrayIndex-=1;
            showReview();
        }
        else if(mPage>1)
        {
            mPage -=1;
            getReview();
        }
        else {

        }
    }

    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
