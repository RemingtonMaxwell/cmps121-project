package com.example.brandongomez.overheards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jolina on 3/6/2016.
 */
public class FullPost extends AppCompatActivity{
    private ArrayList<CommentElement> aList;
    CommentAdapter adapter;
    ListView myListView;
    public TextView fp_username;
    public TextView fp_timestamp;
    public TextView fp_content;
    public TextView fp_upvotes;
    public Button fp_like;
    public Button fp_dislike;

    public FullPost(){}

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_post);
        aList = new ArrayList<CommentElement>();
        adapter = new CommentAdapter(this, R.layout.comment_element, aList);

        Intent intent = getIntent();
        Log.i("post", intent.getExtras().getString("test"));


        myListView = (ListView)findViewById(R.id.commentList);
        fp_username = (TextView)findViewById(R.id.username);
        fp_timestamp = (TextView)findViewById(R.id.timestamp);
        fp_content = (TextView)findViewById(R.id.content);
        fp_upvotes = (TextView)findViewById(R.id.votes);

    }

    @Override
    public void onResume(){
        super.onResume();
        myListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //fp_username.setText(pfpElement.username);
        //fp_timestamp.setText(pfpElement.time);
        //fp_content.setText(pfpElement.content);
        //fp_upvotes.setText(pfpElement.votes);

        getComments();
    }

    private void getComments(){
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
        aList.add(new CommentElement("SO YOU WANNA BE A MASTER OF POKEMON", "pic", "Cynthia","time"));
        aList.add(new CommentElement("UNDERSTAND THE SECRETS AND HAVE SOME FUN", "pic", "Wallace", "time"));
        aList.add(new CommentElement("SO YOU WANNA BE A MASTER OF POKEMON", "pic", "Juan", "time"));
        aList.add(new CommentElement("DO YOU HAVE THE SKILLS TO BE NUMBER ONE?", "pic", "Alder", "time"));
        adapter.notifyDataSetChanged();
    }
}
