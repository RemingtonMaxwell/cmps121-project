package com.example.brandongomez.overheards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {
    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    /*stores the text from the post box in a variable when user clicks post button
     NEED TO CHANGE LATER TO WORK WITH DATABASE AND POST MESSAGE TO SERVER INSTEAD
    */
    public void postInfo(View v){
        EditText inputMessage = (EditText) findViewById(R.id.postBox);
        EditText inputLocation = (EditText) findViewById(R.id.editText);//location editText
        String message = inputMessage.getText().toString();
        //MAYBE USE CURRENT LOCATION IF USER DOES NOT ENTER a LOCATION
        String location = inputLocation.getText().toString();
        System.out.println(message);
        System.out.println(location);
    }

    public void settingsView(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);//pass the cuisine to the search activity for searching

    }

}
