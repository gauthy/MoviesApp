package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;

import com.example.android.popularmovies.utilities.MovieContract;

/**
 * Created by gowth on 1/21/2017.
 */

public class CheckFavourite {

    public static int isFavorited(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_ID + " = ?",
                new String[] { Integer.toString(id) },
                null
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }


}
