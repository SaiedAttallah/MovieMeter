package com.cosmos.moviemeter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Saied Attallah on 4/22/2016.
 */
public class ImageAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final MovieDetails mLock = new MovieDetails();

    private List<MovieDetails> mObjects;

    public ImageAdapter(Context context, List<MovieDetails> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(MovieDetails object) {
        synchronized (mLock) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }

    public void setData(List<MovieDetails> data) {
        clear();
        for (MovieDetails movie : data) {
            add(movie);
        }
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public MovieDetails getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.movie_image_view, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final MovieDetails movieDetails = getItem(position);

        String image_url = "http://image.tmdb.org/t/p/w185" + movieDetails.getImage();

        viewHolder = (ViewHolder) view.getTag();

        Glide.with(getContext()).load(image_url).into(viewHolder.imageView);
        viewHolder.titleView.setText(movieDetails.getTitle());

        return view;
    }

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView titleView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_image);
            titleView = (TextView) view.findViewById(R.id.grid_item_title);
        }
    }
}
