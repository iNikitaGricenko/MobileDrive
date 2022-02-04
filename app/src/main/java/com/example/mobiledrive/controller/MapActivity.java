package com.example.mobiledrive.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mobiledrive.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

import lombok.SneakyThrows;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        map = googleMap;
        map.setMyLocationEnabled(true);

        /* TODO: realize getting user current location */

        LatLng zaporizhzhya = getLocation();
        if (zaporizhzhya == null) {
            zaporizhzhya = new LatLng(47.8580910792408, 35.10394327473505);
        }
        map.addMarker(new MarkerOptions().position(zaporizhzhya).title("Zaporizhzhya"));
        map.moveCamera(CameraUpdateFactory.newLatLng(zaporizhzhya));
        float minZoom = 14.5f;
        map.setMinZoomPreference(minZoom);
    }

    @SuppressLint("MissingPermission")
    private LatLng getLocation() {
        final LatLng[] yourLocation = new LatLng[1];
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SneakyThrows
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                    int maxResults = 1;
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, maxResults);
                    yourLocation[0] = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                    map.addMarker(new MarkerOptions().position(yourLocation[0]));
                }
            }
        });
        return yourLocation[0];
    }
}