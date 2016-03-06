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
    private List<Comment> comments = new ArrayList<Comment>();

    public User(String emailAddress, String user_id,Location currentLocation){// String user_id){
     this.emailAddress=emailAddress;
        this.user_id=user_id;
        this.currentLocation=currentLocation;
    }
    public User() {
        // empty default constructor, necessary for Firebase to be able to deserialize users
    }

    /******************Getter Methods for Users*************************/

    public String getEmailAddress(){
      return emailAddress;
    }

    public void setEmailAddress(String newEmailAddress){
        emailAddress=newEmailAddress;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String newFirstName){
        firstName=newFirstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String newLastName){
        lastName=newLastName;
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

    public void addPost(Post newPost){
        posts.add(newPost);
    }

    public void removePost(Post post){
        for(int i=0;i<posts.size();i++){
            if(posts.get(i).equals(post)){
                posts.remove(post);
            }
        }
    }

    public List<Location> getLocations(){
        return locations;
    }

    public void addLocation(Location newLocation){
        locations.add(newLocation);
    }

    public void removeLocation(Location location){
        for (int i=0;i<locations.size();i++){
            if(locations.get(i).equals(location)){
                locations.remove(location);
            }
        }
    }

    public List<Comment> getComments(){
        return comments;
    }

    public void addComment(Comment newComment){
        comments.add(newComment);
    }

    public void removeComment(Comment comment){
        for (int i=0; i<comments.size();i++){
            if(comments.get(i).equals(comment)){
                comments.remove(comment);
            }
        }
    }

}

