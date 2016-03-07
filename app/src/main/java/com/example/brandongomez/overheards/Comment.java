package com.example.brandongomez.overheards;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by remin on 3/3/2016.
 */
public class Comment {
    private String comment_id;
    private String post_id;
    private String user_id;
    private String content;
    private String timestamp;

    public Comment(){
        // empty default constructor, necessary for Firebase to be able to deserialize comments
    }

    public Comment(String post_id, String user_id, String content){
        this.comment_id=new SecureRandomString().nextString();
        this.post_id=post_id;
        this.user_id=user_id;
        this.content=content;
        this.timestamp=createDate();

    }
    private String createDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    /******************Getter Methods for Comments*************************/

    public String getComment_id(){
        return comment_id;
    }

    public String getPost_id(){
        return post_id;
    }

    public String getUser_id(){
        return user_id;
    }

    public String getContent(){
        return content;
    }

    public String getTimestamp(){
        return timestamp;
    }
}
