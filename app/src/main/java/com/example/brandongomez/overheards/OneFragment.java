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

import java.util.ArrayList;
import java.util.List;


public class OneFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner mspin;
    private Spinner spinDisplay;
    private ArrayList<PostElement> aList;
    PostListAdapter adapter;
    ListView myListView;
    TextView fullPost;
    Button like;
    Button dislike;


    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aList = new ArrayList<PostElement>();
        adapter = new PostListAdapter(this.getContext(), R.layout.post_element, aList);
        Log.i("First Fragment user id", "here");

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
        getPosts();
    }

    private void getPosts(){
        /* HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/default/")
                .addConverterFactory(GsonConverterFactory.create())    //parse Gson string
                .client(httpClient)    //add logging
                .build();

        getMessagesApi getMessageService = retrofit.create(getMessagesApi.class);

        Call<GetMessages> getMessages = getMessageService.getMessagesFromApi(MainActivity.lat, MainActivity.lng, MainActivity.user_id);

        //Call retrofit asynchronously
        getMessages.enqueue(new Callback<GetMessages>() {
            @Override
            public void onResponse(Response<GetMessages> response) {
                List<ResultList> results = getMessages(response.body());
                aList.clear();
                Log.i(CHAT_LOG_TAG, String.valueOf(results.size()));
                for (int i = results.size() - 1; i >= 0; i--) {
                    Log.i(CHAT_LOG_TAG, results.get(i).getNickname());
                    Log.i(CHAT_LOG_TAG, results.get(i).getMessage());
                    Log.i(CHAT_LOG_TAG, results.get(i).getMessageId());
                    Log.i(CHAT_LOG_TAG, results.get(i).getTimestamp());
                    Log.i(CHAT_LOG_TAG, results.get(i).getUserId());
                    aList.add(new ListElement(results.get(i).getTimestamp(), results.get(i).getMessage(), results.get(i).getNickname(), results.get(i).getMessageId(), results.get(i).getUserId()));
                }
                adapter.notifyDataSetChanged();*/
        aList.clear();
        aList.add(new PostElement("I WANT TO BE THE VERY BEST", "pic", "Ash Ketchum","time","0"));
        aList.add(new PostElement("THAT NO ONE EVERY WAS", "pic", "Misty", "time", "0"));
        aList.add(new PostElement("TO CATCH THEM IS MY REAL QUEST", "pic", "null", "time", "0"));
        aList.add(new PostElement("TO TRAIN THEM IS MY CAUSE", "pic", "String Not Found", "time", "0"));
        adapter.notifyDataSetChanged();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String spinnerdisplay = mspin.getSelectedItem().toString();
        String spinnerdisplaydisplay = spinDisplay.getSelectedItem().toString();
        //Toast.makeText(this.getContext(), spinnerdisplay +" "+ spinnerdisplaydisplay, Toast.LENGTH_SHORT).show();
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
