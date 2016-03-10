package com.example.brandongomez.overheards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ImageView fp_imageview;
    public Button fp_like;
    public Button fp_dislike;
    private String post_id;
    private String user_id;

    public FullPost(){}

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_post);
        aList = new ArrayList<CommentElement>();
        adapter = new CommentAdapter(this, R.layout.comment_element, aList);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor e = settings.edit();
        user_id = settings.getString("user_id", null);
        Intent intent = getIntent();
        Log.i("post", intent.getExtras().getString("post_id"));
        post_id=intent.getExtras().getString("post_id");
        myListView=(ListView) findViewById(R.id.commentList);
        fp_username=(TextView) findViewById(R.id.fp_username);
        fp_timestamp=(TextView) findViewById(R.id.fp_timestamp);
        fp_content=(TextView) findViewById(R.id.fp_content);
        fp_upvotes=(TextView) findViewById(R.id.fp_votes);
        fp_imageview=(ImageView) findViewById(R.id.fp_imageView);
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
            displayPost();
    }

    public void addComment(View v){
        final String comment=((EditText) findViewById(R.id.add_comment)).getText().toString();
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Post post=snapshot.child("posts").child(post_id).getValue(Post.class);
                User user = snapshot.child("users").child(user_id).getValue(User.class);
                Comment c = new Comment(post_id,user_id,comment);
                Firebase ref1 = database.child("comments").child(c.getComment_id());
                user.addComment(c.getComment_id());
                //set reference for comment in comments
                ref1.setValue(c);
                //set reference for user's list of comments
                Firebase ref = database.child("users").child(user_id);
                ref.setValue(user);
                //set reference for post's list of comments
                ref = database.child("posts").child(post_id);
                post.addComment(c.getComment_id());
                ref.setValue(post);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ((EditText) findViewById(R.id.add_comment)).setText("");
        Toast.makeText(this, "Comment posted.", Toast.LENGTH_SHORT).show();
        getComments();







































































































































































































































































































































































































































































































































































































































































    }


    private void getComments(){
        //aList.clear();
       // aList.add(new CommentElement("SO YOU WANNA BE A MASTER OF POKEMON", "pic", "Cynthia","time"));
       // aList.add(new CommentElement("UNDERSTAND THE SECRETS AND HAVE SOME FUN", "pic", "Wallace", "time"));
       // aList.add(new CommentElement("SO YOU WANNA BE A MASTER OF POKEMON", "pic", "Juan", "time"));
       // aList.add(new CommentElement("DO YOU HAVE THE SKILLS TO BE NUMBER ONE?", "pic", "Alder", "time"));
        final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Post post=snapshot.child("posts").child(post_id).getValue(Post.class);
                List<String> myComments=post.getComments();
                for(int i=0;i<myComments.size();i++){
                    String comment=myComments.get(i);
                    Comment com=snapshot.child("comments").child(comment).getValue(Comment.class);
                    String content=com.getContent();
                    String timestamp=com.getTimestamp();
                    User user =snapshot.child("users").child(com.getUser_id()).getValue(User.class);
                    String userName=user.getUserName();
                    String profilePic=user.getProfilePic();
                    aList.add(0,new CommentElement(content,profilePic,userName,timestamp));
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
            adapter.notifyDataSetChanged();
        }

    private void displayPost(){
        Firebase.setAndroidContext(this);
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Post post=snapshot.child("posts").child(post_id).getValue(Post.class);
                fp_content.setText(post.getContent());
                fp_upvotes.setText(Integer.toString(post.getVotes()));
                fp_username.setText((String) snapshot.child("users").child(post.getUser_id()).child("userName").getValue());
                fp_timestamp.setText(post.getTimestamp());
                byte[] imageAsBytes = Base64.decode((String) snapshot.child("users").child(post.getUser_id()).child("profilePic").getValue(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                fp_imageview.setImageBitmap(bitmap);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });



    }
}
