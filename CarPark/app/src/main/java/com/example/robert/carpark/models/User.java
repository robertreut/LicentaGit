package com.example.robert.carpark.models;


import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class User {
    private String UserID;
    private String Username;
    private String Email;
    private String PhoneNumber;
    private String PhotoUrl;
    private int Prestige;

    public User(String Username,String Email, String PhoneNumber, String PhotoUrl, int Prestige) {
        this.Username = Username;
        this.Email = Email;
        this.PhoneNumber = PhoneNumber;
        this.PhotoUrl = PhotoUrl;
        this.Prestige = Prestige;

    }

    public User() {
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
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

    public int getPrestige() {
        return Prestige;
    }

    public void setPrestige(int Prestige) {
        this.Prestige = Prestige;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + UserID + '\'' +
                ", username='" + Username + '\'' +
                ", email='" + Email + '\'' +
                ", phoneNumber='" + PhoneNumber + '\'' +
                ", photoUrl='" + PhotoUrl + '\'' +
                ", prestige=" + Prestige + '\'' +
                '}';
    }


}
