package com.example.progettotreest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    int did;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =0;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        did = getIntent().getIntExtra("did", -1);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();



    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            //devo richiedere i permessi
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }else {
            //ho i permessi
            getUserLocation();
        }

    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        Log.d(MyStrings.PROVA, "calcolo la posizione");
        fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY,null)
                .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Log.d("Location", "Current location:" + location.toString());
                        if (this.googleMap!=null){
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 12f));
                        }
                        //todo: se finisce prima che sia pronta la mappa questo non funziona
                    } else {
                        Log.d("Location", "Current location not available");
                    } });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.464211,9.191383), 12f));

        CommunicationController.getStations(this, Model.getInstance().getSid(), did, response -> {
           Log.d(MyStrings.VOLLEY, "Stazioni: "+response.toString());
            try {
                JSONArray stations = response.getJSONArray("stations");
                PolylineOptions polylineOptions = new PolylineOptions().clickable(true);
                for (int i =0; i<stations.length(); i++){
                    JSONObject station = stations.getJSONObject(i);
                    double lat = station.getDouble("lat");
                    double lon = station.getDouble("lon");
                    polylineOptions.add(new LatLng(lat,lon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(station.getString("sname")));



                }
                googleMap.addPolyline(polylineOptions);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d(MyStrings.VOLLEY, error.toString()));

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d("Location", "Now the permission is granted");
                    getUserLocation();
                } else {
                    Log.d("Location", "Permission still not granted");
                    //todo: fai un alert grande quanto una casa che dice che non puoi mostrare la posizione
                }
                return;
            }
        }
    }


}