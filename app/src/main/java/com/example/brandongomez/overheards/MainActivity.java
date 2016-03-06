package com.example.brandongomez.overheards;

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
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
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
        adapter.addFragment(new TwoFragment(), "Recent");
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

    public void mapButton(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void submitWithCurr(View v){
        EditText txtDescription = (EditText)findViewById(R.id.overheard);
        DatePicker dateDescription = (DatePicker)findViewById(R.id.datePicker);
        TimePicker timeDescription = (TimePicker)findViewById(R.id.timePicker);
        String overheard = txtDescription.getText().toString();
        int year = dateDescription.getYear();
        int month = dateDescription.getMonth();
        int dayOfMonth = dateDescription.getDayOfMonth();
        int hour = timeDescription.getCurrentHour();
        int min = timeDescription.getCurrentMinute();
        GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth, hour, min, 0);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        fmt.setCalendar(date);
        String dateStr = fmt.format(date.getTime());
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String spinnerText = spinner.getSelectedItem().toString();
        final Location loc = new Location(36, -121);
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
        System.out.println(getIntent().getExtras().getString("user_id"));
        Firebase ref = database.child("users").child(getIntent().getExtras().getString("user_id")).child("currentLocation");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                latitude = (double)snapshot.child("latitude").getValue();
                longitude = (double)snapshot.child("longitude").getValue();
                loc.setLatitide(latitude);
                loc.setLongitude(longitude);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Post p = new Post(overheard, user_id, loc, spinnerText, dateStr);
        ref = database.child("posts").child(p.getPost_id());
        ref.setValue(p);
        Toast.makeText(this, "Overheard posted", Toast.LENGTH_SHORT).show();
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
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
        Firebase ref = database.child("users").child(getIntent().getExtras().getString("user_id")).child("currentLocation");
        ref.addValueEventListener(new ValueEventListener() {
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
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String spinnerText = spinner.getSelectedItem().toString();
        Post p = new Post(overheard, user_id, loc, spinnerText, dateStr);
        database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
        ref = database.child("posts").child(p.getPost_id());
        ref.setValue(p);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("message_id", p.getPost_id());
        startActivity(intent);
    }

}
