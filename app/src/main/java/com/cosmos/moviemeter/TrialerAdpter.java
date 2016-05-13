package com.cosmos.moviemeter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Saied Attallah on 5/13/2016.
 */
public class TrialerAdpter extends BaseAdapter {

    Context context ;
    ArrayList<MovieTrialers> trailerList;
    public TrialerAdpter(Context context ,ArrayList<MovieTrialers> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @Override
    public int getCount() {
        return trailerList.size();
    }

    @Override
    public Object getItem(int position) {
        return trailerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trialer_item, null);
        }
        TextView trialerName = (TextView)convertView.findViewById(R.id.trialerTitle);
        trialerName.setText(trailerList.get(position).getName());

        return convertView;

    }
}
