package com.example.locationmapsexample;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    // launch request with permission to access GPS  (fine location) and WIFI/CELLULAR based location (coarse locaiton)
    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            if (result != null) {
                boolean fine = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                boolean coarse = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                // this means permission has been approved
                if (fine && coarse) {

                    // this method handles locations
                    getLocation();


                    Toast.makeText(MainActivity.this, "Location Permission approved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "App cannot work without location approval", Toast.LENGTH_SHORT).show();
                    MainActivity.this.finish();
                }


            } else {
                Toast.makeText(MainActivity.this, "App cannot work without location approval", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }


        }

    });

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissionLauncher.launch(permissions);

        }
        // this means we have location
        else
        {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            // a few options to get the location
            // 1 GetLastLocation() - this gets the last time location was checked by any client
            // 2 getCurrentLocation - a fresh location

            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,null).addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // location hold the lat and long coordinates or last know location
                    if(location!=null)
                    {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        TextView textView = findViewById(R.id.tvMainLatLng);

                        textView.setText("Last known Location is: Lat = " + lat + ", Lon = " + lon );


                    }
                    else
                        Toast.makeText(MainActivity.this, "Failed to get location ", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();


    }

    public void locationUpdateClick(View view)
    {
        Intent intent = new Intent(this,ActivityLocationUpdate.class);
        startActivity(intent);

    }

    public void mapExampleClick(View view) {
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }

    public void gotoMapOnly(View view) {
        Intent intent = new Intent(this,MapOnlyActivity.class);
        startActivity(intent);

    }
}