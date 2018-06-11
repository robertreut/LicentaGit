package com.example.robert.carpark.models;

/**
 * Created by Robert on 11.06.2018.
 */

public class Upload {
    private String mName;
    private String mImageUrl;

    public Upload() {

    }

    public Upload(String name, String imageUrl) {
        if(name.trim().equals("")) {
            name = "No name";
        }
        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName(String name) {
        return name;
    }

    public void setName(String name) {
        mName =name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}

