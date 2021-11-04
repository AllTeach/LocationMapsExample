package com.example.locationmapsexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapOnlyActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mMapView;
    private GoogleMap gMap = null;
    private double lng = 0, lat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_only);

        initViews();
        setUpMap();

    }

    private void initViews() {
        mMapView = findViewById(R.id.mapOnly);
    }

    private void setUpMap() {
        // Get a handle  and register the callback.
        // if API_KEY is not present this method will fail
        mMapView.onCreate(null);
        mMapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // map is ready 
        gMap = googleMap;
        int num=1;
        gMap.addMarker(new MarkerOptions().position(new LatLng(40.730610, -73.935242)).icon(BitmapDescriptorFactory.fromResource(R.drawable.walk1)));

    }

    public void setMarker(View view) {

    }


    public void moveCamera(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.730610, -73.935242),10.0f));


    }
}