package com.example.yashoda.bookfinderapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String campusName;
    SharedPreferences sharedPref;
    int index;
    Context context = MapsActivity.this;
    Connectivity connectivity = new Connectivity();
    LatLng mark;
    float zoomLevel = 16.0f; //This goes up to 21

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        index = sharedPref.getInt("key2",0);
        try {
            ResultSet rs = connectivity.getResultSet(getlocationDetails(index));
            //campusName = rs.getString(9);


        } catch (SQLException e) {
            e.printStackTrace();
        };
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
        mMap = googleMap;
        campusName="UKZN Westville Campus";
        if (campusName.contains("Westville"))
        {
            LatLng mark = new LatLng(-29.817897, 30.942771);
            Marker westville = mMap.addMarker(new MarkerOptions().position(mark).title("UKZN Westville Campus"));
            westville.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
        }

        else if (campusName.contains("Howard"))
        {
            LatLng mark = new LatLng(-29.867145, 30.976453);
            Marker howard = mMap.addMarker(new MarkerOptions().position(mark).title("UKZN Howard Campus"));
            howard.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
        }
        else  if (campusName.contains("Edgewood"))
        {
            LatLng mark = new LatLng(-29.817413, 30.846677);
            Marker edgewood = mMap.addMarker(new MarkerOptions().position(mark).title("UKZN Edgewood Campus"));
            edgewood.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
        }
        else  if (campusName.contains("Medical"))
        {
            LatLng mark = new LatLng(-29.874364, 30.990272);
            Marker medical = mMap.addMarker(new MarkerOptions().position(mark).title("UKZN Medical School"));
            medical.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
        }
        else  if (campusName.contains("PMB"))
        {
            LatLng mark = new LatLng(-29.616819, 30.394263);
            Marker PMB = mMap.addMarker(new MarkerOptions().position(mark).title("UKZN PMB Campus"));
            PMB.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, zoomLevel));
        }

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private String getlocationDetails(int index) {
        return "SELECT LOCATIONDETAILS FROM BOOK B WHERE B.BookID='" + index + "'";
    }
}
