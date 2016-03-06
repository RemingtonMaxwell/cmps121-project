package com.example.brandongomez.overheards;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brandongomez.overheards.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FiveFragment extends Fragment {
    protected View mView;
    public FiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_five, container, false);
        mView=view;
        fillSettings();
        return view;
    }

    public void fillSettings(){
        Log.i("Fifth Fragment", "settings");
        String userId = getActivity().getIntent().getExtras().getString("user_id");
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/users/"+userId);
        // Attach an listener to read the data at our posts reference
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                EditText first_name = (EditText) mView.findViewById(R.id.settings_first_name);
                //first name
                ((EditText) mView.findViewById(R.id.settings_first_name)).setText((String) snapshot.child("firstName").getValue());
                //last name
                ((EditText) mView.findViewById(R.id.settings_last_name)).setText((String) snapshot.child("lastName").getValue());
                //user name
                ((EditText) mView.findViewById(R.id.settings_user_name)).setText((String) snapshot.child("userName").getValue());
                //email address
                ((EditText) mView.findViewById(R.id.settings_email_address)).setText((String) snapshot.child("emailAddress").getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

}
