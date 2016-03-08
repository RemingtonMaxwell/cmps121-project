package com.example.brandongomez.overheards;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.brandongomez.overheards.R;

public class FourFragment extends Fragment {

    public FourFragment() {
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
        return inflater.inflate(R.layout.fragment_four, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ProgressBar spinner;
        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);

        //spinner.setVisibility(View.GONE);
        //spinner.setVisibility(View.VISIBLE);

    }


}
