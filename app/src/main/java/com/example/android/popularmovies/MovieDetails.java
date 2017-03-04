package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.utilities.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by gowth on 1/4/2017.
 */

public class MovieDetails extends AppCompatActivity {

    private String mDetails;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mDate;
    private TextView mVote;
    private ImageView imgview;
    private TextView mreviewtext;
    Context context;
    String img;
    String title;
    String overview ;
    String date ;
    String voteavg ;
    String movietrailer ;
    String moviereviews ;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTitle = (TextView) findViewById(R.id.display_title);
        imgview = (ImageView) findViewById(R.id.poster);
        mOverview = (TextView) findViewById(R.id.overview);
        mDate = (TextView) findViewById(R.id.date);
        mVote = (TextView) findViewById(R.id.voting);
        mreviewtext= (TextView) findViewById(R.id.reviewext);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mDetails = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                String[] Details = mDetails.split(">");
                 img = Details[0];
                 title = Details[1];
                 overview = Details[2];
                 date = Details[3];
                 voteavg = Details[4];
                 movietrailer = Details[5];
                 moviereviews = Details[6];
                 id=Integer.parseInt(Details[7]);

                mTitle.setText(title);
                Picasso.with(MovieDetails.this).load(img).into(imgview);
                mOverview.setText(overview);
                mDate.setText("Release Date :" + date);
                mVote.setText(  voteavg+"/10");


                final String[] trailers = movietrailer.split("<");
                int lengthh = trailers.length;
                String[] moviereview = moviereviews.split("<");
                int lengthrev = moviereview.length;
                //    final LinearLayout lm = (LinearLayout) findViewById(R.id.buttonLayout);

                // create the layout params that will be used to define how your
                // button will be displayed
                //  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                //        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                for (int i = 1; i < lengthh; i++) {
                    //LinearLayout ll = new LinearLayout(this);
                    Button myButton = new Button(this);
                    myButton.setText("Trailer " + i);
                    myButton.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    View view=new View(this);
                    view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    //   myButton.setId(i);
                    final int index = i;
                    // myButton.setLayoutParams(params);
                    LinearLayout ll = (LinearLayout) findViewById(R.id.buttonLayout);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    myButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String trailerurl = "https://www.youtube.com/watch?v=" + trailers[index];

                            Uri webpage = Uri.parse(trailerurl);


                            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);


                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }


                        }
                    });

                    ll.addView(myButton);
                    ll.addView(view);
                    //lm.addView(myButton);


                }
                if(lengthrev>1) {

                    LinearLayout lr = (LinearLayout) findViewById(R.id.reviewLayout);
                    for (int j = 1; j < lengthrev; j++) {
                        TextView tv = new TextView(this);
                        TextView tv2=new TextView(this);
                        tv.setText("REVIEW " + j  + ":" +  "\n");
                        tv.setTextSize(20);
                        tv2.setText(moviereview[j]);
                        tv.setTextColor(getResources().getColor(R.color.colorWhite));
                        tv.setId(j + 5);
                        lr.addView(tv);
                        lr.addView(tv2);
                    }
                }
                else
                {
               mreviewtext.setText("No reviews");
                }
            }




            final FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

            int numm= CheckFavourite.isFavorited(getApplicationContext(),id);
            if(numm==1)
            {
                fabButton.setImageResource(R.drawable.ic_action_favorite);
            }
            else
            {
                fabButton.setImageResource(R.drawable.ic_action_name);
            }

            fabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 int num= CheckFavourite.isFavorited(getApplicationContext(),id);
                    if(num==1)
                    {
                  int deleted=  getContentResolver().delete(
                                MovieContract.MovieEntry.CONTENT_URI,
                                MovieContract.MovieEntry.COLUMN_ID + " = ?",
                                new String[]{Integer.toString(id)}
                        );
                      //  Toast.makeText(getBaseContext(), deleted+"deleted", Toast.LENGTH_LONG).show();
                      fabButton.setImageResource(R.drawable.ic_action_name);
                    }
                    else {
                        ContentValues contentValues = new ContentValues();

                        contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGEID, img);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_PLOT, overview);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTING, voteavg);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_DATE, date);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_TRAILER, movietrailer);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_REVIEW, moviereviews);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_ID, id);
                        // Insert the content values via a ContentResolver
                        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                        if (uri != null) {
                       //     Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                        }
                      fabButton.setImageResource(R.drawable.ic_action_favorite);
                    }
                }
            });



        }




    }


}