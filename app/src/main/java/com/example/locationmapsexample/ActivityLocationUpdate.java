package com.example.locationmapsexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class ActivityLocationUpdate extends AppCompatActivity {

    // client for location updates
    private FusedLocationProviderClient fusedLocationClient;

    // holds all the request parameters
    private LocationRequest locationRequest;

    // callback for location updates
    private LocationCallback locationCallback;

    private double lng = 0, lat = 0;
    private TextView tvLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_update);

        initViews();
        // set the location request settings according to your app requirement
        setUpLocation();
        createLocationRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void initViews() {
        tvLocation = findViewById(R.id.tvUpdateLocation);
    }

    private void startLocationUpdates()
    {
        // assuming permission has been granted in Main activity
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    // register as location client and implement the location Call Back Method
    private void setUpLocation() {
        fusedLocationClient = new FusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult != null) {
                    String str = "";
                    for (Location location : locationResult.getLocations()) {
                        // Update UI with location data
                        // ...
                        lng = location.getLongitude();
                        lat = location.getLatitude();
                        str += "location, lat: " + lat + ", long: " + lng + "\n";

                        // GEOFENCING


/*
                        Geocoder geocoder = new Geocoder(ActivityLocationUpdate.this);
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(lat,lng,5);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(addresses !=null)
                        {
                            for (Address addr: addresses )
                            {
                                for(int i=0;i<= addr.getMaxAddressLineIndex();i++)
                                {
                                    str += "\n" + addr.getAddressLine(i);


                }
            }
        }
        */
                    }
                    tvLocation.setText(str);

                }
            }
        };
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void createLocationRequest()
    {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1500);
        locationRequest.setSmallestDisplacement(20.0f);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
}