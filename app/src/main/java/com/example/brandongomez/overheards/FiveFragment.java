package com.example.brandongomez.overheards;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.EditText;
import android.widget.Toast;



import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

public class FiveFragment extends Fragment {
    protected View mView;
    public FiveFragment() {
        // Required empty public constructor
    }

    private MyAdapter aa;
    private MyAdapterComments commentAdapter;

    private ArrayList<ListElement> aList;
    private ArrayList<ListElement> cList;

    private class ListElement {
        ListElement() {
        }
        //constructor for posts
        ListElement(String tl, String t2, String post_id) {
            content = tl;
            timestamp = t2;
            this.post_id=post_id;
            comment_id="";
        }
        ListElement(String tl, String t2, String post_id, String comment_id) {
            content = tl;
            timestamp = t2;
            this.post_id=post_id;
            this.comment_id=comment_id;
        }

        private String content;
        private String timestamp;
        private String post_id;
        private String comment_id;

        public String getPostId(){
            return post_id;
        }


        public String getCommentId(){
            return comment_id;
        }
    }
    public class MyAdapter extends ArrayAdapter<ListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LinearLayout newView;

            ListElement w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            TextView tv = (TextView) newView.findViewById(R.id.itemText2);
            TextView tv2 = (TextView) newView.findViewById(R.id.itemText);

            tv.setText(w.content);
            tv2.setText(w.timestamp);


            newView.setTag(w.content);
            newView.setTag(w.timestamp);
            TextView deletePost = (TextView) newView.findViewById(R.id.delete_post);
            deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("overheards", "in posts");
                    getPosts(newView);
                }
            });

            return newView;
        }
    }
    public class MyAdapterComments extends ArrayAdapter<ListElement> {

        int resource;
        Context context;

        public MyAdapterComments(Context _context, int _resource, List<ListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final LinearLayout newView;

            final ListElement w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            TextView tv = (TextView) newView.findViewById(R.id.itemText2);
            TextView tv2 = (TextView) newView.findViewById(R.id.itemText);

            tv.setText(w.content);
            tv2.setText(w.timestamp);


            newView.setTag(w.content);
            newView.setTag(w.timestamp);
            TextView deleteComment = (TextView) newView.findViewById(R.id.delete_post);
            final Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
            deleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("overheards", "in comments");
                    //delete comment from database
                    Firebase comment = database.child("comments").child(w.getCommentId());
                    Log.i("comment removed is", w.getCommentId());
                    comment.removeValue();
                    //delete comment from post
                    final Firebase postData = database.child("posts").child(w.getPostId());
                    postData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Post post = snapshot.getValue(Post.class);
                            Log.i("comments are", post.getComments().toString());
                            post.removeComment(w.getCommentId());
                            postData.setValue(post);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                    //delete comment from user
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                    final SharedPreferences.Editor e = settings.edit();
                    final Firebase userData = database.child("users").child(settings.getString("user_id",null));
                    userData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            user.removeComment(w.getCommentId());
                            userData.setValue(user);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });

                   getPosts(newView);
                }

            });

            return newView;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aList = new ArrayList<ListElement>();
        cList = new ArrayList<ListElement>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_five, container, false);
        mView=view;

        return view;
    }

    private void getPosts(View v){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String userID = settings.getString("user_id", null);
        Firebase database = new Firebase("https://vivid-heat-3338.firebaseio.com/");
        // Attach an listener to read the data at our posts reference
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                aList.clear();
                cList.clear();
               // for (DataSnapshot postSnapshot : snapshot.child("users").getChildren()) {
                    final User users = snapshot.child("users").child(userID).getValue(User.class);
                   // if(users.getUser_id().equals(userID)) {
                        List<String> myPosts = users.getPosts();
                        List<String> myComments = users.getComments();
                        for(int i=0;i<myPosts.size();i++){
                            String post=myPosts.get(i);
                            String content=(String)snapshot.child("posts").child(post).child("content").getValue();
                            String timeStamp=(String)snapshot.child("posts").child(post).child("timestamp").getValue();
                            aList.add(0,new ListElement(content,timeStamp,post));
                        }
                        for(int i=0;i<myComments.size();i++){
                            String comment =myComments.get(i);
                            String content=(String)snapshot.child("comments").child(comment).child("content").getValue();
                            String timeStamp=(String)snapshot.child("comments").child(comment).child("timestamp").getValue();
                            String post=(String)snapshot.child("comments").child(comment).child("post_id").getValue();
                            cList.add(0,new ListElement(content,timeStamp,post,comment));
                        }
                aa.notifyDataSetChanged();
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aa = new MyAdapter(this.getActivity(), R.layout.list_element, aList);
        commentAdapter = new MyAdapterComments(this.getActivity(), R.layout.list_element, cList);
        ListView myListView = (ListView) view.findViewById(R.id.postListView);
        ListView cListView = (ListView) view.findViewById(R.id.commentListView);
        myListView.setAdapter(aa);
        cListView.setAdapter(commentAdapter);
        getPosts(view);
        // We notify the ArrayList adapter that the underlying list has changed,
        // triggering a re-rendering of the list.
        aa.notifyDataSetChanged();
        commentAdapter.notifyDataSetChanged();

    }

}
