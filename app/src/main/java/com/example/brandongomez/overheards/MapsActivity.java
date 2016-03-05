package com.example.brandongomez.overheards;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.location.LocationManager;
import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.location.Address;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import android.location.Geocoder;
import java.util.Locale;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnInfoWindowClickListener, OnMapReadyCallback{

    private GoogleMap mMap;
    final Context context = this;
    Marker marker;
    LatLng latLng;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        Firebase.setAndroidContext(this);
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        // Attach an listener to read the data at our posts reference
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println("There are " + snapshot.getChildrenCount() + " users");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    //System.out.println(user.getEmailAddress());
                    for (int i = 0; i < user.getLocations().size(); i++) {
                        addMarker(user.getLocations().get(i).getLatitude(),
                                user.getLocations().get(i).getLongitude(),
                                user.getEmailAddress(),
                                "HI");
                    }
                    //System.out.println(user.getUser_id());
                    //use this to check user id
                    //something happening in logactivity-not creating user in authentication
                    //but yet user is in database
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Santa Cruz.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Checks to see if permissions are available
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                // If user presses the location button but gps not found, then show a Toast
                public boolean onMyLocationButtonClick() {
                    LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Toast.makeText(context, "GPS is disabled", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        } else {
            // Alert if permissions not available
            Toast.makeText(this, "Permission not available", Toast.LENGTH_SHORT).show();
        }
        mMap.setOnInfoWindowClickListener(this);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // drops a marker and gets the address of the marker
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                //save current location
                latLng = point;

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);

                if (address != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i) + "\n");
                    }
                    Toast.makeText(MapsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                }

                //remove previously placed Marker
                if (marker != null) {
                    marker.remove();
                }

                //place marker where user just clicked
                marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker")
                        .title("USER NAME")
                        .snippet("OVERHEARD")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // TODO: make it so it goes to the appropriate Overheards screen
        Toast.makeText(this, "Info window clicked", Toast.LENGTH_SHORT).show();
    }

    public void changeView(View v) {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        }
    }

    public void addMarker(double lat, double lon, String username, String overheard){
        LatLng POINT = new LatLng(lat, lon);
        Marker Mark = mMap.addMarker(new MarkerOptions()
                .position(POINT)
                .title(username)
                .snippet(overheard)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
    }

}
