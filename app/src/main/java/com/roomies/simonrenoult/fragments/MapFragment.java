package com.roomies.simonrenoult.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roomies.simonrenoult.activities.CollocationsListActivity;
import com.roomies.simonrenoult.models.Collocation;
import com.roomies.simonrenoult.models.Roomy;
import com.roomies.simonrenoult.roomies.R;

public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private Collocation _collocation;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle bundle) {
        super.onCreate(bundle);
        View v = inflater.inflate(R.layout.fragment_map, container,false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(bundle);

        mMapView.onResume();// needed to get the map to display immediately

        Bundle args = getArguments();
        Collocation c = (Collocation) args.getSerializable(CollocationsListActivity.COLLOCATION.toString());

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.setMyLocationEnabled(true);

        if(c.getRoomies().size()>0) {
            LatLng initialPos = new LatLng(c.getRoomies().get(0).getLat(), c.getRoomies().get(0).getLng());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPos, 15));
        }
        
        for(Roomy r : c.getRoomies()) {
            googleMap.addMarker(new MarkerOptions()
                    .title(r.getUsername())
                    .position(new LatLng(r.getLat(), r.getLng())));
        }
        
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}