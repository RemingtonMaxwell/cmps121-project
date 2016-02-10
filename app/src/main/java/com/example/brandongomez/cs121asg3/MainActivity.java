package com.example.brandongomez.cs121asg3;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private String user_id, message_id;
    String lat, lon, nickname, message;

    LocationData locationData = LocationData.getLocationData();//store location to share between activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button postButton = (Button) findViewById(R.id.postButton);
        postButton.setEnabled(false);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = settings.getString("user_id", null);
        if (user_id == null) {
            // Creates a random one, and sets it.
            SecureRandomString srs = new SecureRandomString();
            user_id = srs.nextString();
            SharedPreferences.Editor e = settings.edit();
            e.putString("user_id", user_id);
            e.commit();
        }

    }


    @Override
    public void onResume(){
        //check if user already gave permission to use location
        Boolean locationAllowed = checkLocationAllowed();
        Button postButton = (Button) findViewById(R.id.postButton);
        if(locationAllowed){
            requestLocationUpdate();
        }
        else {
            postButton.setEnabled(false);//search button must not be enabled until we have a location
        }
        //Set the button text between "Enable Location" or "Disable Location"
        render();
        super.onResume();
    }

    /*
    Check users location sharing setting
    */
    private boolean checkLocationAllowed(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean("location_allowed", false);
    }

    /*
    Persist users location sharing setting
    */
    private void setLocationAllowed(boolean allowed){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("location_allowed", allowed);
        editor.commit();
    }
    /*
	Set the button text between "Enable Location" or "Disable Location"
	 */
    private void render(){
        Boolean locationAllowed = checkLocationAllowed();
        Button button = (Button) findViewById(R.id.button);

        if(locationAllowed) {
            button.setText("Disable Location");
        }
        else {
            button.setText("Enable Location");
        }
    }
    /*
	Request location update. This must be called in onResume if the user has allowed location sharing
	 */
    private void requestLocationUpdate(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    /*
    Remove location update. This must be called in onPause if the user has allowed location sharing
     */
    private void removeLocationUpdate() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.removeUpdates(locationListener);
            }
        }
    }

    public void toggleLocation(View v) {

        Boolean locationAllowed = checkLocationAllowed();

        if(locationAllowed){
            //disable it
            removeLocationUpdate();
            setLocationAllowed(false);//persist this setting

            Button postButton = (Button) findViewById(R.id.postButton);
            postButton.setEnabled(false);//now that we cannot use location, we should disable search facility

        } else {
            //enable it
            requestLocationUpdate();
            setLocationAllowed(true);//persist this setting

        }

        //Set the button text between "Enable Location" or "Disable Location"
        render();
    }
    /**
     * Listens to the location, and gets the most precise recent location.
     * Copied from Prof. Luca class code
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Location lastLocation = locationData.getLocation();

            // Do something with the location you receive.
            double newAccuracy = location.getAccuracy();

            long newTime = location.getTime();
            // Is this better than what we had?  We allow a bit of degradation in time.
            boolean isBetter = ((lastLocation == null) ||
                    newAccuracy < lastLocation.getAccuracy() + (newTime - lastLocation.getTime()));
            if (isBetter) {
                // We replace the old estimate by this one.
                locationData.setLocation(location);

                //Now we have the location.
                Button postButton = (Button) findViewById(R.id.postButton);
                if(checkLocationAllowed() && lat != "") {
                    postButton.setEnabled(true);//We must enable search button
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    public void post(View v){
        EditText editNickname = (EditText) findViewById(R.id.editNickname);
        nickname = editNickname.getText().toString();
        lat = Double.toString(locationData.getLocation().getLatitude());
        lon = Double.toString(locationData.getLocation().getLongitude());
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        intent.putExtra("user_id", user_id);
        intent.putExtra("nickname", nickname);
        String Chelsea = "Chelsea 143";
        Log.i("CHELSEA_LOG", Chelsea);
        startActivity(intent);//pass the cuisine to the search activity for searching
    }
}
