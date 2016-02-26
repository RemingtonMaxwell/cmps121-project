package com.example.brandongomez.overheards;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        // Add a marker in Sydney and move the camera
        LatLng santaCruz = new LatLng(36.9719, -122.0264);
        mMap.addMarker(new MarkerOptions().position(santaCruz).title("Here is some information"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(santaCruz, 15.0f));

        // Checks to see if permissions are available
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                // If user presses the location button but gps not found, then show a Toast
                public boolean onMyLocationButtonClick() {
                    LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        } else {
            // Alert if permissions not available
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Something went wrong");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // here you can add functions
                }
            });
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.show();
        }
    }

    public void addLocation(View v) {
        Random r = new Random();
        float lat = r.nextFloat() * (37.05f - 36.95f) + 36.95f;
        float lon = r.nextFloat() * (122.05f - 121.95f) + 121.95f;
        LatLng santaCruz2 = new LatLng(lat, -lon);
        mMap.addMarker(new MarkerOptions().position(santaCruz2).title("Here is some information"));
    }

}
