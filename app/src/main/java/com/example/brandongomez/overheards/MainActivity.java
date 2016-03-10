package com.example.brandongomez.overheards;


import android.content.Context;
import android.content.Intent;

import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;

import android.view.ViewGroup;

import android.view.View;
import android.util.Log;
import android.view.View;
import android.content.Intent;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;



import com.example.brandongomez.overheards.Post;
import com.example.brandongomez.overheards.Location;
import com.example.brandongomez.overheards.R;
import com.example.brandongomez.overheards.OneFragment;
import com.example.brandongomez.overheards.TwoFragment;
import com.example.brandongomez.overheards.ThreeFragment;
import com.example.brandongomez.overheards.FourFragment;
import com.example.brandongomez.overheards.FiveFragment;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/*
    Tab design from:
    http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
 */

public class MainActivity extends AppCompatActivity{
    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String user_id;
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        Firebase.setAndroidContext(this);
        Log.i("Main Activity user id", intent.getStringExtra(LoginActivity.USER_ID));
        //checking user preferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor e = settings.edit();
        user_id = settings.getString("user_id", null);
        Log.i("Main Activity", intent.getStringExtra(LoginActivity.USER_ID));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Top");
        adapter.addFragment(new ThreeFragment(), "Map");
        adapter.addFragment(new FourFragment(), "Post");
        adapter.addFragment(new FiveFragment(), "You");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    /*stores the text from the post box in a variable when user clicks post button
     NEED TO CHANGE LATER TO WORK WITH DATABASE AND POST MESSAGE TO SERVER INSTEAD
    */


    public void settingsView(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        Bundle b = getIntent().getExtras();
        String pass = b.getString("password");
        String userID = b.getString("user_id");
        String em = b.getString("email");
        intent.putExtra("password", pass);
        intent.putExtra("user_id", userID);
        intent.putExtra("email", em);
        startActivity(intent);//pass the cuisine to the search activity for searching

    }



    public void viewFullPost(View v){
        Intent intent = new Intent(this, FullPost.class);
        intent.putExtra("test", "test");
        startActivity(intent);
    }

    public void mapButton(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    public void updateFirstName(View v) {
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                EditText firstName = (EditText) findViewById(R.id.settings_first_name);
                updateSettings.put("firstName", firstName.getText().toString());
                ref.updateChildren(updateSettings);
                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void submitWithCurr(View v){
        final EditText txtDescription = (EditText)findViewById(R.id.overheard);
        final DatePicker dateDescription = (DatePicker)findViewById(R.id.datePicker);
        final TimePicker timeDescription = (TimePicker)findViewById(R.id.timePicker);
        final String overheard = txtDescription.getText().toString();
        int year = dateDescription.getYear();
        int month = dateDescription.getMonth();
        int dayOfMonth = dateDescription.getDayOfMonth();
        int hour = timeDescription.getCurrentHour();
        int min = timeDescription.getCurrentMinute();
        GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth, hour, min, 0);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        fmt.setCalendar(date);
        final String dateStr = fmt.format(date.getTime());
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        final String spinnerText = spinner.getSelectedItem().toString();
        if(!overheard.equals("")) {
            final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
            System.out.println(getIntent().getExtras().getString("user_id"));
            final Firebase ref = database.child("users").child(getIntent().getExtras().getString("user_id")).child("currentLocation");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    latitude = (double) snapshot.child("latitude").getValue();
                    longitude = (double) snapshot.child("longitude").getValue();
                    Post p = new Post(overheard, user_id, new Location(latitude, longitude), spinnerText, dateStr);
                    Firebase ref1 = database.child("posts").child(p.getPost_id());
                    addPost(p);
                    ref1.setValue(p);
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            Toast.makeText(this, "Overheard posted", Toast.LENGTH_SHORT).show();
            txtDescription.getText().clear();
        }
        else{
            Toast.makeText(this, "Write a message first", Toast.LENGTH_SHORT).show();
        }
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
    public void updateLastName(View v){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                EditText lastName=(EditText)findViewById(R.id.settings_last_name);
                updateSettings.put("lastName", lastName.getText().toString());
                ref.updateChildren(updateSettings);
                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void updateUserName(View v){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                EditText userName=(EditText)findViewById(R.id.settings_user_name);
                updateSettings.put("userName", userName.getText().toString());
                ref.updateChildren(updateSettings);
                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void submitOverheard(View v){
        EditText txtDescription = (EditText)findViewById(R.id.overheard);
        DatePicker dateDescription = (DatePicker)findViewById(R.id.datePicker);
        TimePicker timeDescription = (TimePicker)findViewById(R.id.timePicker);
        String overheard = txtDescription.getText().toString();
        int year = dateDescription.getYear();
        int month = dateDescription.getMonth();
        int dayOfMonth = dateDescription.getDayOfMonth();
        int hour = timeDescription.getCurrentHour();
        int min = timeDescription.getCurrentMinute();
        final Location loc = new Location(36, -121);
        if(!overheard.equals("")) {
            Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
            Firebase ref = database.child("users").child(getIntent().getExtras().getString("user_id")).child("currentLocation");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    latitude = (double) snapshot.child("latitude").getValue();
                    longitude = (double) snapshot.child("longitude").getValue();
                    loc.setLatitide(latitude);
                    loc.setLongitude(longitude);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });
            GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth, hour, min, 0);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            fmt.setCalendar(date);
            String dateStr = fmt.format(date.getTime());
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            String spinnerText = spinner.getSelectedItem().toString();
            Post p = new Post(overheard, user_id, loc, spinnerText, dateStr);
            database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
            ref = database.child("posts").child(p.getPost_id());
            ref.setValue(p);
            txtDescription.getText().clear();
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("message_id", p.getPost_id());
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Write a message first", Toast.LENGTH_SHORT).show();
        }

    }
    public void updateEmailAddress(View v){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                final EditText emailAddress = (EditText) findViewById(R.id.settings_email_address);
                updateSettings.put("emailAddress", emailAddress.getText().toString());
                Firebase refUser = new Firebase("https://vivid-heat-3338.firebaseio.com/users/" + getIntent().getStringExtra(LoginActivity.USER_ID));
                updateUserEmailAddress();
                //send updates to server
                ref.updateChildren(updateSettings);
                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void updateUserEmailAddress(){
        //get the old email and password
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        final Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                final EditText emailAddress=(EditText)findViewById(R.id.settings_email_address);
                ref.changeEmail(getIntent().getStringExtra(LoginActivity.EMAIL), getIntent().getStringExtra(LoginActivity.PASSWORD), emailAddress.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        // email changed
                        System.out.println("email changed");
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // error encountered
                    }
                });
    }
    public void updatePassword(View v){
        final Firebase ref = new Firebase("https://vivid-heat-3338.firebaseio.com");
        final EditText password = (EditText) findViewById(R.id.settings_password);
        ref.changePassword(getIntent().getExtras().getString("email"), getIntent().getExtras().getString("password"), password.getText().toString(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }

    public void logout(View v){
        final Firebase ref = new Firebase("https://vivid-heat-3338.firebaseio.com");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor e = settings.edit();
        e.putString("email",null);
        e.putString("password",null);
        e.putString("user_id", null);
        e.commit();
        ref.unauth();
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }



}
