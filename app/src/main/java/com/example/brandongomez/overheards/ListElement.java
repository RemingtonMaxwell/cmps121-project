package com.example.brandongomez.overheards;

/**
 * Created by shobhit on 1/24/16.
 */
public class ListElement {
    ListElement() {};

    ListElement(String tl, String bl) {
        restaurantName = tl;
        details = bl;
    }

    public String restaurantName;
    public String details;  //Restaurant details button. Currently not implemented
}
