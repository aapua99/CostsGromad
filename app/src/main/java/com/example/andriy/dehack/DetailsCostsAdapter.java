package com.example.andriy.dehack;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andriy on 06.02.2018.
 */

public class DetailsCostsAdapter extends ArrayAdapter<DataClass> {




    public DetailsCostsAdapter(Activity context, ArrayList<DataClass> places) {
        super(context, 0, places);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view

        View listItemView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_object, parent, false);


        DataClass currentPlace = getItem(position);

        TextView nameCost = (TextView) listItemView.findViewById(R.id.nameCost);
        TextView valueCost=(TextView)  listItemView.findViewById(R.id.valueCost);
        nameCost.setText(currentPlace.getName());
        valueCost.setText(currentPlace.getValue());


        return listItemView;
    }

}