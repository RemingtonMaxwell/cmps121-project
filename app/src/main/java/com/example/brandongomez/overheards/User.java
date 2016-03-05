package com.example.brandongomez.overheards;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by remin on 3/3/2016.
 */
//users in database
public class User {
    private String firstName;
    private String lastName;
    //add something for images
    private String userName;
    private String emailAddress;
    private String user_id;
    private Location currentLocation;
    private List<Post> posts = new ArrayList<Post>();
    private List<Location> locations=new ArrayList<Location>();

    public User(String emailAddress, String user_id){// String user_id){
     this.emailAddress=emailAddress;
        this.user_id=user_id;
        //locations.add(new Location(100,-127));
        //locations.add(new Location(50,100));
     //this.user_id=user_id;
    }
    public User() {
        // empty default constructor, necessary for Firebase to be able to deserialize users
    }

    /******************Getter Methods for Users*************************/

    public String getEmailAddress(){
      return emailAddress;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getUser_id(){
        return user_id;
    }

    public Location getCurrentLocation(){
        return currentLocation;
    }

    public List<Post> getPosts(){
        return posts;
    }

    public List<Location> getLocations(){
        return locations;
    }

}

