package com.example.android.popularmovies.utilities;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by gowth on 1/3/2017.
 */

public class NetWorkUtils {



    public static URL getmovieUrl(String order) {

        URL url = null;
        try {

            url = new URL("https://api.themoviedb.org/3/movie/"+order+"?api_key=b55d57cc7e87476cadcf4a12f2ab5522");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

     //   Log.v(TAG, "Built URI " + url);

        return url;

    }



    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
