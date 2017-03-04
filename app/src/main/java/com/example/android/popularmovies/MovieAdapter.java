package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by gowth on 1/3/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private String[] MovieDetails;
    private Context context;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(String moviedetail);
    }

public MovieAdapter(Context context, MovieAdapterOnClickHandler clicklistener){
    mClickHandler=clicklistener;
    this.context=context;
}
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context=parent.getContext();
        int layoutid=R.layout.movie_list;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldattachtoparent=false;
        View view=inflater.inflate(layoutid,parent,shouldattachtoparent);



        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        String moviedetails=MovieDetails[position];
        String[] Splitdata=moviedetails.split(">");
        String imageurl=Splitdata[0];



        Picasso.with(context).load(imageurl).into(holder.mImageview);

    }

    @Override
    public int getItemCount() {
        if(null==MovieDetails)
            return 0;

        return MovieDetails.length;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mImageview;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mImageview= (ImageView) itemView.findViewById(R.id.movieimage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            String movie = MovieDetails[adapterPosition];
            mClickHandler.onClick(movie);

        }
    }

    public void setMovieData(String[] movieData) {
        MovieDetails = movieData;
        notifyDataSetChanged();
    }
}
