package com.gamesexchange.gamesexchange.model;

/**
 * Created by bestway on 02/07/2018.
 */

public class User {

    private String password,email,firebase_uid,phoneNumber,reg_date,location ="", district="";
    private String first_name,last_name;
    private String refer;
    private String platform = "pc",profile_image;
    private String last_collected,accumlated_days;
    private int id;
    public User()
    {}

    public User(String email,String password)
    {
        this.password = password;
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return first_name + " " + last_name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebase_uid() {
        return firebase_uid;
    }

    public void setFirebase_uid(String firebase_uid) {
        this.firebase_uid = firebase_uid;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isTradeable() {
        if (!location.toLowerCase().equals("null")) {
            return true;
        }else {
            return false;
        }
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getLast_collected() {
        return last_collected;
    }

    public void setLast_collected(String last_collected) {
        this.last_collected = last_collected;
    }

    public String getAccumlated_days() {
        return accumlated_days;
    }

    public void setAccumlated_days(String accumlated_days) {
        this.accumlated_days = accumlated_days;
    }
}
