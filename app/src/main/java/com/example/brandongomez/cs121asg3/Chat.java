package com.example.brandongomez.cs121asg3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.example.brandongomez.cs121asg3.response.QueryResponse;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Chat extends AppCompatActivity{

    String lat, lon, user_id, nickname;
    String getURL, postURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {

            } else {
                lat = extras.getString("lat");
                lon = extras.getString("lon");
                user_id = extras.getString("user_id");
                nickname = extras.getString("nickname");
            }
        } else {
            lat = (String) savedInstanceState.getSerializable("lat");
            lon = (String) savedInstanceState.getSerializable("lon");
            user_id = (String) savedInstanceState.getSerializable("user_id");
            nickname = (String) savedInstanceState.getSerializable("nickname");
        }
        TextView temp = (TextView)findViewById(R.id.textView3);
        temp.setText(lat + " " + lon + " " + nickname + " " + user_id);
    }
    // get the url information using retrofit
    public void getChat(View view){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/default/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        getChatService service = retrofit.create(getChatService.class);

        Call<QueryResponse> queryResponseCall = service.getChat("");

        //Call retrofit asynchronously
        queryResponseCall.enqueue(new Callback<QueryResponse>() {
            @Override
            public void onResponse(Response<QueryResponse> result_list) {
                //TextView post = (TextView)findViewById(R.id.postedMessages);
                //create a new text view
                try {
                    //insert stuff into text view
                    String temp = " " + result_list.body().resultList.size();
                    Log.i("TEST", " " + temp);
                } catch (Throwable t) {

                }
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed

            }
        });
    }

    public interface getChatService {
        @GET("get_messages?lat=9.99999&lng=10.0001&user_id=39")
        Call<QueryResponse> getChat(@Query("result_list") String result_list);
    }

    public interface postChatService {
        @GET("post_message?lat=9.9993&lng=10.0004&user_id=31&nickname=Hobbes&message=Tuna&message_id=8755")
        Call<QueryResponse> postChat();
    }
}
