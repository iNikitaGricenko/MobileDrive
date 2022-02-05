package com.example.mobiledrive.listener;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.google.android.gms.maps.GoogleMap.OnMapClickListener;

public class MapClickListener implements OnMapClickListener, OnMarkerClickListener {
    private final GoogleMap googleMap;
    private final MarkerOptions markerOptions = new MarkerOptions().title("Drive here?");
    private final BitmapDescriptor iconDescriptor = BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_RED);

    private Marker marker;

    public MapClickListener(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        markerOptions.position(latLng);
        markerOptions.icon(iconDescriptor);
        marker = googleMap.addMarker(markerOptions);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        return true;
    }
}
