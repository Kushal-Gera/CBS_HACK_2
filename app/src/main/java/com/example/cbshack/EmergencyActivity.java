package com.example.cbshack;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.microsoft.azure.maps.mapcontrol.AzureMap;
import com.microsoft.azure.maps.mapcontrol.AzureMaps;
import com.microsoft.azure.maps.mapcontrol.MapControl;
import com.microsoft.azure.maps.mapcontrol.events.OnReady;
import com.microsoft.azure.maps.mapcontrol.layer.SymbolLayer;
import com.microsoft.azure.maps.mapcontrol.source.DataSource;

import static com.microsoft.azure.maps.mapcontrol.options.CameraOptions.center;
import static com.microsoft.azure.maps.mapcontrol.options.CameraOptions.zoom;
import static com.microsoft.azure.maps.mapcontrol.options.SymbolLayerOptions.iconImage;

public class EmergencyActivity extends AppCompatActivity {

    static {
        AzureMaps.setSubscriptionKey("_iGvSdHd-lg_xNYPLyE271WrMULE9fF6nm78MJkr-hg");
    }

    private FusedLocationProviderClient fusedLocationClient;
    MapControl mapControl;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        mapControl = findViewById(R.id.mapcontrol);

        mapControl.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null ) {
                            loc = location;
                        }
                    }
                });


        //Wait until the map resources are ready.
        mapControl.onReady(map -> {
            //Add your post map load code here.
            map.setCamera(center(loc.getLatitude(), loc.getLongitude()), zoom(12.5));

            //Create a data source and add it to the map.
            DataSource dataSource = new DataSource();
            map.sources.add(dataSource);

            //Create a point feature and add it to the data source.
            dataSource.add(Feature.fromGeometry(Point.fromLngLat(loc.getLongitude(), loc.getLatitude())));

            //Add a custom image icon to the map resources.
            map.images.add("my-icon", R.drawable.mapcontrol_point_circle_blue_o);

            //Create a symbol layer and add it to the map.
            map.layers.add(new SymbolLayer(dataSource,
                    iconImage("my-icon")));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapControl.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapControl.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapControl.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapControl.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapControl.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapControl.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapControl.onSaveInstanceState(outState);
    }


}
