package com.example.android.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.popularmovies.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gowth on 1/3/2017.
 */

public class MovieJson {
    public static String[] getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        String[] moviedetailData = null;

        final String LIST = "results";


        JSONObject movieJson = new JSONObject(movieJsonStr);


        JSONArray movieArray = movieJson.getJSONArray(LIST);

        moviedetailData = new String[movieArray.length()];


        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject singlemovie = movieArray.getJSONObject(i);

            String path = singlemovie.getString("poster_path");
            String tittle=singlemovie.getString("original_title");
            String overview=singlemovie.getString("overview");
            String datee=singlemovie.getString("release_date");
            String voteavg=singlemovie.getString("vote_average");
            String id=singlemovie.getString("id");
            String trailers=gettrailer(id);
            String reviews=getreviews(id);
            moviedetailData[i] = "http://image.tmdb.org/t/p/w185"+path+">"+tittle+">"+overview+">"+datee+">"+voteavg+">"+trailers+">"+reviews+">"+id;
        }

        return moviedetailData;
    }

    private static String gettrailer(String id) {

        URL url = null;
        try {

            url = new URL("https://api.themoviedb.org/3/movie/"+id+"/videos?api_key=b55d57cc7e87476cadcf4a12f2ab5522");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String SimpleJsondata= "";
        String Jsonresponse= null;
        try {
            Jsonresponse = NetWorkUtils.getResponseFromHttpUrl(url);

            SimpleJsondata = getSimpleMovieTrailerFromJson(Jsonresponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SimpleJsondata;
    }

    private static String getreviews(String id) {

        URL url = null;
        try {

            url = new URL("https://api.themoviedb.org/3/movie/"+id+"/reviews?api_key=b55d57cc7e87476cadcf4a12f2ab5522");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String SimpleJsondata= "";
        String Jsonresponse= null;
        try {
            Jsonresponse = NetWorkUtils.getResponseFromHttpUrl(url);

            SimpleJsondata = getSimpleMovieReviewsFromJson(Jsonresponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SimpleJsondata;
    }


    public static String getSimpleMovieTrailerFromJson( String movieJsonStr)
            throws JSONException {
        String moviedetailData = "";

        final String LIST = "results";


        JSONObject movieJson = new JSONObject(movieJsonStr);


        JSONArray movieArray = movieJson.getJSONArray(LIST);

     //  String moviedetailData="" ;
        if(movieArray.length()>0) {

             for (int i = 0; i < movieArray.length(); i++) {

               JSONObject singlemovie = movieArray.getJSONObject(i);

                String path = singlemovie.getString("key");

                moviedetailData = moviedetailData + "<" + path;
             }
           }
        else
              {
                 return "No trailers";
              }
            return moviedetailData;

    }

    public static String getSimpleMovieReviewsFromJson( String movieJsonStr)
            throws JSONException {
        String moviedetailData = "";

        final String LIST = "results";


        JSONObject movieJson = new JSONObject(movieJsonStr);


        JSONArray movieArray = movieJson.getJSONArray(LIST);

        //  String moviedetailData="" ;
        if(movieArray.length()>0) {

            for (int i = 0; i < movieArray.length(); i++) {

                JSONObject singlemovie = movieArray.getJSONObject(i);

                String reviw = singlemovie.getString("content");

                moviedetailData = moviedetailData + "<" + reviw;
            }
        }
        else
        {
            return "No reviews";
        }
        return moviedetailData;

    }



}
