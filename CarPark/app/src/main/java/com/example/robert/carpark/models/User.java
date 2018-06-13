package com.example.robert.carpark.models;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class User implements Comparable<User>{
    private String UserID;
    private String Username;
    private String PhoneNumber;
    private String PhotoUrl;
    private long Prestige;

    public User(String UserID, String Username, String PhoneNumber, String PhotoUrl, long Prestige) {
        this.UserID = UserID;
        this.Username = Username;
        this.PhoneNumber = PhoneNumber;
        this.PhotoUrl = PhotoUrl;
        this.Prestige = Prestige;

    }

    public User() {
    }
    public String getUserID() { return UserID; }

    public void setUserID(String UserID) {this.UserID = UserID;}

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }

    public long getPrestige() {
        return Prestige;
    }

    public void setPrestige(long Prestige) {
        this.Prestige = Prestige;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + UserID + '\'' +
                ", username='" + Username + '\'' +
                ", phoneNumber='" + PhoneNumber + '\'' +
                ", photoUrl='" + PhotoUrl + '\'' +
                ", prestige=" + Prestige + '\'' +
                '}';
    }


    @Override
    public int compareTo(@NonNull User compareUser) {

        int comparePrestige = (int)((User) compareUser).getPrestige();
        return comparePrestige - (int)this.Prestige;
    }
}
