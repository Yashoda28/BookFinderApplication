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

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.yashoda.bookfinderapplication.CommonUtils.handleException;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static ResultSet rs;
    private static String campusName;
    private static int index;
    private static Connectivity connectivity = new Connectivity();

    private Context context = MapsActivity.this;

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        index = sharedPref.getInt("key2", 0);
        query = "SELECT * FROM BOOK B WHERE B.BookID = " + index;

        populateMap();
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
            if ("WESTVILLE".contains(campusName)) {
                LatLng mark = new LatLng(-29.817897, 30.942771);
                Marker westville = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN Westville Campus"));
                westville.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            } else if ("HOWARD".contains(campusName)) {
                LatLng mark = new LatLng(-29.867145, 30.976453);
                Marker howard = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN Howard Campus"));
                howard.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            } else if ("EDGEWOOD".contains(campusName)) {
                LatLng mark = new LatLng(-29.817413, 30.846677);
                Marker edgewood = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN Edgewood Campus"));
                edgewood.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            } else if ("MEDICAL".contains(campusName)) {
                LatLng mark = new LatLng(-29.874364, 30.990272);
                Marker medical = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN Medical School"));
                medical.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            } else if ("PMB".contains(campusName)) {
                LatLng mark = new LatLng(-29.616819, 30.394263);
                Marker PMB = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN PMB Campus"));
                PMB.showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
            }
        } else {
            LatLng mark = new LatLng(-29.616819, 30.394263);
            Marker PMB = googleMap.addMarker(new MarkerOptions().position(mark).title("UKZN PMB Campus"));
            PMB.showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
        }
    }

    private void populateMap() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        rs = connectivity.getResultSet(query);
                        campusName = rs.getString(9);
                    } catch (final SQLException f) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                handleException(context, f, f.getMessage());
                            }
                        });
                    }
                }
            });
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                public void run() {
                    handleException(context, e, e.getMessage());
                }
            });
        }
    }

}
