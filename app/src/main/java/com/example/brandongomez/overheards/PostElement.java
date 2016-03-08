package com.example.brandongomez.overheards;

/**
 * Created by Jolina on 3/5/2016.
 */
public class PostElement {
    public PostElement(){}

    public String content;
    public String profile_pic;
    public String username;
    public String time;
    public String votes;
    private int count = 0;
    public PostElement(String c, String pic, String name, String timestamp, String vote){
        content = c;
        profile_pic = pic;
        username = name;
        time = timestamp;
        votes = vote;
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
}
