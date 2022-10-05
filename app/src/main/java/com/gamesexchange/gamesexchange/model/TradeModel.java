package com.gamesexchange.gamesexchange.model;

public class TradeModel {
    private String locationOfUser;
    private String idOfMyUser, idOfCommonUser;
    private int onlineStatus;
    private String gamesIWant;
    private String firstNameOfUser,lastNameOfUser;

    private String district;
    private String profileImage;
    private String gamesOfCommonUser,wishesOfCommonUser;
    private String token;

    public TradeModel()
    {
    }


    public String getLocationOfUser() {
        return locationOfUser;
    }

    public void setLocationOfUser(String locationOfUser) {
        this.locationOfUser = locationOfUser;
    }

    public String getNameOfUser() {
        return firstNameOfUser + " " + lastNameOfUser;
    }


    public String getIdOfMyUser() {
        return idOfMyUser;
    }

    public void setIdOfMyUser(String idOfMyUser) {
        this.idOfMyUser = idOfMyUser;
    }

    public String getIdOfCommonUser() {
        return idOfCommonUser;
    }

    public void setIdOfCommonUser(String idOfCommonUser) {
        this.idOfCommonUser = idOfCommonUser;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getGamesIWant() {
        return gamesIWant;
    }

    public void setGamesIWant(String gamesIWant) {
        this.gamesIWant = gamesIWant;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public String getFirstNameOfUser() {
        return firstNameOfUser;
    }

    public void setFirstNameOfUser(String firstNameOfUser) {
        this.firstNameOfUser = firstNameOfUser;
    }

    public String getLastNameOfUser() {
        return lastNameOfUser;
    }

    public void setLastNameOfUser(String lastNameOfUser) {
        this.lastNameOfUser = lastNameOfUser;
    }

    public String getGamesOfCommonUser() {
        return gamesOfCommonUser;
    }

    public void setGamesOfCommonUser(String gamesOfCommonUser) {
        this.gamesOfCommonUser = gamesOfCommonUser;
    }

    public String getWishesOfCommonUser() {
        return wishesOfCommonUser;
    }

    public void setWishesOfCommonUser(String wishesOfCommonUser) {
        this.wishesOfCommonUser = wishesOfCommonUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
