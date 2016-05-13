package com.cosmos.moviemeter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    static View fragmentView;
    static TextView movieDate;
    static TextView vote;
    static TextView overview;
    static ImageView moviePoster;
    static TextView movieTitle;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getBaseContext();
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;


        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            fragmentView = findViewById(R.id.detail_fragment);
            fragmentView.setVisibility(View.INVISIBLE);
            MoviesFragment.large = true;
            movieTitle = (TextView) findViewById(R.id.detailMovieName);

            moviePoster = (ImageView)findViewById(R.id.detailMovieImage );

            movieDate = (TextView)findViewById(R.id.detailMovieReleaseDate);

            overview = (TextView)findViewById(R.id.detailMovieOverview);

            vote = (TextView)findViewById(R.id.detailMovieVote_average);
        }
        else {
            MoviesFragment.large = false;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static void getMovieData(String [] movieData){

        fragmentView.setVisibility(View.VISIBLE);

        Picasso.with(context)
                .load(movieData[1])
                .into(moviePoster);

        overview.setText(movieData[1]);
        vote.setText(movieData[1]);
        movieTitle.setText(movieData[1]);
        movieDate.setText(movieData[1]);


    }
}
