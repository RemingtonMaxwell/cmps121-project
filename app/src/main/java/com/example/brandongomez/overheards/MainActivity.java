package com.example.brandongomez.overheards;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.brandongomez.overheards.R;
import com.example.brandongomez.overheards.OneFragment;
import com.example.brandongomez.overheards.TwoFragment;
import com.example.brandongomez.overheards.ThreeFragment;
import com.example.brandongomez.overheards.FourFragment;
import com.example.brandongomez.overheards.FiveFragment;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/*
    Tab design from:
    http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
 */

public class MainActivity extends AppCompatActivity {
    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


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
        String id = settings.getString("user_id", null);
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

    public void updateFirstName(View v){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addValueEventListener(new ValueEventListener() {
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
    public void updateLastName(View v){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addValueEventListener(new ValueEventListener() {
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
        database.addValueEventListener(new ValueEventListener() {
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
    public void updateEmailAddress(View v){
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase ref = database.child(getIntent().getExtras().getString("user_id"));
                Map<String, Object> updateSettings = new HashMap<String, Object>();
                EditText emailAddress=(EditText)findViewById(R.id.settings_email_address);
                updateSettings.put("emailAddress", emailAddress.getText().toString());
                ref.updateChildren(updateSettings);
                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
