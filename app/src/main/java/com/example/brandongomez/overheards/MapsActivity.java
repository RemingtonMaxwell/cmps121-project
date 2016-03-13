package com.example.brandongomez.overheards;

import android.location.*;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff;
import android.graphics.Bitmap;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.ui.IconGenerator;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.SimpleTimeZone;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnInfoWindowClickListener, OnMapReadyCallback, OnItemSelectedListener, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>{

    private GoogleMap mMap;
    final Context context = this;
    Marker marker;
    LatLng latLng;
    Geocoder geocoder;
    String post_id;
    boolean posted = false;
    // Declare a variable for the cluster manager.
    ClusterManager<MyItem> mClusterManager;
    private MyItem clickedClusterItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        try {
            Bundle extras = getIntent().getExtras();
            post_id = extras.getString("message_id");
        }
        catch (Exception e) {

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this, Locale.getDefault());
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
        Spinner spin = (Spinner) findViewById(R.id.timeSpinner);
        spin.setOnItemSelectedListener(this);
        Spinner spin2 = (Spinner) findViewById(R.id.categorySpinner);
        spin2.setOnItemSelectedListener(this);
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
                        //Toast.makeText(context, "GPS is disabled", Toast.LENGTH_SHORT).show();
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
        if(post_id != null && posted == false) {
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
                        //Toast.makeText(MapsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                    }

                    //remove previously placed Marker
                    if (marker != null) {
                        marker.remove();
                    }

                    if(posted == false) {
                        //place marker where user just clicked
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title("Marker")
                                .title("USER NAME")
                                .snippet("OVERHEARD")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


                        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.child("posts").hasChild(post_id)) {
                                    Firebase ref = database.child("posts").child(post_id).child("location");
                                    Map<String, Object> updateMap = new HashMap<String, Object>();
                                    updateMap.put("latitude", latLng.latitude);
                                    updateMap.put("longitude", latLng.longitude);
                                    ref.updateChildren(updateMap);
                                    addPost(snapshot.child("posts").child(post_id).getValue(Post.class));
                                    for (DataSnapshot postSnapshot : snapshot.child("posts").getChildren()) {
                                        Post post = postSnapshot.getValue(Post.class);
                                        if (post.getPost_id().compareTo(post_id) == 0) {
                                            marker.setTitle((String) snapshot.child("users").child(post.getUser_id()).child("userName").getValue());
                                            marker.setSnippet(post.getContent());
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }

                        });
                        Toast.makeText(context, "Overheard posted", Toast.LENGTH_SHORT).show();
                    }
                    posted = true;
                }
            });
        }
        setUpClusterer();
        //need to trigger selection events
        spin.setSelection(1);
        //spin2.setSelection(1);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

    }
    public void addPost(final Post p){
        final Firebase ref = new Firebase("https://vivid-heat-3338.firebaseio.com/users/"+p.getUser_id());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                user.addPost(p.getPost_id());
                ref.setValue(user);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
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

    public void onItemSelected (AdapterView < ? > parent, View view, final int pos, long id){
        removeMarkers();
        Firebase.setAndroidContext(this);
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
        // Attach an listener to read the data at our posts reference
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.child("posts").getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
                    SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
                    GregorianCalendar date = new GregorianCalendar(pdt);
                    Spinner spinner = (Spinner) findViewById(R.id.timeSpinner);
                    String spinnerText = spinner.getSelectedItem().toString();
                    String dateHeard = post.getDate_heard();
                    String[] parsedDateHeard = dateHeard.split("/");
                    String[] parsedTimeHeard = parsedDateHeard[2].split(" ");
                    Spinner typeSpinner = (Spinner)findViewById(R.id.categorySpinner);
                    String typeSpinnerText = typeSpinner.getSelectedItem().toString();
                    String typePosted = post.getType();
                    Log.i("overheads map", "post is"+post.getContent());
                    Log.i("overheards map","year heard is"+parsedDateHeard[0] );
                    Log.i("overheards map","month heard is"+parsedDateHeard[1] );
                    Log.i("overheards map","year heard is"+parsedTimeHeard[0] );
                    if (spinnerText.equals("this year")) {
                        if (Integer.valueOf(parsedDateHeard[0]) == date.get(Calendar.YEAR)) {
                            if(typeSpinnerText.equals(typePosted) || typeSpinnerText.equals("all")) {
                                MyItem offsetItem = new MyItem(post.getLocation().getLatitude(),
                                        post.getLocation().getLongitude(),
                                        (String) snapshot.child("users").child(post.getUser_id()).child("userName").getValue(),
                                        post.getContent());
                                offsetItem.setPostId(post.getPost_id());
                                    mClusterManager.addItem(offsetItem);
                            }
                        }
                    }
                    if (spinnerText.equals("this month")) {
                        if (Integer.valueOf(parsedDateHeard[0]) == date.get(Calendar.YEAR)) {
                            if (Integer.valueOf(parsedDateHeard[1]) == (date.get(Calendar.MONTH) + 1)) {
                                if(typeSpinnerText.equals(typePosted) || typeSpinnerText.equals("all")) {
                                    MyItem offsetItem = new MyItem(post.getLocation().getLatitude(),
                                            post.getLocation().getLongitude(),
                                            (String) snapshot.child("users").child(post.getUser_id()).child("userName").getValue(),
                                            post.getContent());
                                    offsetItem.setPostId(post.getPost_id());
                                    mClusterManager.addItem(offsetItem);
                                }
                            }
                        }
                    }
                    if (spinnerText.equals("today")) {
                        if (Integer.valueOf(parsedDateHeard[0]) == date.get(Calendar.YEAR)) {
                            if (Integer.valueOf(parsedDateHeard[1]) == (date.get(Calendar.MONTH) + 1)) {
                                if (Integer.valueOf(parsedTimeHeard[0]) == date.get(Calendar.DAY_OF_MONTH)) {
                                    if(typeSpinnerText.equals(typePosted) || typeSpinnerText.equals("all")) {
                                        MyItem offsetItem = new MyItem(post.getLocation().getLatitude(),
                                                post.getLocation().getLongitude(),
                                                (String) snapshot.child("users").child(post.getUser_id()).child("userName").getValue(),
                                                post.getContent());
                                        offsetItem.setPostId(post.getPost_id());
                                        mClusterManager.addItem(offsetItem);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void onNothingSelected(AdapterView parent) {

    }


    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mClusterManager
                .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
                    @Override
                    public boolean onClusterItemClick(MyItem item) {
                        clickedClusterItem = item;
                        return false;
                    }
                });

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new MyCustomAdapterForItems());
        mClusterManager.setRenderer(new MyClusterRenderer(this, mMap,
                mClusterManager));

    }


    //added with edit
    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
        /*
        //Cluster item InfoWindow clicked, set title as action
        //Intent i = new Intent(this, OtherActivity.class);
        //i.setAction(myItem.getTitle());
        //startActivity(i);

        //You may want to do different things for each InfoWindow:
        if (myItem.getTitle().equals("some title")){

            //do something specific to this InfoWindow....

        }
        */
        Intent intent = new Intent(context, FullPost.class);
        intent.putExtra("post_id", myItem.getPostId());
        context.startActivity(intent);

    }

    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());

        MyCustomAdapterForItems() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.info_window, null);
        }
        @Override
        public View getInfoWindow(Marker marker) {

            TextView tvTitle = ((TextView) myContentsView
                    .findViewById(R.id.txtTitle));
            TextView tvSnippet = ((TextView) myContentsView
                    .findViewById(R.id.txtSnippet));

            tvTitle.setText(clickedClusterItem.getTitle());
            tvSnippet.setText(clickedClusterItem.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

    }

    public class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {

        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());

        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item,
                                                   MarkerOptions markerOptions) {

            BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);

            markerOptions.icon(markerDescriptor);
        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions){

            final Drawable clusterIcon = getResources().getDrawable(R.drawable.bubble_mask);
            clusterIcon.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

            mClusterIconGenerator.setBackground(clusterIcon);

            //modify padding for one or two digit numbers
            if (cluster.getSize() < 10) {
                mClusterIconGenerator.setContentPadding(40, 20, 0, 0);
            }
            else {
                mClusterIconGenerator.setContentPadding(30, 20, 0, 0);
            }

            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }
    }

    private void removeMarkers() {
        mMap.clear();
        mClusterManager.clearItems();
    }



}
