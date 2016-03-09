package com.example.brandongomez.overheards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.client.Firebase;
import com.firebase.client.Transaction;
import com.firebase.client.MutableData;
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;

import java.util.List;

/**
 * Created by Jolina on 3/5/2016.
 */
public class PostListAdapter extends ArrayAdapter<PostElement>{
    int resource;
    Context context;
    public int pos;

    public PostListAdapter(Context _context, int _resource, List<PostElement> items) {
        super(_context, _resource, items);
        resource = _resource;
        context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;
        pos = position;
        Log.i("PostAdapter", ""+pos);

        final PostElement element  = getItem(position);
        final int votes = Integer.parseInt(element.votes);

        // Inflate a new view if necessary.
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater i = (LayoutInflater) getContext().getSystemService(inflater);
            i.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        // Fills in the view.
        ImageView profile = (ImageView)newView.findViewById(R.id.imageView);
        TextView tv = (TextView) newView.findViewById(R.id.content);
        TextView name = (TextView) newView.findViewById(R.id.username);
        TextView time = (TextView) newView.findViewById(R.id.timestamp);
        final TextView upvotes = (TextView)newView.findViewById(R.id.votes);
        final Button upvote = (Button)newView.findViewById(R.id.upvoteButton);
        final Button downvote = (Button)newView.findViewById(R.id.downvoteButton);

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvotes.setText(element.voteup());
                int counter = element.clickCountUp();

                if(counter == 1) {
                    upvote.setClickable(false);
                }
                downvote.setClickable(true);
                Firebase upvotesRef = new Firebase("https://vivid-heat-3338.firebaseio.com/posts/"+element.post_id+"/votes");
                upvotesRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData currentData) {
                        if (currentData.getValue() == null) {
                            currentData.setValue(1);
                        } else {
                            currentData.setValue((Long) currentData.getValue() + 1);
                        }
                        return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                        //This method will be called once with the results of the transaction.
                    }
                });
                upvote.setClickable(false);
            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvotes.setText(element.votedown());
                int counter = element.clickCountDown();

                if (counter == -1) {
                    downvote.setClickable(false);
                }
                upvote.setClickable(true);
                Firebase upvotesRef = new Firebase("https://vivid-heat-3338.firebaseio.com/posts/" + element.post_id + "/votes");
                upvotesRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData currentData) {
                        if (currentData.getValue() == null) {
                            currentData.setValue(1);
                        } else {
                            currentData.setValue((Long) currentData.getValue() - 1);
                        }
                        return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                        //This method will be called once with the results of the transaction.
                    }
                });
                downvote.setClickable(false);
            }
        });
        if(element.username.compareTo("hello")==0) {
            //convert from image string
            byte[] imageAsBytes = Base64.decode(element.profile_pic, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            profile.setImageBitmap(bitmap);
        }else {
            profile.setImageResource(R.drawable.flanfox);
        }
        tv.setTextColor(Color.BLACK);
        name.setTextColor(Color.BLACK);
        tv.setText(element.content);
        name.setText(element.username);
        time.setText(element.time);
        upvotes.setText(element.votes);

        return newView;
    }
}
