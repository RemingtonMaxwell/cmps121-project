package com.example.brandongomez.cs121asg3.response; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultList {

    @SerializedName("timestamp")
    @Expose
    public String timestamp;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("nickname")
    @Expose
    public String nickname;
    @SerializedName("message_id")
    @Expose
    public String messageId;
    @SerializedName("user_id")
    @Expose
    public String userId;

}