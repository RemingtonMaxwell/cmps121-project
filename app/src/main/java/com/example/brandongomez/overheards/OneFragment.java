package com.example.brandongomez.overheards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class OneFragment extends Fragment {
    protected View mView;
    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("First Fragment user id", "here");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TextView nicknameText=(TextView)findViewById(R.id.test);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_one, container, false);
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        mView=view;
        loadPosts();
        return view;
    }

    public void loadPosts(){
        Log.i("First Fragment user id", "loadposts");
        final TextView test = (TextView) mView.findViewById(R.id.test);
        //User user=
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users");
        // Attach an listener to read the data at our posts reference
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

}
