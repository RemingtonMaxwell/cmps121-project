package com.example.brandongomez.overheards;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by remin on 3/3/2016.
 */
public class Post {
    private String post_id;
    private String content;
    private String user_id;
    private Location location;
    private String timestamp;
    private String type;
    private int votes;
    private String date_heard;

    public Post() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public Post(String content,String user_id, Location location,String type, String date_heard){
        post_id= new SecureRandomString().nextString();
        this.content=content;
        this.user_id=user_id;
        this.location=location;
        this.timestamp=createDate();
        this.type=type;
        this.votes=0;
        this.date_heard=date_heard;
    }

    private String createDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    /******************Getter Methods for Posts*************************/

    public String getPost_id(){
        return post_id;
    }

    public String getContent(){
        return content;
    }

    public String getUser_id(){
        return user_id;
    }

    public Location getLocation(){
        return location;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getType(){
        return type;
    }
    public int getVotes(){
        return votes;
    }

    public String getDate_heard(){
        return date_heard;
    }
}
