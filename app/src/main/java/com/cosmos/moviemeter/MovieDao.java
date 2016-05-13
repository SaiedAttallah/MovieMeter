package com.cosmos.moviemeter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Saied Attallah on 5/13/2016.
 */
public class MovieDao {

    private DataBaseWrapper dbHelper;


    private SQLiteDatabase database;

    public MovieDao(Context context) {

        dbHelper = new DataBaseWrapper(context);

    }

    public void open() throws SQLException {

        database = dbHelper.getWritableDatabase();

    }
    public void close() {

        dbHelper.close();

    }

    public void addMovie(String id , String title , String posterURL , String date , String rate , String overview) {
        ContentValues values = new ContentValues();

        values.put(DataBaseWrapper.Movie_ID, id);
        values.put(DataBaseWrapper.Movie_TITLE, title);
        values.put(DataBaseWrapper.Movie_POSTER_URL, posterURL);
        values.put(DataBaseWrapper.Movie_DATE, date);
        values.put(DataBaseWrapper.Movie_RATE, rate);
        values.put(DataBaseWrapper.Movie_OVERVIEW, overview);
        values.put(DataBaseWrapper.Movie_FAVORITES, 0);
        database.insert(DataBaseWrapper.Movies, null, values);

    }
    public void updateFavoritesList(String id , int value) {
        ContentValues values = new ContentValues();
        values.put(DataBaseWrapper.Movie_FAVORITES, value);
        database.update(DataBaseWrapper.Movies, values, "id=" +  id , null );

    }
    public ArrayList<MovieDetails> getFavoritesMovies() {
        ArrayList<MovieDetails>  movieItemsList = new ArrayList<MovieDetails>();

        Cursor cursor = database.query(DataBaseWrapper.Movies,

                null, DataBaseWrapper.Movie_FAVORITES + "=1" , null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            MovieDetails movieItem = parseMovie(cursor);
            movieItemsList.add(movieItem );

            cursor.moveToNext();

        }
        cursor.close();
        return movieItemsList;

    }
    public int checkIsFavorite(String movieId ) {
        Cursor cursor  = database.query(DataBaseWrapper.Movies,
                null ,
                DataBaseWrapper.Movie_ID + " = ? ",
                new String[]{movieId}, null, null, null);

        //    null, DataBaseWrapper.Movie_ID + "=" +movieId , null, null, null, null);
        cursor.moveToFirst();

        if (cursor.isAfterLast())
            return -1 ;
        String c = cursor.getString(6) ;
        if ( cursor.getString(6).equals("1") ) {
            cursor.close();
            return 1;
        }
        return 0;

    }

    private MovieDetails parseMovie(Cursor cursor) {

        MovieDetails movieItem = new MovieDetails();

        movieItem.setMovieId(cursor.getString(0));
        movieItem.setOriginalTitle(cursor.getString(1));
        movieItem.setPosterPath(cursor.getString(2));
        movieItem.setReleaseDate(cursor.getString(3));
        movieItem.setVoteAverage(cursor.getString(4));
        movieItem.setMovieOverview(cursor.getString(5));

        return movieItem;

    }
}
