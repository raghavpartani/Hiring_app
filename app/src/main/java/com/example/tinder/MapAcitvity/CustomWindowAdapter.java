package com.example.tinder.MapAcitvity;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.tinder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by MD on 06-05-2018.
 */

public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;


    public CustomWindowAdapter(Activity context){
        this.context = context;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View view = context.getLayoutInflater().inflate(R.layout.custom_window,null);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(marker.getTitle());
        return view;
    }
}
