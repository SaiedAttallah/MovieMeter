package com.cosmos.moviemeter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Saied Attallah on 4/22/2016.
 */
public class ImageAdapter extends BaseAdapter {
    Context activity;
    ArrayList<MovieDetails> moviePoster;
    int resource;


    public ImageAdapter(Context context, int resource, ArrayList<MovieDetails> movieDetailsArrayList) {
        activity = context;
        moviePoster = movieDetailsArrayList;
        this.resource = resource;
        //  Toast.makeText(context, String.valueOf(moviePoster.size()),Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return moviePoster.size();
    }

    @Override
    public MovieDetails getItem(int position) {
        return moviePoster.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //  Toast.makeText(activity, String.valueOf(position),Toast.LENGTH_SHORT).show();
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_image_view, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.movieImageView);
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        Picasso.with(activity).load(moviePoster.get(position).getPosterPath()).resize(screenWidth/2, screenHeight/2).into(holder.imageView);
        return convertView;


    }

    private void startActivity(Intent mIntent) {
        Intent mintent = new Intent(getActivity(), DetailFragment.class);
        mIntent.putExtra(Intent.EXTRA_TEXT, true);
        startActivity(mintent);

    }

    public Context getActivity() {
        return activity;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
