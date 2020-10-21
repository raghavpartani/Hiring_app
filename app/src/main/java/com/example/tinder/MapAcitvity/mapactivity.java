package com.example.tinder.MapAcitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tinder.MainActivity;
import com.example.tinder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class mapactivity extends AppCompatActivity  implements OnMapReadyCallback {
    Location currentLocation;
    Location startPoint;
    MarkerOptions markerOptions;
    private GoogleMap mMap;
    private  float c = 5.0f;
    private DatabaseReference databaseReference;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    String currentuser;
    public ArrayList<Double> latitude;
    public ArrayList<Double> longitude;
    public ArrayList<String> name;
    public ArrayList<Float> distance;
    Button ten,hundred,thousand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapactivity);
        latitude = new ArrayList<>();
        longitude= new ArrayList<>();
        distance = new ArrayList<>();
        name= new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        //get current user id
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

         // created 28-07-2020
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                latitude.clear();
                longitude.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Object lat = dataSnapshot.child("latitude").getValue();
                    Object lng = dataSnapshot.child("longitude").getValue();
                    Object nameget = dataSnapshot.child("name").getValue();
                    String lngs ="0.0";
                    String lats = "0.0";
                    String names ="Default";
                    if (lat != null){
                        lats = lat.toString();
                        latitude.add(Double.parseDouble(lats));
                    }
                    if (lng != null){
                        lngs = lng.toString();
                        longitude.add(Double.parseDouble(lngs));
                    }
                    if (nameget != null){
                        names = nameget.toString();
                        name.add(names);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }

    public void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location)
            {

                if (location != null) {
                    currentLocation = location;
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Users").child(currentuser).child("latitude").setValue(currentLocation.getLatitude());
                    databaseReference.child("Users").child(currentuser).child("longitude").setValue(currentLocation.getLongitude());
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(mapactivity.this);
                    //created 30-07-2020
                    //calculating distance of others from user location
                    startPoint=new Location("User");
                    startPoint.setLatitude(currentLocation.getLatitude());
                    startPoint.setLongitude(currentLocation.getLongitude());
                    Location endPoint=new Location("User");
                    for (int counter = 0; counter < latitude.size(); counter++)
                    {
                        endPoint.setLatitude(latitude.get(counter));
                        endPoint.setLongitude(longitude.get(counter));
                        Float distanceuser = startPoint.distanceTo(endPoint);
                        distance.add(distanceuser);
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMaxZoomPreference(15.0f);
        LatLng india = new LatLng(20.5937 ,78.9629);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(india,4.2f));
        for (int counter = 0; counter < latitude.size(); counter++)
        {
            //made if case for making markers within a distance of 100km from user
            if((distance.get(counter)/1000)<10000)
            {
                LatLng latLng = new LatLng(latitude.get(counter), longitude.get(counter));
                markerOptions = new MarkerOptions().position(latLng).title(name.get(counter));
                mMap.addMarker(markerOptions);
            }
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng) {
                double lati = latlng.latitude;
                double lngi = latlng.longitude;
                startPoint.setLatitude(lati);
                startPoint.setLongitude(lngi);
            }
        });
        //created 27-07-2020
        //for zooming in on marker click
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                float zoomLevel = mMap.getCameraPosition().zoom;
                if((zoomLevel>=3.0f && zoomLevel <= 7.0f) || (c >= 5.0f && c<=7.0f) )
                {
                    c = c + 2;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), c), 1000, null);
                }
                else if(zoomLevel > 7.0f)
                    {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),15.0f));
                        CustomWindowAdapter adapter = new CustomWindowAdapter(mapactivity.this);
                        mMap.setInfoWindowAdapter(adapter);
                        marker.showInfoWindow();
                    }
                return true;
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    fetchLocation();
                }
                break;
        }
    }

        }


