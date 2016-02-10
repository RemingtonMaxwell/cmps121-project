package com.example.brandongomez.cs121asg3.response; ;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QueryResponse {

    @SerializedName("result_list")
    @Expose
    //public ResultList resultList;
    public List<ResultList> resultList = new ArrayList<ResultList>();
    @SerializedName("result")
    @Expose
    public String result;

}