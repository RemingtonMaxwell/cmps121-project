package com.example.brandongomez.overheards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


public class OneFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner mspin;
    private Spinner spinDisplay;
    private ArrayList<PostElement> aList;
    PostListAdapter adapter;
    ListView myListView;
    TextView fullPost;
    Button like;
    Button dislike;
    String spinnerdisplay = "all";
    String spinnerdisplaydisplay = "recent";


    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aList = new ArrayList<PostElement>();
        adapter = new PostListAdapter(this.getContext(), R.layout.post_element, aList);
        Log.i("First Fragment user id", "here");
        aList.clear();
        getPosts();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_one, container, false);

        //instantiate widgets
        myListView = (ListView) v.findViewById(R.id.listView);
        fullPost = (TextView)v.findViewById(R.id.more);
        mspin = (Spinner)v.findViewById(R.id.oneSpinner);
        spinDisplay = (Spinner)v.findViewById(R.id.spinner_display);

        mspin.setOnItemSelectedListener(this);
        spinDisplay.setOnItemSelectedListener(this);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        myListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        aList.clear();
        getPosts();
    }

    private void getPosts(){
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
        Query query;
        if(spinnerdisplaydisplay.equals("recent")) {
            query = database.orderByChild("timestamp").limitToLast(55);
        }
        else{
            query = database.orderByChild("votes").limitToFirst(55);
        }
        // Attach an listener to read the data at our posts reference
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                aList.clear();
                for (DataSnapshot postSnapshot : snapshot.child("posts").getChildren()) {
                    final Post post = postSnapshot.getValue(Post.class);
                            aList.add(0, new PostElement(post.getContent(),
                                    (String) snapshot.child("users").child(post.getUser_id()).child("profilePic").getValue(),
                                    (String) snapshot.child("users").child(post.getUser_id()).child("userName").getValue(),
                                    post.getTimestamp(), String.valueOf(post.getVotes()), post.getPost_id()));
                            adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String spinnerdisplay = mspin.getSelectedItem().toString();
        String spinnerdisplaydisplay = spinDisplay.getSelectedItem().toString();
        //Toast.makeText(this.getContext(), spinnerdisplay +" "+ spinnerdisplaydisplay, Toast.LENGTH_SHORT).show();
        spinnerdisplay = mspin.getSelectedItem().toString();
        spinnerdisplaydisplay = spinDisplay.getSelectedItem().toString();
        Toast.makeText(this.getContext(), spinnerdisplay +" "+ spinnerdisplaydisplay, Toast.LENGTH_SHORT).show();
        aList.clear();
        getPosts();

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
