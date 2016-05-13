package com.cosmos.moviemeter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
public class DetailFragment extends Fragment {

    TextView over;
    ListView tlist;
    String []data;
    ArrayList<MovieTrialers> treailerkey;
    Button buttonAddFavorite;
    private MovieDao movieDBOperation;
    MovieDetails movieItem ;
    int isFavorite;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_fragment_frame_layout, container, false);
        movieDBOperation = new MovieDao(getContext());
        try {
            movieDBOperation.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        buttonAddFavorite = (Button)rootView.findViewById(R.id.detailMovieFavorite);
        buttonAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFavorite();
            }
        });
        tlist  = (ListView)rootView.findViewById(R.id.Trialerlist);

        Intent intent = new Intent();
        data = intent .getStringArrayExtra("data");

        TextView titel = (TextView) rootView.findViewById(R.id.detailMovieName);
        titel.setText(data[0]);

        ImageView poster = (ImageView)rootView.findViewById(R.id.detailMovieImage);
        Picasso.with(getContext())
                .load(data[1])
                .into(poster);

        TextView date = (TextView)rootView.findViewById(R.id.detailMovieReleaseDate);
        date.setText(data[3]);

        over = (TextView)rootView.findViewById(R.id.detailMovieOverview);
        over.setText(data[2]);
        TextView vote = (TextView)rootView.findViewById(R.id.detailMovieVote_average);
        vote.setText(data[4]);


        MainPostersAsyncTask task = new MainPostersAsyncTask();
        task.execute(data[5]);
        TrailerTask tssk = new TrailerTask();
        tssk.execute(data[5]);

        isFavorite = movieDBOperation.checkIsFavorite(data[5]);

        tlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String shar = "http://www.youtube.com/watch?v=" + treailerkey.get(position).getKey();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shar)));


                //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(treailerkey.get(position).getKey())));
            }
        });

        return rootView;
    }



    void addFavorite() {
        if ( isFavorite == -1 ) {

            movieDBOperation.addMovie(data[5],data[0], data[1],
                    data[2], data[4], data[3]);
            isFavorite = 0 ;
        }
        if ( isFavorite == 1 ) {
            movieDBOperation.updateFavoritesList(data[5], 0);
            buttonAddFavorite.setText("Add to favorite");
            isFavorite = 0 ;
        }
        else if ( isFavorite == 0 ) {
            movieDBOperation.updateFavoritesList(data[5] , 1);
            buttonAddFavorite.setText("remove from favorite");
            isFavorite = 1 ;
        }

    }

    class MainPostersAsyncTask extends AsyncTask<String, Void, String> {
        Context con1;
        GridView secondgird;




        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String base_string;

//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            String choice = sharedPreferences.getString("listpref", "");


            base_string = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?";



            BufferedReader reader = null;
            String posterJson = "";
            URL url = null;

            String movieJsonStr = null;

            Uri uri = Uri.parse(base_string).buildUpon()
                    .appendQueryParameter("api_key", "c9bb36f94aaa138a81f9546d2e558da8").build();

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
                Log.i("error", e.getMessage());
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
                String item = "";

                //Toast.makeText(c1,"size "+String.valueOf(posterJson.length()),Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject(posterJson);
                JSONArray arrayJ = jsonObject.getJSONArray("results");
                Log.i("url", uri.toString());
                for (int i = 0; i < arrayJ.length(); i++) {
                    JSONObject mymovie = arrayJ.getJSONObject(i);
                    String auth = mymovie.getString("author");
                    String content = mymovie.getString("content");

                    item =item +"\n" + auth +"\n" + content;
                }
                return item;
            } catch (Exception e) {

                Log.i("error", e.toString());
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
                        Log.e("ahmed", "Error closing stream", e);
                    }

            }

        }

        @Override
        protected void onPostExecute(String strings) {

            over.setText(strings);
        }
    }

    class TrailerTask extends AsyncTask<String, Void, ArrayList<MovieTrialers> > {
        Context con1;
        GridView secondgird;




        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected ArrayList<MovieTrialers> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String base_string;

//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            String choice = sharedPreferences.getString("listpref", "");


            base_string = "http://api.themoviedb.org/3/movie/" +params[0]+ "/videos?";



            BufferedReader reader = null;
            String posterJson = "";
            URL url = null;

            String movieJsonStr = null;

            Uri uri = Uri.parse(base_string).buildUpon()
                    .appendQueryParameter("api_key", "c9bb36f94aaa138a81f9546d2e558da8").build();

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
                Log.i("error", e.getMessage());
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
                String item = "";

                //Toast.makeText(c1,"size "+String.valueOf(posterJson.length()),Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject(posterJson);
                JSONArray arrayJ = jsonObject.getJSONArray("results");
                treailerkey=  new ArrayList<MovieTrialers>();
                for (int i = 0; i < arrayJ.length(); i++) {
                    JSONObject mymovie = arrayJ.getJSONObject(i);
                    MovieTrialers Mytreiler = new MovieTrialers();
                    Mytreiler.setName(mymovie.getString("name"));
                    Mytreiler.setKey(mymovie.getString("key"));


                    treailerkey.add(Mytreiler);
                }
                return treailerkey;
            } catch (Exception e) {

                Log.i("error", e.toString());
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
                        Log.e("ahmed", "Error closing stream", e);
                    }

            }

        }

        @Override
        protected void onPostExecute(ArrayList<MovieTrialers> strings) {
            tlist.setAdapter(new TrialerAdpter(getContext(),strings));


        }
    }
}
