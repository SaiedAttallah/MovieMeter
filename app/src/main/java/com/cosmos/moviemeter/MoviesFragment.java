package com.cosmos.moviemeter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    GridView gridView;
    static boolean large = false;
    ArrayList<String> moviePosters;

    private MovieDao movieDao;
    SharedPreferences sharedPreferences;
    String choice;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        movieDao = new MovieDao(getActivity());
        try {
            movieDao.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        gridView = (GridView) rootView.findViewById(R.id.girdView);

        getMovies();
        Toast.makeText(getActivity(), "Yesssssssss!", Toast.LENGTH_SHORT).show();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

//    void update() {
//        MainPostersAsyncTask MainPostersAsyncTask = new MainPostersAsyncTask(getActivity(), gridView);
//        MainPostersAsyncTask.execute();
//
//    }




    void getMovies() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        choice = sharedPreferences.getString("sorting", "");

        if (choice.equals("Favourite")){
            ArrayList<MovieDetails> movieDetailsArrayList = movieDao.getFavoritesMovies();
            gridView.setAdapter(new ImageAdapter(getActivity(), R.layout.movie_image_view, movieDetailsArrayList));
        }
        else {
            MainPostersAsyncTask mainPostersAsyncTask = new MainPostersAsyncTask(getActivity(), gridView);
            mainPostersAsyncTask.execute();
        }

    }



//    @Override
//    public void onStop() {
//        super.onStop();
//
//    }

    class MainPostersAsyncTask extends AsyncTask<Void, Void, ArrayList<MovieDetails>> {
        Context context;
        GridView secondGird;
        ArrayList<MovieDetails> movieDetailsArrayList;

        public MainPostersAsyncTask(Context con, GridView firstGrid) {
            context = con;
            secondGird = firstGrid;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected ArrayList<MovieDetails> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            String base_string;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String choice = sharedPreferences.getString(
                    getString(R.string.pref_sorting_key),
                    getString(R.string.pref_sorting_most_popular));
            base_string = "https://api.themoviedb.org/3/movie/popular?";

            if (choice.equals(getString(R.string.pref_sorting_top_rated))) {
                base_string = "https://api.themoviedb.org/3/movie/top_rated?";
            }


            BufferedReader reader = null;
            String posterJson = "";
            URL url = null;

            Uri uri = Uri.parse(base_string).buildUpon()
                    .appendQueryParameter("api_key", "").build();

            try {
                url = new URL(uri.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
//               url= new URL("https://");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            } catch (IOException e) {
                //  Toast.makeText(c1,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            try {
                InputStream streamReader = urlConnection.getInputStream();
                if (streamReader == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(streamReader));
                String line;
                while ((line = reader.readLine()) != null) {
                    posterJson += line;
                    Log.v("hi", line);
                }
                //Toast.makeText(c1,"size "+String.valueOf(posterJson.length()),Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject(posterJson);
                JSONArray arrayJ = jsonObject.getJSONArray("results");
                movieDetailsArrayList = new ArrayList<MovieDetails>();
                for (int i = 0; i < arrayJ.length(); i++) {
                    JSONObject movieJsonObject = arrayJ.getJSONObject(i);
                    String PP = new String("http://image.tmdb.org/t/p/w185");
                    PP = PP + movieJsonObject.getString("poster_path");
                    String original_title = movieJsonObject.getString("original_title");
                    String overview = movieJsonObject.getString("overview");
                    String release_date = movieJsonObject.getString("release_date");
                    String vote_average = movieJsonObject.getString("vote_average");
                    String id = movieJsonObject.getString("id");

                    MovieDetails movieDetails = new MovieDetails(vote_average,release_date,overview,original_title,id,posterJson);

                    movieDetailsArrayList.add(movieDetails);

                }
                return movieDetailsArrayList;

            } catch (Exception e) {
                return null;
                // Toast.makeText(c1,e.getMessage(),Toast.LENGTH_SHORT).show();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("BufferReaderIOException", "Error closing stream", e);
                    }

            }

        }

        @Override
        protected void onPostExecute(ArrayList<MovieDetails> strings) {
            final ImageAdapter imageAdapter= new ImageAdapter(context, R.layout.movie_image_view, strings);
            secondGird.setAdapter(imageAdapter);
            secondGird.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieDetails movieDetails = imageAdapter.getItem(position);
                    String[] ty = new String[6];
                    String title = movieDetails.getOriginalTitle();
                    String poster = movieDetails.getPosterPath();
                    String date = movieDetails.getReleaseDate();
                    String vote = movieDetails.getVoteAverage();
                    String over = movieDetails.getMovieOverview();
                    String Id = movieDetails.getMovieId();

                    String [] movieData = new String[6];
                    ty[0] = title;
                    ty[1] = poster;
                    ty[2] = over;
                    ty[3] = date;
                    ty[4] = Id;
                    ty[5] = vote;
                    if (!large) {
                        Intent intent = new Intent(getActivity(), DetailFragment.class);
                        intent.putExtra("data", ty);
//                    intent.putExtra("poster_url",poster);
                        startActivity(intent);
                    } else {
                        MainActivity.getMovieData(movieData);
                    }
                }
            });
        }
    }
}