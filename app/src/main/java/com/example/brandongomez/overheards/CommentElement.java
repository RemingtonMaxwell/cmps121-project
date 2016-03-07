package com.example.brandongomez.overheards;

/**
 * Created by Jolina on 3/6/2016.
 */
public class CommentElement {
    public CommentElement(){}

    public String c_content;
    public String c_profile_pic;
    public String c_username;
    public String c_time;

    public CommentElement(String c, String pic, String name, String timestamp){
        c_content = c;
        c_profile_pic = pic;
        c_username = name;
        c_time = timestamp;
    }
}
