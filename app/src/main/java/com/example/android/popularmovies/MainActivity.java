package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.MovieContract;
import com.example.android.popularmovies.utilities.MovieJson;
import com.example.android.popularmovies.utilities.NetWorkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.R.styleable.RecyclerView;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;


    private String ordertype="popular";
    public static final String[] MOVIE_DETAIL={
            MovieContract.MovieEntry.COLUMN_IMAGEID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_PLOT,
            MovieContract.MovieEntry.COLUMN_VOTING,
            MovieContract.MovieEntry.COLUMN_DATE,
            MovieContract.MovieEntry.COLUMN_TRAILER,
            MovieContract.MovieEntry.COLUMN_REVIEW,
            MovieContract.MovieEntry.COLUMN_ID
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerviewmovies);

        Context context=getApplicationContext();
        mMovieAdapter=new MovieAdapter(context,this);

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mMovieAdapter);

        mErrorMessageDisplay= (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator= (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovies();

    }



    private void loadMovies() {

        showMovieData();
        new GetMovieDetails().execute(ordertype);

    }


    private void showMovieData() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String moviedetail) {
        Context context = this;
        Class destinationClass = MovieDetails.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, moviedetail);
        startActivity(intentToStartDetailActivity);
    }


    public class GetMovieDetails extends AsyncTask<String,Void,String[]>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {
            String order=strings[0];
            URL moviesurl= NetWorkUtils.getmovieUrl(order);
            try {
                String Jsonresponse=NetWorkUtils.getResponseFromHttpUrl(moviesurl);
                String[] SimpleJsondata= MovieJson.getSimpleMovieStringsFromJson(MainActivity.this,Jsonresponse);

                return SimpleJsondata;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieData();
                mMovieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }


    }

    @Override
    protected void onResume() {
        if(R.id.order_favourite)
        super.onResume();
    }

    public class GetFavouriteMovieDetails extends AsyncTask<Void,Void,String[]>
    {
private Context mContext;

        public GetFavouriteMovieDetails(Context context)
        {
            mContext=context;

        }

        private String[] getFavoriteMoviesDataFromCursor(Cursor cursor) {
            String[] results = new String[cursor.getCount()];
            int i=0;
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    results[i]=cursor.getString(cursor.getColumnIndex("image_id"))+">"
                            +cursor.getString(cursor.getColumnIndex("title"))+">"
                            +cursor.getString(cursor.getColumnIndex("plot"))+">"
                            +cursor.getString(cursor.getColumnIndex("date"))+">"
                             +cursor.getString(cursor.getColumnIndex("vote"))+">"
                             +cursor.getString(cursor.getColumnIndex("trailer"))+">"
                             +cursor.getString(cursor.getColumnIndex("review"))+">"
                             +cursor.getInt(cursor.getColumnIndex("id"));
                    i++;
                } while (cursor.moveToNext());
                cursor.close();
            }
            return results;        }

        @Override
        protected String[] doInBackground(Void... voids) {


            Cursor cursor=mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                     MOVIE_DETAIL  ,
                    null,
                    null,
                    null);


            return getFavoriteMoviesDataFromCursor(cursor);
        }

        @Override
        protected void onPostExecute(String[] movies) {

      //   String[] stockArr = new String[movies.size()];
       //     stockArr = movies.toArray(stockArr);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
           if (movies != null) {
                showMovieData();
                mMovieAdapter.setMovieData(movies);
            } else {
                showErrorMessage();
           }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.movielistorder, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.order_popular) {

            new GetMovieDetails().execute("popular");
            return true;
        }

        if(id==R.id.order_rating)
        {
            new GetMovieDetails().execute("top_rated");

            return true;
        }
        if(id==R.id.order_favourite)
        {
            new GetFavouriteMovieDetails(getApplicationContext()).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
