package com.example.mobiledrive.controller;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mobiledrive.R;
import com.example.mobiledrive.listener.MapClickListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.graphics.Color.BLUE;

public class MapActivity extends AppCompatActivity {

    private SupportMapFragment map;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCurrentLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void setCurrentLocation() {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    goToCurrentLocation(location);
                } else {
                    getCurrentLocation();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                map.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        CircleOptions circleOptions = new CircleOptions().center(new LatLng(latitude, longitude))
                                .radius(30)
                                .strokeColor(BLUE)
                                .fillColor(BLUE);
                        float minZoom = 14.5f;
                        googleMap.addCircle(circleOptions);
                        googleMap.setMinZoomPreference(minZoom);
                        googleMap.setOnMapClickListener(new MapClickListener(googleMap));
                    }
                });
            }
        });
    }

    private void goToCurrentLocation(Location location) {
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                CircleOptions circleOptions = new CircleOptions().center(new LatLng(latitude, longitude))
                        .radius(30)
                        .strokeColor(BLUE)
                        .fillColor(BLUE);
                float minZoom = 14.5f;
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, minZoom));
                googleMap.addCircle(circleOptions);
                googleMap.setMinZoomPreference(minZoom);
                googleMap.setOnMapClickListener(new MapClickListener(googleMap));
            }
        });
    }
}