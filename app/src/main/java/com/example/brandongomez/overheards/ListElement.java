package com.example.brandongomez.overheards;

/**
 * Created by shobhit on 1/24/16.
 */
public class ListElement {
    ListElement() {};

    ListElement(String tl, String bl, String id) {
        content = tl;
        timestamp = bl;
        this.id=id;
    }

    private String content;
    private String timestamp;
    private String id;

    public String getId(){
        return id;
    }
}

