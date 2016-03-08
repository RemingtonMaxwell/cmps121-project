package com.example.brandongomez.overheards;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Test;

import java.util.List;

/**
 * Created by Jolina on 3/5/2016.
 */
public class CommentAdapter extends ArrayAdapter<CommentElement>{
    int comment_resource;
    Context comment_context;

    public CommentAdapter(Context _context, int _resource, List<CommentElement> items) {
        super(_context, _resource, items);
        comment_resource = _resource;
        comment_context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;

        CommentElement c_element  = getItem(position);

        // Inflate a new view if necessary.
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater i = (LayoutInflater) getContext().getSystemService(inflater);
            i.inflate(comment_resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        // Fills in the view.
        ImageView profile = (ImageView)newView.findViewById(R.id.c_imageView);
        TextView tv = (TextView) newView.findViewById(R.id.c_content);
        TextView name = (TextView) newView.findViewById(R.id.c_username);
        TextView time = (TextView) newView.findViewById(R.id.c_timestamp);

        profile.setImageResource(R.drawable.flanfox);
        tv.setTextColor(Color.BLACK);
        name.setTextColor(Color.BLACK);
        tv.setText(c_element.c_content);
        name.setText(c_element.c_username);
        time.setText(c_element.c_time);
        return newView;
    }
}