package com.example.progettotreest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    int did;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        did = getIntent().getIntExtra("did", -1);



    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.464211,9.191383), 12f));

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
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(station.getString("sname")));



                }
                googleMap.addPolyline(polylineOptions);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d(MyStrings.VOLLEY, error.toString()));

    }


}