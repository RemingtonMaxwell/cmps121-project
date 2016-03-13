package com.example.brandongomez.overheards;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Jolina on 3/5/2016.
 */
public class PostElement{
    public PostElement(){}

    public String content;
    public String profile_pic;
    public String username;
    public String time;
    public String votes;
    public String post_id;
    private int count = 0;
    public PostElement(String c, String pic, String name, String timestamp, String vote, String post){
        content = c;
        profile_pic = pic;
        username = name;
        time = timestamp;
        votes = vote;
        post_id = post;
    }

    public String voteup(){
        int intvote = Integer.parseInt(votes);
        intvote++;
        votes = Integer.toString(intvote);
        return Integer.toString(intvote);
    }

    public String votedown(){
        int intvote = Integer.parseInt(votes);
        intvote--;
        votes = Integer.toString(intvote);
        return Integer.toString(intvote);
    }

    public int clickCountUp(){
        count++;
        return count;
    }

    public int clickCountDown(){
        count--;
        return count;
    }

    public String getPost_id(){
        return post_id;
    }

}

class votesCompare implements Comparator<PostElement> {
    public int compare(PostElement post1, PostElement post2) {
        return Integer.parseInt(post2.votes) - Integer.parseInt(post1.votes);
    }
}

class dateCompare implements Comparator<PostElement> {
    public int compare(PostElement post1, PostElement post2) {
        Date post1Date = new Date (post1.time);
        Date post2Date = new Date (post2.time);
        return post2Date.compareTo(post1Date);
    }
}