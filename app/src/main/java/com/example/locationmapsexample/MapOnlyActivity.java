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
        gMap.addMarker(new MarkerOptions().position(new LatLng(40.730610, -73.935242)).icon(BitmapDescriptorFactory.fromResource(R.drawable.walk1)));

    }

    public void setMarker(View view) {

    }


    public void moveCamera(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
      //  gMap.setMyLocationEnabled(true);
        // set Map in the heart of the big apple NYC :-)
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.730610, -73.935242),10.0f));


    }

    // MapView should be handled in all activity lifecycle methods
    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    protected void onResume() {
        super.onResume();
        //  checkPermissions();

        mMapView.onResume();

    }
}