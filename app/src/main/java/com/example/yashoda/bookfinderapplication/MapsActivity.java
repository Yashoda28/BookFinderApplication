package com.example.yashoda.bookfinderapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static String campusName;

    private Context context = MapsActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        campusName = sharedPref.getString("key3", "PMB");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //This goes up to 21
        float zoomLevel = 16.0f;
        if (campusName != null) {
            campusName = campusName.toUpperCase();
            if (campusName.contains("WESTVILLE")) {
                LatLng mark = new LatLng(-29.817897, 30.942771);
                Marker westville = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN Westville Campus"));
                westville.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            } else if (campusName.contains("HOWARD")) {
                LatLng mark = new LatLng(-29.867145, 30.976453);
                Marker howard = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN Howard Campus"));
                howard.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            } else if (campusName.contains("EDGEWOOD")) {
                LatLng mark = new LatLng(-29.817413, 30.846677);
                Marker edgewood = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN Edgewood Campus"));
                edgewood.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            } else if (campusName.contains("MEDICAL")) {
                LatLng mark = new LatLng(-29.874364, 30.990272);
                Marker medical = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN Medical School"));
                medical.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            } else if (campusName.contains("PMB")) {
                LatLng mark = new LatLng(-29.616819, 30.394263);
                Marker PMB = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN PMB Campus"));
                PMB.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            }
        }
    }

}
