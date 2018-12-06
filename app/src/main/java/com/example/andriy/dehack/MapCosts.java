package com.example.andriy.dehack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Andriy on 16.02.2018.
 */

public class MapCosts extends Fragment implements OnMapReadyCallback {
    View view;
    MapView mapView;
    GoogleMap map;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng gnisduchiv = new LatLng(49.320831, 24.084886);
        LatLng korolivci=new LatLng(49.358522, 24.135344);
        LatLng livchici=new LatLng(49.311552, 24.125584);
        LatLng ruda= new LatLng(49.304852, 24.096441);
        LatLng gannivci=new LatLng(49.292417, 24.072283);

        map = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gnisduchiv, 12));
        googleMap.addMarker(new MarkerOptions().position(gnisduchiv));
        googleMap.addMarker(new MarkerOptions().position(korolivci));
        googleMap.addMarker(new MarkerOptions().position(livchici));
        googleMap.addMarker(new MarkerOptions().position(ruda));
        googleMap.addMarker(new MarkerOptions().position(gannivci));


       // googleMap.setOnInfoWindowClickListener(this);


    }
}
