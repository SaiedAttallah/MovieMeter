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
 * Created by Saied Attallah on 5/13/2016.
 */
public class TrailerAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final TrailerDetails mLock = new TrailerDetails();

    private List<TrailerDetails> mObjects;

    public TrailerAdapter(Context context, List<TrailerDetails> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(TrailerDetails object) {
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

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public TrailerDetails getItem(int position) {
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
            view = mInflater.inflate(R.layout.trailer_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final TrailerDetails trailerDetails = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        String yt_thumbnail_url = "http://img.youtube.com/vi/" + trailerDetails.getKey() + "/0.jpg";
        Glide.with(getContext()).load(yt_thumbnail_url).into(viewHolder.imageView);

        viewHolder.nameView.setText(trailerDetails.getName());

        return view;
    }

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView nameView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.trailer_image);
            nameView = (TextView) view.findViewById(R.id.trailer_name);
        }
    }
}
