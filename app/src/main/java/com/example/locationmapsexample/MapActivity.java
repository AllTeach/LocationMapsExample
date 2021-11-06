package com.example.locationmapsexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView tvLocation;
    private TextView tvAddress;
    private LocationRequest locationRequest;
    private MapView mMapView;
    private GoogleMap gMap = null;
    private double lng = 0, lat = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        initViews();
        setUpLocation();

        createLocationRequest();

        setUpMap();
    }

    private void initViews() {
        tvLocation = findViewById(R.id.tvLocation);
        tvAddress = findViewById(R.id.tvAddress);
        mMapView = findViewById(R.id.map);
    }

    private void setUpMap() {
        // Get a handle  and register the callback.
        // if API_KEY is not present this method will fail
        mMapView.onCreate(null);
        mMapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // map is ready -> Add a marker att current location
        gMap = googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                gMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            }
        });
     //   gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker"));
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

                        // add the Map here
                        if (gMap != null)
                        {
                            if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                return;
                            }
                        // this is to move location according to current position
                        gMap.setMyLocationEnabled(true);
                        //moves map with center of current location
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),15.0f));

                       // gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.walk1)));
                            CircleOptions circleOptions =  new CircleOptions()
                                    .center(new LatLng(lat, lng))
                                    .strokeColor(Color.GREEN)
                                    .radius(3);

                            gMap.addCircle(circleOptions);


                    }


/*

                        Geocoder geocoder = new Geocoder(MapActivity.this);
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

    private void startLocationUpdates()
    {
        // assuming permission has been granted in Main activity
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    public void startGeoCoding(View view)
    {
        // getting GeoCoding - is Synchronous
        // uses network - should run on a seperate thread
        // recommended not to use on UI Thread
        Handler mHandler = new Handler();
        Runnable mRunnableOnSeparateThread = new Runnable() {
            @Override
            public void run () {

                Geocoder geocoder = new Geocoder(MapActivity.this);
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(lat,lng,5);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                List<Address> finalAddresses = addresses;
                mHandler.post(new Runnable(){
                    @Override
                    public void run(){
                        String str="";

                        if(finalAddresses !=null)
                        {
                            for (Address addr: finalAddresses)
                            {
                                for(int i=0;i<= addr.getMaxAddressLineIndex();i++)
                                {
                                    str += "\n" + addr.getAddressLine(i);
                                }
                            }
                        }
                        tvAddress.setText(str);
                    }
                });


            }
        };
        new Thread(mRunnableOnSeparateThread).start();






    }


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
        stopLocationUpdates();
        mMapView.onPause();

    }

    protected void onResume() {
        super.onResume();
      //  checkPermissions();
      startLocationUpdates();
      mMapView.onResume();

    }

}