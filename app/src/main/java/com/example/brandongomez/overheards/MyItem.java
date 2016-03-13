package com.example.brandongomez.overheards;

import com.google.maps.android.clustering.ClusterItem;
import com.google.android.gms.maps.model.LatLng;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private String post_id;

    public MyItem(double lat, double lng, String t, String s) {
        mPosition = new LatLng(lat, lng);
        mTitle = t;
        mSnippet = s;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getSnippet(){
        return mSnippet;
    }

    public void setPostId(String id) {
        post_id=id;
    }

    public String getPostId(){
        return post_id;
    }
}
